package com.app.pga.App.Services.Implements;

import com.app.pga.App.Exception.NotFoundException;
import com.app.pga.App.Exception.ResourceDisabledException;
import com.app.pga.App.Models.Dtos.ActividadAlumnoDto;
import com.app.pga.App.Models.Dtos.ReporteSeguimientoDto;
import com.app.pga.App.Models.Dtos.RequestDto.CambiarEstadoTareaDto;
import com.app.pga.App.Models.Dtos.ResponseDto.ActividadAlumnoResponseDTO;
import com.app.pga.App.Models.Entities.ActividadAlumno;
import com.app.pga.App.Models.Entities.ActividadGrupo;
import com.app.pga.App.Models.Entities.Grupo;
import com.app.pga.App.Models.Entities.Inscripcion;
import com.app.pga.App.Models.Enum.Alcance;
import com.app.pga.App.Models.Enum.Estado;
import com.app.pga.App.Models.Enum.EstadoTarea;
import com.app.pga.App.Models.Mappers.ActividadAlumnoMapper;
import com.app.pga.App.Repositories.IActividadAlumnoRepository;
import com.app.pga.App.Repositories.IActividadGrupoRepository;
import com.app.pga.App.Repositories.IInscripcionRepository;
import com.app.pga.App.Services.Interfaces.IActividadAlumnoService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ActividadAlumnoService implements IActividadAlumnoService {
    private final IActividadAlumnoRepository actividadAlumnoRepository;
    private final IInscripcionRepository inscripcionRepository;
    private final ActividadAlumnoMapper actividadAlumnoMapper;
    private final StorageService storageService;
    private final IActividadGrupoRepository actividadGrupoRepository;


    @Override
    public void entregarActividad( Long idActividadAlumno, MultipartFile archivo) {

        ActividadAlumno aa = actividadAlumnoRepository.findById(idActividadAlumno)
                .orElseThrow(() -> new NotFoundException("Actividad alumno no encontrada"));

        Inscripcion inscripcion = aa.getInscripcion();

        if (!inscripcion.getEstado())
            throw new ResourceDisabledException("Inscripción deshabilitada");

        if (!aa.getActividadGrupo().getReqEntrega())
            throw new IllegalStateException("Actividad no requiere entrega");

        if (aa.getExcento()==true)
            throw new IllegalStateException("Alumno exento");

        if (aa.getEstadoTarea() == EstadoTarea.Aprobada)
            throw new IllegalStateException("Tarea ya aprobada");

        String url = storageService.guardarEntrega(inscripcion.getUsuario().getIdUsuario(), inscripcion.getGrupo().getIdGrupo(), aa.getActividadGrupo().getIdActividadGrupo(), archivo);

        aa.setFechaEntrega(LocalDate.now());
        aa.setUrlEntrega(url);
        aa.setEstadoTarea(EstadoTarea.Completada);

        actividadAlumnoRepository.save(aa);
    }
    @Override
    @Transactional(readOnly = true)
    public List<ActividadAlumnoDto> obtenerPorInscripcion(Long idInscripcion) {

        Inscripcion inscripcion = inscripcionRepository.findById(idInscripcion)
                .orElseThrow(() -> new NotFoundException("Inscripción no encontrada"));
        return actividadAlumnoRepository
                .findByInscripcion_IdInscripcion(idInscripcion)
                .stream()
                .map(aa-> actividadAlumnoMapper.toDto(aa))
                .collect(Collectors.toList());
    }
    @Override
    public void agregarObservacion(Long idActividadAlumno, CambiarEstadoTareaDto cambiarEstadoTareaDto) {

        ActividadAlumno aa = actividadAlumnoRepository.findById(idActividadAlumno)
                .orElseThrow(() -> new NotFoundException("Actividad alumno no encontrada"));

        if (aa.getUrlEntrega()== null)
            throw new NotFoundException("No hay entrega del alumno");

        if (aa.getExcento() == true)
            throw new ResourceDisabledException("Actividad Excenta");

        aa.setEstadoTarea(cambiarEstadoTareaDto.estado());
        aa.setObservaciones(cambiarEstadoTareaDto.mensaje());
        actividadAlumnoRepository.save(aa);
    }
    @Override
    public void exentarActividad( Long idActividadAlumno, CambiarEstadoTareaDto cambiarEstadoTareaDto) {

        ActividadAlumno aa = actividadAlumnoRepository.findById(idActividadAlumno)
                .orElseThrow(() -> new NotFoundException("Actividad alumno no encontrada"));

        if (aa.getUrlEntrega() != null)
            throw new ResourceDisabledException("Exite una entrega del alumno");
        aa.setExcento(true);
        aa.setMotivoExencion(cambiarEstadoTareaDto.mensaje());
        aa.setEstadoTarea(EstadoTarea.Exenta);
        actividadAlumnoRepository.save(aa);
    }
    @Override
    public void cambiarEstado( Long idActividadAlumno, CambiarEstadoTareaDto cambiarEstadoTareaDto) {

        ActividadAlumno aa = actividadAlumnoRepository.findById(idActividadAlumno)
                .orElseThrow(() -> new NotFoundException("Actividad alumno no encontrada"));

        if (aa.getExcento() == true)
            throw new ResourceDisabledException("Actividad Excenta");

        if (aa.getActividadGrupo().getReqEntrega() && cambiarEstadoTareaDto.estado() == EstadoTarea.Completada && aa.getUrlEntrega()==null)
            throw new ResourceDisabledException("Primero debe asignar una entrega");

        if (aa.getEstadoTarea() == EstadoTarea.Aprobada)
            throw new ResourceDisabledException("Tarea ya aprobada");

        aa.setEstadoTarea(cambiarEstadoTareaDto.estado());
        aa.setComentarios(cambiarEstadoTareaDto.mensaje());
        actividadAlumnoRepository.save(aa);
    }

    @Override
    public void asignarActividadesGrupalesPorInscripcion(Long idInscripcion) {

        Inscripcion inscripcion = inscripcionRepository.findById(idInscripcion)
                .orElseThrow(() -> new NotFoundException("Inscripción no encontrada"));

        if (!inscripcion.getEstado()) {
            throw new ResourceDisabledException("La inscripción está Deshabilitada");
        }
        Grupo grupo = inscripcion.getGrupo();
        if (grupo == null || grupo.getEstado() == Estado.DESHABILITADO) {
            throw new ResourceDisabledException("Grupo inválido o deshabilitado");
        }
        List<ActividadGrupo> actividadesGrupales = actividadGrupoRepository.findByGrupo_IdGrupoAndAlcance( grupo.getIdGrupo(), Alcance.GRUPAL);
        if (actividadesGrupales.isEmpty()) return;

        List<ActividadAlumno> nuevas = new ArrayList<>();

        for (ActividadGrupo ag : actividadesGrupales) {
            boolean yaExiste = actividadAlumnoRepository .existsByActividadGrupo_IdActividadGrupoAndInscripcion_IdInscripcion( ag.getIdActividadGrupo(), idInscripcion );
            if (yaExiste) continue;
            ActividadAlumno aa = new ActividadAlumno();
            aa.setActividadGrupo(ag);
            aa.setInscripcion(inscripcion);
            aa.setEstadoTarea(EstadoTarea.Sin_Iniciar);
            aa.setExcento(false);
            nuevas.add(aa);
        }
        actividadAlumnoRepository.saveAll(nuevas);
    }

    @Transactional(readOnly = true)
    public Resource visualizarEntrega(Long idActividadAlumno) {

        ActividadAlumno aa = actividadAlumnoRepository.findById(idActividadAlumno)
                .orElseThrow(() -> new NotFoundException("Actividad del alumno no encontrada"));
        if (!aa.getActividadGrupo().getReqEntrega()) {
            throw new ResourceDisabledException("La actividad  no requiere entrega");
        }
        if (aa.getUrlEntrega() == null || aa.getUrlEntrega().isBlank()) {
            throw new NotFoundException("La actividad aún no tiene entrega");
        }


        return storageService.loadAsResource(aa.getUrlEntrega());
    }

    @Transactional(readOnly = true)
    public List<ReporteSeguimientoDto>obtenerReporteSeguimientoGrupo(){
        return actividadAlumnoRepository.reporteSeguimientoGeneral();
    }

    public void eliminarActividadesPorInscripcion(Long idInscripcion) {
        actividadAlumnoRepository.deleteByInscripcion_IdInscripcion(idInscripcion);
    }

    @Override
    public List<ActividadAlumnoResponseDTO> verAsignaciones(Long idActividadGrupo) {

        return actividadAlumnoRepository
                .findByActividadGrupo_IdActividadGrupo(idActividadGrupo)
                .stream()
                .map(aa-> actividadAlumnoMapper.toDtoResponse(aa))
                .collect(Collectors.toList());
    }
}


