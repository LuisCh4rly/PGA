package com.app.pga.App.Services.Implements;

import com.app.pga.App.Exception.DuplicateResourceException;
import com.app.pga.App.Exception.NotFoundException;
import com.app.pga.App.Exception.ResourceDisabledException;
import com.app.pga.App.Models.Dtos.RequestDto.InscripcionRequestDto;
import com.app.pga.App.Models.Dtos.ResponseDto.*;
import com.app.pga.App.Models.Entities.Grupo;
import com.app.pga.App.Models.Entities.Inscripcion;
import com.app.pga.App.Models.Entities.Usuario;
import com.app.pga.App.Models.Enum.Estado;
import com.app.pga.App.Models.Filtros.InscripcionFiltro;
import com.app.pga.App.Models.Mappers.InscripcionMapper;
import com.app.pga.App.Models.Specification.InscripcionSpecification;
import com.app.pga.App.Repositories.IGrupoRepository;
import com.app.pga.App.Repositories.IInscripcionRepository;
import com.app.pga.App.Repositories.IUsuarioRepository;
import com.app.pga.App.Services.Interfaces.IActividadAlumnoService;
import com.app.pga.App.Services.Interfaces.IInscripcionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional

public class InscripcionService implements IInscripcionService {

    private final InscripcionMapper inscripcionMapper;
    private final IInscripcionRepository inscripcionRepository;
    private final IGrupoRepository grupoRepository;
    private final IActividadAlumnoService actividadAlumnoService;
    private final IUsuarioRepository usuarioRepository;

    public InscripcionService (InscripcionMapper inscripcionMapper, IInscripcionRepository inscripcionRepository, IUsuarioRepository usuarioRepository, IGrupoRepository grupoRepository, IActividadAlumnoService actividadAlumnoService){
        this.inscripcionMapper=inscripcionMapper;
        this.inscripcionRepository=inscripcionRepository;
        this.usuarioRepository = usuarioRepository;
        this.grupoRepository = grupoRepository;
        this.actividadAlumnoService= actividadAlumnoService;
    }
    //Crear inscripciones
    public InscripcionResponseDto createInscripcion (InscripcionRequestDto inscripcionDto){
        Usuario usuario = usuarioRepository.findAlumnoById(inscripcionDto.idUsuario())
                .orElseThrow(()->new NotFoundException("Alumno no encontrado."));
        if(!usuario.getActivo()){
            throw new ResourceDisabledException("Alumno deshbilitado");
        }
        //verificar que el alumno no tenga inscripciones activas
        inscripcionRepository.findByUsuario_IdUsuarioAndEstadoTrue(inscripcionDto.idUsuario()).ifPresent(InscripcionDto->{
            throw new DuplicateResourceException("El alumno tiene una inscripción activa");
        });

        Inscripcion inscripcionEntity = inscripcionMapper.toEntity(inscripcionDto);
        if (inscripcionEntity.getEstado()==null){
            inscripcionEntity.setEstado(true);
            inscripcionEntity.setFechaInscripcion(LocalDate.now());
            inscripcionEntity.setUsuario(usuario);
        }
        Inscripcion nuevaInscripcion = inscripcionRepository.save(inscripcionEntity);
        return inscripcionMapper.toDto(nuevaInscripcion);
    }

    //consulta para inscripciones activas
    @Transactional(readOnly = true)
    public List<InscripcionResponseDto>findAllActivos(){
        return inscripcionRepository.findByEstadoTrue()
                .stream()
                .map(inscripcion ->inscripcionMapper.toDto(inscripcion))
                .collect(Collectors.toList());
    }

    //consulta de alumnos por grupo
    @Transactional(readOnly = true)
    public List<AlumnoGrupoDto> obtenerAlumnosPorGrupo(Long idGrupo){
        List<Inscripcion> inscripciones = inscripcionRepository.findByGrupo_IdGrupoAndEstadoTrue(idGrupo);
        if(inscripciones.isEmpty()){
            throw new NotFoundException("No hay alumnos en este grupo");
        }

        return inscripciones.stream()
                .map(ins -> new AlumnoGrupoDto(
                        ins.getIdInscripcion(),
                        ins.getUsuario().getNombre(),
                        ins.getUsuario().getApellidoPaterno(),
                        ins.getUsuario().getApellidoMaterno(),
                        ins.getEstado()
                ))
                .toList();
    }

    //consulta de alumnos por grupo general
    @Transactional(readOnly = true)
    public List<AlumnoGrupoDto> obtenerAlumnosPorGrupoGeneral(Long idGrupo){
        List<Inscripcion> inscripciones = inscripcionRepository.findByGrupo_IdGrupo(idGrupo);
        if(inscripciones.isEmpty()){
            throw new NotFoundException("No hay alumnos en este grupo");
        }

        return inscripciones.stream()
                .map(ins -> new AlumnoGrupoDto(
                        ins.getIdInscripcion(),
                        ins.getUsuario().getNombre(),
                        ins.getUsuario().getApellidoPaterno(),
                        ins.getUsuario().getApellidoMaterno(),
                        ins.getEstado()
                ))
                .toList();
    }

    //consulta general
    @Transactional(readOnly = true)
    public Page<InscripcionResponseDto> findAll(InscripcionFiltro filtro, Pageable pageable){
        return inscripcionRepository.findAll(InscripcionSpecification.filtrar(filtro), pageable)
                .map(inscripcion ->inscripcionMapper.toDto(inscripcion));

    }


    //consulta por id
    @Transactional(readOnly = true)
    public InscripcionResponseDto finfById(Long idInscripcion){
        Inscripcion inscripcion = inscripcionRepository.findById(idInscripcion)
                .orElseThrow(()->new NotFoundException("Registro no encontrado: "+idInscripcion));
        return inscripcionMapper.toDto(inscripcion);
    }

    //activar desactivar
    public InscripcionResponseDto desactivarActivarInscripcion(Long idInscripcion){
        Inscripcion inscripcion = inscripcionRepository.findById(idInscripcion)
                .orElseThrow(()->new NotFoundException("Registro no encontrado: "+idInscripcion));
        if(inscripcion.getEstado()==true){
            inscripcion.setFechaBaja(LocalDate.now());
        }else{
            inscripcionRepository.findByUsuario_IdUsuarioAndEstadoTrue(inscripcion.getUsuario().getIdUsuario())
                    .ifPresent(InscripcionDto->{
                throw new DuplicateResourceException("El alumno tiene una inscripción activa");
            });
            if( inscripcion.getGrupo().getEstado()==Estado.DESHABILITADO){
                throw new ResourceDisabledException("El grupo se encuentra deshabilitado");
            }
            inscripcion.setFechaBaja(null);
            inscripcion.setFechaInscripcion(LocalDate.now());
        }
        inscripcion.setEstado(!inscripcion.getEstado());
        Inscripcion inscripcionDes = inscripcionRepository.save(inscripcion);
        return inscripcionMapper.toDto(inscripcionDes);
    }


    //Asignar grupo
    public InscripcionResponseDto asignarGrupo (Long idInscripcion, Long idGrupo){
        Inscripcion inscripcion = inscripcionRepository.findById(idInscripcion)
                .orElseThrow(()->new NotFoundException("Inscripción no encontrada: " + idInscripcion));

        if (!inscripcion.getEstado()) {
            throw new ResourceDisabledException("La inscripción no está activa");
        }

        Grupo grupo = grupoRepository.findById(idGrupo)
                .orElseThrow(() -> new NotFoundException("Grupo no encontrado"));
        if (grupo.getEstado() == Estado.DESHABILITADO) {
            throw new ResourceDisabledException("El grupo no está activo");
        }

        boolean existe = inscripcionRepository.existsByUsuario_IdUsuarioAndGrupo_IdGrupoAndGrupo_Estado(
                inscripcion.getUsuario().getIdUsuario(),
                grupo.getIdGrupo(),
                Estado.HABILITADO);

        if (existe) {
                throw new DuplicateResourceException("El alumno ya está inscrito en este grupo");
        }
        Grupo grupoAnterior = inscripcion.getGrupo();

        if (grupoAnterior != null) {
            throw new DuplicateResourceException("Inscripción ya cuenta con grupo asociado");
        }

        inscripcion.setGrupo(grupo);
        actividadAlumnoService.asignarActividadesGrupalesPorInscripcion(inscripcion.getIdInscripcion());
        return inscripcionMapper.toDto(inscripcionRepository.save(inscripcion));
        }

    @Transactional(readOnly = true)
    public List<Inscripcion> obtenerPorAlumno(Long idAlumno){
        return inscripcionRepository.obtenerInscripcionesAlumno(idAlumno);
    }

    //consulta para inscripciones generales reporte
    @Transactional(readOnly = true)
    public List<InscripcionReporteDto>findAll(){
        return inscripcionRepository.obtenerInscripcionReporte();
    }

    //consulta del grupo por alumno con inscripcion activa
    @Transactional(readOnly = true)
    public Optional<GrupoAlumnoDto> grupoAlumnoInscripcion (Long idUsuario){
        usuarioRepository.findAlumnoById(idUsuario)
                .orElseThrow(()->new NotFoundException("Alumno no encontrado."));

        boolean existe = inscripcionRepository.existeInscripcionActiva(idUsuario);

        if (!existe) {
            throw new ResourceDisabledException("El alumno no cuenta con una inscripción activa");
        }

        return inscripcionRepository.findGrupoActivoByUsuario(idUsuario);
    }

    //consulta de la lista de los grupos con inscripciones desactivadas
    public List<GrupoAlumnoDto> gruposAlumnoInscripcion (Long idUsuario){
        usuarioRepository.findAlumnoById(idUsuario)
                .orElseThrow(()->new NotFoundException("Alumno no encontrado."));

        List <GrupoAlumnoDto> gruposInactivos = inscripcionRepository.buscarInscripcionesInactivas(idUsuario);

        if (gruposInactivos.isEmpty()) {
            throw new ResourceDisabledException("El alumno no cuenta con inscripciones inactivas");
        }

        return gruposInactivos;
    }

    public int obtenerAlumnosActivosConteo() {
        return usuarioRepository.countAlumnosActivos();
    }
    public int obtenerAlumnosInactivosConteo() {
        return usuarioRepository.countAlumnosInactivos();
    }
    public int obtenerDocentesActivosConteo() {
        return usuarioRepository.countDocentesActivos();
    }
    public int obtenerDocentesInactivosConteo() {
        return usuarioRepository.countDocentesInactivos();
    }

    }


