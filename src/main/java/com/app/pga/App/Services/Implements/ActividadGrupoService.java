package com.app.pga.App.Services.Implements;

import com.app.pga.App.Exception.NotFoundException;
import com.app.pga.App.Exception.ResourceDisabledException;
import com.app.pga.App.Models.Dtos.ActividadGrupoDto;
import com.app.pga.App.Models.Dtos.RequestDto.AsignarActividadCatalogoDto;
import com.app.pga.App.Models.Dtos.RequestDto.AsignarActividadExtraDto;
import com.app.pga.App.Models.Dtos.ResponseDto.ActividadGrupoDashboardDto;
import com.app.pga.App.Models.Entities.*;
import com.app.pga.App.Models.Enum.Alcance;
import com.app.pga.App.Models.Enum.Estado;
import com.app.pga.App.Models.Enum.EstadoTarea;
import com.app.pga.App.Models.Enum.Origen;
import com.app.pga.App.Models.Mappers.ActividadGrupoMapper;
import com.app.pga.App.Repositories.*;
import com.app.pga.App.Services.Interfaces.IActividadGrupoService;
import com.app.pga.App.Services.Interfaces.IStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ActividadGrupoService implements IActividadGrupoService {
    private final IActividadGrupoRepository actividadGrupoRepository;
    private final ICurso_ActividadBaseRepository cursoActividadBaseRepository;
    private final IGrupoRepository grupoRepository;
    private final IActividadBaseRepository actividadBaseRepository;
    private final IInscripcionRepository inscripcionRepository;
    private final IActividadAlumnoRepository actividadAlumnoRepository;
    private final ActividadGrupoMapper actividadGrupoMapper;
    private final StorageService storageService;



    public void precargarDesdeCurso(Long idGrupo) {

        Grupo grupo = grupoRepository.findById(idGrupo)
                .orElseThrow(() -> new NotFoundException("Grupo no encontrado"));
        if(grupo.getEstado()==Estado.DESHABILITADO){
            throw new ResourceDisabledException("Grupo deshabilitado");
        }
        List<ActividadBase> actividades =
                cursoActividadBaseRepository
                        .findByCurso_IdCursoAndActividadBase_Activo(
                                grupo.getCurso().getIdCurso(),
                                true
                        )
                        .stream()
                        .map(Curso_ActividadBase::getActividadBase)
                        .toList();

        for (ActividadBase ab : actividades) {

            ActividadGrupo ag = new ActividadGrupo();
            ag.setTitulo(ab.getTitulo());
            ag.setDescripcion(ab.getDescripcion());
            ag.setFechaAsignacion(LocalDate.now());
            ag.setReqEntrega(true);
            ag.setAlcance(Alcance.GRUPAL);
            ag.setOrigen(Origen.CURSO);
            ag.setCampo(ab.getCampoFormativo().getNombre());
            ag.setGrupo(grupo);
            ag = actividadGrupoRepository.save(ag);
            ag.setUrlInstrucciones(storageService.copiarInstruccionesActividadBase(ab.getUrlInstrucciones(), ag.getIdActividadGrupo()));
            ag = actividadGrupoRepository.save(ag);
        }
    }

    @Override
    public ActividadGrupoDto asignarDesdeCatalogo(Long idGrupo, AsignarActividadCatalogoDto dto, MultipartFile archivo) {

        ActividadBase ab = actividadBaseRepository.findById(dto.idActividadBase())
                    .orElseThrow(() -> new NotFoundException("Actividad Base no encontrada"));
            if (!ab.getActivo())
                throw new ResourceDisabledException("Actividad Base deshabilitada");

            Grupo grupo = grupoRepository.findById(idGrupo)
                    .orElseThrow(()->new NotFoundException("Grupo no encontrado"));
            if (grupo.getEstado()== Estado.DESHABILITADO)
                throw new ResourceDisabledException("Grupo deshabilitado");

        if(!inscripcionRepository.existsByGrupo_IdGrupoAndEstadoTrue(idGrupo))
            throw new NotFoundException("El grupo no tiene inscripciones");

            ActividadGrupo ag = new ActividadGrupo();
            ag.setTitulo(ab.getTitulo());
            ag.setDescripcion(ab.getDescripcion());
            ag.setFechaAsignacion(LocalDate.now());
            ag.setReqEntrega(dto.reqEntrega());
            ag.setAlcance(dto.alcance());
            ag.setOrigen(Origen.CATALOGO);
            ag.setCampo(ab.getCampoFormativo().getNombre());
            ag.setGrupo(grupo);
            ActividadGrupo an = actividadGrupoRepository.save(ag);

           if (archivo != null && !archivo.isEmpty()){
               an.setUrlInstrucciones   (
                       storageService.guardarInstruccionesActividadGrupo(an.getIdActividadGrupo(), archivo, an.getTitulo()));
           }else {
               an.setUrlInstrucciones(
                       storageService.copiarInstruccionesActividadBase(ab.getUrlInstrucciones(), an.getIdActividadGrupo()));
           }
            ag = actividadGrupoRepository.save(ag);
            asignarActividad(ag, dto.idInscripciones());

            return actividadGrupoMapper.toDto(ag);
    }
    @Override
    public ActividadGrupoDto crearExtra( Long idGrupo, AsignarActividadExtraDto dto,MultipartFile archivo) {
        Grupo grupo = grupoRepository.findById(idGrupo)
                .orElseThrow(()->new NotFoundException("Grupo no encontrado"));
        if (grupo.getEstado()== Estado.DESHABILITADO)
            throw new ResourceDisabledException("Grupo deshabilitado");
        if(!inscripcionRepository.existsByGrupo_IdGrupoAndEstadoTrue(idGrupo))
            throw new NotFoundException("El grupo no tiene inscripciones");

        ActividadGrupo ag = new ActividadGrupo();
        ag.setTitulo(dto.titulo());
        ag.setDescripcion(dto.descripcion());
        ag.setFechaAsignacion(LocalDate.now());
        ag.setReqEntrega(dto.reqEntrega());
        ag.setAlcance(dto.alcance());
        ag.setOrigen(Origen.EXTRA);
        ag.setGrupo(grupo);
        ag= actividadGrupoRepository.save(ag);
        if (archivo != null && !archivo.isEmpty()){
            ag.setUrlInstrucciones   (
                    storageService.guardarInstruccionesActividadGrupo(ag.getIdActividadGrupo(), archivo, ag.getTitulo()));
        }
        ag = actividadGrupoRepository.save(ag);
        asignarActividad(ag, dto.idInscripciones());
        return actividadGrupoMapper.toDto(ag);
    }



    @Transactional(readOnly = true)
    @Override
    public List<ActividadGrupoDto> obtenerActividadesPorGrupo(Long idGrupo){

        if(!grupoRepository.existsById(idGrupo)){
            throw new NotFoundException("Grupo no encontrado");
        }

        return actividadGrupoRepository
                .findByGrupo_IdGrupo(idGrupo)
                .stream()
                .map(ag -> actividadGrupoMapper.toDto(ag))
                .toList();
    }
    @Transactional(readOnly = true)
    @Override
    public List<ActividadGrupoDto> obtenerActividadesGrupales(Long idGrupo){
        if(!grupoRepository.existsById(idGrupo)){
            throw new NotFoundException("Grupo no encontrado");
        }


        return actividadGrupoRepository
                .findByGrupo_IdGrupoAndAlcance(idGrupo, Alcance.GRUPAL)
                .stream()
                .map(actividadGrupoMapper::toDto)
                .toList();
    }
    @Transactional(readOnly = true)
    @Override
    public List<ActividadGrupoDto> obtenerActividadesIndividuales(Long idGrupo){
        if(!grupoRepository.existsById(idGrupo)){
            throw new NotFoundException("Grupo no encontrado");
        }
        return actividadGrupoRepository
                .findByGrupo_IdGrupoAndAlcance(idGrupo, Alcance.INDIVIDUAL)
                .stream()
                .map(actividadGrupoMapper::toDto)
                .toList();
    }
    @Override
    public ActividadGrupoDashboardDto obtenerActividadPorId(Long idActividad) {
        return actividadGrupoRepository
                .obtenerPorId(idActividad).orElseThrow( () -> new NotFoundException("Actividad no encontrada "));
    }
    @Override
    public ActividadGrupoDto actualizarInstruccionesPorId (Long idActividadGrupo, MultipartFile archivo){
        ActividadGrupo ag = actividadGrupoRepository.findById(idActividadGrupo)
                .orElseThrow(()->new NotFoundException("Actividad no encontrada"));
        if(archivo != null && !archivo.isEmpty()){
            ag.setUrlInstrucciones(storageService.guardarInstruccionesActividadGrupo(idActividadGrupo,archivo,ag.getTitulo()));
        }
        return actividadGrupoMapper.toDto(actividadGrupoRepository.save(ag));
    }

    @Transactional
    public void agregarInscripcionesActividad(Long idActividadGrupo, List<Long> nuevasInscripciones){

        ActividadGrupo ag = actividadGrupoRepository.findById(idActividadGrupo)
                .orElseThrow(() -> new NotFoundException("Actividad no encontrada"));

        if(ag.getAlcance() != Alcance.INDIVIDUAL){
            throw new IllegalArgumentException("Solo aplica para actividades individuales");
        }


        Set<Long> actualesIds = actividadAlumnoRepository
                .findByActividadGrupo_IdActividadGrupo(idActividadGrupo)
                .stream()
                .map(a -> a.getInscripcion().getIdInscripcion())
                .collect(Collectors.toSet());

        for (Long idInscripcion : nuevasInscripciones) {
            if(actualesIds.contains(idInscripcion)){
                continue;
            }
            Inscripcion inscripcion = inscripcionRepository.findById(idInscripcion)
                    .orElseThrow(() -> new NotFoundException("Inscripción no encontrada"));

            if(!inscripcion.getGrupo().getIdGrupo().equals(ag.getGrupo().getIdGrupo())){
                throw new IllegalArgumentException("La inscripción no pertenece al grupo");
            }

            ActividadAlumno aa = new ActividadAlumno();
            aa.setActividadGrupo(ag);
            aa.setInscripcion(inscripcion);
            aa.setEstadoTarea(EstadoTarea.Sin_Iniciar);
            aa.setExcento(false);

            actividadAlumnoRepository.save(aa);
        }
    }

    @Override
    public Resource visualizarInstrucciones(Long idActividadGrupo) {
        ActividadGrupo ag = actividadGrupoRepository.findById(idActividadGrupo)
                    .orElseThrow(() -> new NotFoundException("Actividad del grupo no encontrada"));
            if (ag.getUrlInstrucciones() == null || ag.getUrlInstrucciones().isBlank()) {
                throw new NotFoundException("La actividad aún no tiene instrucciones");
            }
            return storageService.loadAsResource(ag.getUrlInstrucciones());
    }

    private void asignarAGrupo(ActividadGrupo actividadGrupo, Long idGrupo) {

        List<Inscripcion> inscripciones = inscripcionRepository
                .findByGrupo_IdGrupoAndEstadoTrue(idGrupo)
                .stream()
                .toList();
        for (Inscripcion inscripcion : inscripciones) {
            ActividadAlumno aa = new ActividadAlumno();
            aa.setEstadoTarea(EstadoTarea.Sin_Iniciar);
            aa.setExcento(false);
            aa.setMotivoExencion(null);
            aa.setUrlEntrega(null);
            aa.setObservaciones(null);
            aa.setInscripcion(inscripcion);
            aa.setActividadGrupo(actividadGrupo);
            actividadAlumnoRepository.save(aa);
        }
    }
    private void asignarActividad(ActividadGrupo ag, List<Long> ids) {

        List <Inscripcion> inscripciones = inscripcionRepository
                .findByGrupo_IdGrupoAndEstadoTrue(ag.getGrupo().getIdGrupo());

        if(ag.getAlcance()==Alcance.INDIVIDUAL){
            if ( ids == null || ids.isEmpty()){
                throw new IllegalArgumentException("Debe enviar alumnos para una actividad Individual");
            }
            Set<Long> idsGrupo = inscripciones.stream()
                    .map(Inscripcion::getIdInscripcion)
                    .collect(Collectors.toSet());
            if (!idsGrupo.containsAll(ids)) {
                throw new IllegalArgumentException("Hay inscripciones que no pertenecen al grupo");
            }
            inscripciones  = inscripciones
                    .stream()
                    .filter(i-> ids.contains(i.getIdInscripcion()))
                    .toList();
        }

        for (Inscripcion i : inscripciones) {
            ActividadAlumno aa = new ActividadAlumno();
            aa.setEstadoTarea(EstadoTarea.Sin_Iniciar);
            aa.setExcento(false);
            aa.setMotivoExencion(null);
            aa.setUrlEntrega(null);
            aa.setObservaciones(null);
            aa.setInscripcion(i);
            aa.setActividadGrupo(ag);
            actividadAlumnoRepository.save(aa);
        }
    }
}



