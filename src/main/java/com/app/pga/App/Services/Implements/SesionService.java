package com.app.pga.App.Services.Implements;

import com.app.pga.App.Exception.NotFoundException;
import com.app.pga.App.Exception.ResourceDisabledException;
import com.app.pga.App.Models.Dtos.*;
import com.app.pga.App.Models.Entities.*;
import com.app.pga.App.Models.Enum.Alcance;
import com.app.pga.App.Models.Enum.Estado;
import com.app.pga.App.Models.Enum.EstadoAsistencia;
import com.app.pga.App.Models.Mappers.AsistenciaMapper;
import com.app.pga.App.Models.Mappers.GrupoMapper;
import com.app.pga.App.Models.Mappers.SesionAlumnoMapper;
import com.app.pga.App.Models.Mappers.SesionMapper;
import com.app.pga.App.Repositories.*;
import com.app.pga.App.Services.Interfaces.ISesionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class SesionService implements ISesionService {

    private final IInscripcionRepository inscripcionRepository;
    private final ISesionRepository sesionRepository;
    private final ISesionAlumnoRepository sesionAlumnoRepository;
    private final SesionMapper sesionMapper;
    private final AsistenciaRepository asistenciaRepository;
    private final IGrupoRepository iGrupoRepository;
    private final GrupoMapper grupoMapper;
    private final IDocenteRepository docenteRepository;


    public SesionService (ISesionRepository sesionRepository, IInscripcionRepository inscripcionRepository, ISesionAlumnoRepository sesionAlumnoRepository, SesionMapper sesionMapper, AsistenciaRepository asistenciaRepository, IGrupoRepository iGrupoRepository, GrupoMapper grupoMapper, IDocenteRepository docenteRepository){
        this.sesionRepository=sesionRepository;
        this.inscripcionRepository=inscripcionRepository;
        this.sesionAlumnoRepository=sesionAlumnoRepository;
        this.sesionMapper=sesionMapper;
        this.asistenciaRepository = asistenciaRepository;
        this.iGrupoRepository = iGrupoRepository;
        this.grupoMapper = grupoMapper;
        this.docenteRepository = docenteRepository;
    }
//crear sesion
    public SesionDto createSesion (SesionRequestDto dto, Long idGrupo){
        Grupo grupo= iGrupoRepository.findByIdGrupo(idGrupo).orElseThrow(()->new NotFoundException("Grupo no encontrado."));
        if(grupo.getEstado()== Estado.DESHABILITADO){
            throw new ResourceDisabledException("Grupo deshbilitado");
        }
        //verifiicar que el docente no tenga sesion a esa hora
        boolean existe = sesionRepository.existsByGrupo_Docente_IdDocenteAndFecha(grupo.getDocente().getIdDocente(), dto.fecha());
        if (existe) {
            throw new IllegalStateException("El docente ya tiene una sesión en ese horario");
        }

        Sesion sesion = sesionMapper.toEntiity(dto);
        sesion.setGrupo(grupo);
        sesionRepository.save(sesion);

        List<SesionAlumno>relaciones = new ArrayList<>();

        if(sesion.getAlcance()==Alcance.GRUPAL){
           //obtenemos los alumnos incritos
            List<Inscripcion>inscripciones = inscripcionRepository.findByGrupo_IdGrupoAndEstadoTrue(idGrupo);
            if (inscripciones.isEmpty()){
                throw new NotFoundException("No existen alumnos inscritos al grupo");}
            relaciones = inscripciones.stream().map(inscripcion -> {
                SesionAlumno sa = new SesionAlumno();
                sa.setSesion(sesion);
                sa.setInscripcion(inscripcion);
                return sa;
            }).toList();

        } else {//alcance individual
            if (dto.idsInscripcion() == null || dto.idsInscripcion().isEmpty()) {
                throw new IllegalArgumentException("Debe enviar alumnos para una sesión indivudual");}

            relaciones = dto.idsInscripcion().stream()
                    .distinct()
                    .map(id -> {
                        inscripcionRepository.findById(id).orElseThrow(() ->
                                new NotFoundException ("Inscripción no encontrada: " + id));
                        Inscripcion ins = inscripcionRepository.findByIdInscripcionAndGrupo_IdGrupoAndEstadoTrue(id, idGrupo).orElseThrow(() ->
                                new IllegalArgumentException("La inscripcion: "+id+" no pertenece al grupo"));
                        SesionAlumno sa = new SesionAlumno();
                        sa.setSesion(sesion);
                        sa.setInscripcion(ins);
                        return sa;
                    })
                    .toList();
        }
        sesionAlumnoRepository.saveAll(relaciones);

        return sesionMapper.toDto(sesion);
    }


    //actualizar inscripciones a sesion invidivual
    public SesionDetalletDto actualizarInscripcionesSesion(Long idSesion, List<Long> nuevosIdsInscripcion) {

        Sesion sesion = sesionRepository.findById(idSesion)
                .orElseThrow(() -> new NotFoundException("Sesión no encontrada"));

        if (sesion.getAlcance() == Alcance.GRUPAL) {
            throw new IllegalStateException(
                    "No se pueden modificar inscripciones de una sesión grupal");
        }

        // borrar relaciones actuales
        sesionAlumnoRepository.deleteBySesionIdSesion(idSesion);

        // crear nuevas relaciones
        List<SesionAlumno> nuevasRelaciones = nuevosIdsInscripcion.stream()
                .distinct()
                .map(id -> {
                    Inscripcion ins = inscripcionRepository.findById(id)
                            .orElseThrow(() ->
                                    new NotFoundException("Inscripción no encontrada: " + id));
                    SesionAlumno sa = new SesionAlumno();
                    sa.setSesion(sesion);
                    sa.setInscripcion(ins);
                    return sa;
                })
                .toList();

        sesionAlumnoRepository.saveAll(nuevasRelaciones);

        return sesionMapper.toDetalleDto(sesion);
    }

//pasar asistencia
@Transactional
public SesionDetalletDto tomarAsistencia(Long idSesion, List<AsistenciaDto> listaAsistencia) {

    Sesion sesion = sesionRepository.findById(idSesion).orElseThrow(()->new NotFoundException("Sesion no encontrada"));

    // obtener TODAS las relaciones sesión-alumno
    List<SesionAlumno> relaciones = sesionAlumnoRepository.findBySesionIdSesion(idSesion);

    if (relaciones.isEmpty()) {
        throw new NotFoundException("No existen alumnos asignados a esta sesión");
    }

    // Map para acceso rápido
    Map<Long, EstadoAsistencia> estadoPorSesionAlumno =
            listaAsistencia.stream()
                    .collect(Collectors.toMap(
                            AsistenciaDto::idSesionAlumno,
                            AsistenciaDto::estado));

    for (SesionAlumno sa : relaciones) {
        EstadoAsistencia estado =
                estadoPorSesionAlumno.getOrDefault(
                        sa.getIdSesionAlumno(),
                        EstadoAsistencia.FALTO);

        Asistencia asistencia = sa.getAsistencia();
        if (asistencia == null) {
            asistencia = new Asistencia();
            asistencia.setSesionAlumno(sa);
        }
        asistencia.setEstado(estado);
        asistenciaRepository.save(asistencia);
    }

    return obtenerDetalleSesion(idSesion);
}

//consultar asistencia de una sesion
    @Transactional(readOnly = true)
    public SesionDetalletDto obtenerDetalleSesion (Long idSesion){
        Sesion sesion = sesionRepository.findById(idSesion).orElseThrow(()->new NotFoundException("Sesion no encontrada"));
        List<SesionAlumnoDetalleDto> alumnos = sesionAlumnoRepository.findBySesionIdSesion(idSesion)
                .stream()
                .map(sa->new SesionAlumnoDetalleDto(
                        sa.getIdSesionAlumno(),
                        sa.getInscripcion().getIdInscripcion(),
                        sa.getInscripcion().getAlumno().getUsuario().getNombre(),
                        sa.getInscripcion().getAlumno().getUsuario().getApellidoPaterno(),
                        sa.getInscripcion().getAlumno().getUsuario().getApellidoMaterno(),
                        sa.getAsistencia()!=null
                                ? sa.getAsistencia().getEstado()
                                : EstadoAsistencia.FALTO
                ))
                .toList();

        return new SesionDetalletDto(
                sesion.getIdSesion(),
                sesion.getFecha(),
                sesion.getTema(),
                sesion.getAlcance(),
                sesion.getPlataforma(),
                grupoMapper.toDtoSimple(sesion.getGrupo()),
                alumnos);
    }

    //consultar sesiones por grupo
    @Transactional(readOnly = true)
    public List<SesionDto>obtenerSesionesPorGrupo(Long idGrupo){
        Grupo grupo= iGrupoRepository.findByIdGrupo(idGrupo).orElseThrow(()->new NotFoundException("Grupo no encontrado."));
        if(grupo.getEstado()== Estado.DESHABILITADO){
            throw new ResourceDisabledException("Grupo deshbilitado");
        }
        return sesionRepository.findByGrupo_IdGrupo(idGrupo)
                .stream()
                .map(sesionMapper::toDto)
                .toList();
    }

    //consultar sesiones por docente
    @Transactional(readOnly = true)
    public List<SesiondocenteDto>obtenerSesionPorDocente(Long idDocente){
        Docente docente = docenteRepository.findById(idDocente)
                .orElseThrow(() -> new NotFoundException("Docente no encontrado"));
        if (!docente.getActivo()) {
            throw new ResourceDisabledException("Docente deshabilitado");
        }
        return sesionRepository.findByGrupo_Docente_IdDocente(idDocente)
                .stream()
                .map(sesion -> new SesiondocenteDto(
                        sesion.getIdSesion(),
                        sesion.getFecha(),
                        sesion.getTema(),
                        sesion.getPlataforma(),
                        sesion.getGrupo().getNombre()
                )).toList();
    }
    //consultar sesiones por alumno
    @Transactional(readOnly = true)
    public List<SesiondocenteDto>obtenerSesionPorAlumno(Long idInscripcion){
        Inscripcion inscripcion = inscripcionRepository.findById(idInscripcion)
                .orElseThrow(() -> new NotFoundException("Inscripcion no encontrada"));
        if (!inscripcion.getEstado()) {
            throw new ResourceDisabledException("Inscripción deshabilitado");
        }
        List<SesionAlumno>relaciones=sesionAlumnoRepository.findByInscripcion_IdInscripcion(idInscripcion);
        if(relaciones==null||relaciones.isEmpty()){
            throw new NotFoundException("El alumno no tiene sesiones asignadas");
        }
        return relaciones.stream()
                .map(sesion ->{Sesion s = sesion.getSesion();
                        return new SesiondocenteDto(
                        s.getIdSesion(),
                        s.getFecha(),
                        s.getTema(),
                        s.getPlataforma(),
                        s.getGrupo().getNombre()
                );}).toList();
    }

    //Actualizar detalles de la sesion
    public SesionDetalletDto actualizarSesion (Long idSesion, SesionUpdateDto dto ){
        Sesion sesion = sesionRepository.findById(idSesion)
                .orElseThrow(() -> new NotFoundException("Sesión no encontrada"));

        //validamos que la sesion no sea pasada
        if(!sesion.getFecha().isAfter(LocalDateTime.now())){
            throw new IllegalStateException("No se puede modificar una sesión que ya inició o finalizó");
        }
        sesion.setFecha(dto.fecha());
        sesion.setTema(dto.tema());
        sesion.setUrlSesion(dto.urlSesion());
        sesion.setPlataforma(dto.plataforma());

        return sesionMapper.toDetalleDto(sesionRepository.save(sesion));
    }

}
