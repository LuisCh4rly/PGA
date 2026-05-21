package com.app.pga.App.Services.Implements;

import com.app.pga.App.Exception.BadRequestException;
import com.app.pga.App.Exception.DuplicateResourceException;
import com.app.pga.App.Exception.NotFoundException;
import com.app.pga.App.Exception.ResourceDisabledException;
import com.app.pga.App.Models.Dtos.ActividadAlumnoDto;
import com.app.pga.App.Models.Dtos.RequestDto.DetalleSeguimientoAgrupadoDto;
import com.app.pga.App.Models.Dtos.RequestDto.DetalleSeguimientoRequestDto;
import com.app.pga.App.Models.Dtos.ResponseDto.DetalleDashboardDto;
import com.app.pga.App.Models.Entities.ActividadAlumno;
import com.app.pga.App.Models.Entities.DetalleSeguimiento;
import com.app.pga.App.Models.Entities.SeguimentoSemanal;
import com.app.pga.App.Models.Enum.EstadoTarea;
import com.app.pga.App.Models.Mappers.DetalleSeguimientoMapper;
import com.app.pga.App.Repositories.IActividadAlumnoRepository;
import com.app.pga.App.Repositories.IDetalleSeguimientoRepository;
import com.app.pga.App.Repositories.ISeguimentoSemanalRepository;
import com.app.pga.App.Services.Interfaces.IDetalleSeguimientoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
class DetalleSeguimientoService implements IDetalleSeguimientoService {
    private final ISeguimentoSemanalRepository seguimentoSemanalRepository;
    private final IDetalleSeguimientoRepository detalleSeguimientoRepository;
    private final IActividadAlumnoRepository actividadAlumnoRepository;
    private final ActividadAlumnoService actividadAlumnoService;
    private final DetalleSeguimientoMapper detalleSeguimientoMapper;
    private final  SeguimientoSemanalService seguimientoSemanalService;

    @Override

    public List<DetalleDashboardDto> crearDetalleSeguimiento(DetalleSeguimientoAgrupadoDto dto) {
        LocalDate hoy = LocalDate.now();
        SeguimentoSemanal semana = seguimentoSemanalRepository.findById(dto.idSemana())
            .orElseThrow(()-> new NotFoundException("Semana no encontrada"));


        if(hoy.isAfter(semana.getFechaLimiteEdicion())){
        throw new ResourceDisabledException("Semana deshabilitada para actualizacion");
        }
        List<DetalleSeguimiento> detalles = new ArrayList<>();
        dto.actividades().forEach(a-> {
                DetalleSeguimiento detalleSeguimiento = new DetalleSeguimiento();
                ActividadAlumno aa = actividadAlumnoRepository.findById(a.idActividad()).
                        orElseThrow(()->new NotFoundException("Actividad no encontrada"));
                if (!aa.getInscripcion().getIdInscripcion()
                        .equals(semana.getInscripcion().getIdInscripcion())) {
                    throw new BadRequestException("Actividad no pertenece a la inscripción");
                }
                boolean existe = detalleSeguimientoRepository
                        .existsBySemana_IdSeguimientoSemanalAndActividad_IdActividadAlumno(
                                semana.getIdSeguimientoSemanal(),
                                aa.getIdActividadAlumno()
                        );

                if (existe) {
                    throw new DuplicateResourceException("Actividad ya registrada en la semana");
                }

                if (aa.getExcento()) {
                    throw new ResourceDisabledException("Actividad exenta");
                }

                if (aa.getAvanceGlobal() >= 100) {
                    throw new ResourceDisabledException("Actividad ya completada");
                }
                    Long avanceActual = Optional.ofNullable(aa.getAvanceGlobal()).orElse(0L);
                    Long limite =  100L;
                    Long restante = limite - avanceActual;

                detalleSeguimiento.setActividad(aa);
                detalleSeguimiento.setAvanceReal(0L);
                detalleSeguimiento.setAvanceEsperado(restante);
                detalleSeguimiento.setEstadoSemana(a.estadoSemana());
                detalleSeguimiento.setObservacionesAlumno("");
                detalleSeguimiento.setSemana(semana);

                detalles.add(detalleSeguimiento);
            }
            );

        List<DetalleDashboardDto> dnuevos = detalleSeguimientoRepository.saveAll(detalles).stream()
            .map(d-> detalleSeguimientoMapper.toDashboardDto(d))
            .toList();
        seguimientoSemanalService.recalcularAvanceSemana(semana);
        return dnuevos;

    }


    @Override
    public void agregarActividadesPendientes( Long idSemanaActual, Long idInscripcion) {
        SeguimentoSemanal semanaActual = seguimentoSemanalRepository.findById(idSemanaActual)
                .orElseThrow(()->new NotFoundException("Semana no encontrada"));

        if (!semanaActual.getInscripcion().getIdInscripcion().equals(idInscripcion)) {
            throw new IllegalStateException("La semana no pertenece a la inscripción");
        }
        if (LocalDate.now().isAfter(semanaActual.getSemanaFin())) {
            throw new IllegalStateException("Semana cerrada");
        }

        SeguimentoSemanal semanaAnterior =  seguimentoSemanalRepository
                .findTopByInscripcion_IdInscripcionAndNumeroSemanaLessThanOrderByNumeroSemanaDesc(
                        idInscripcion, semanaActual.getNumeroSemana());

        List<DetalleSeguimiento> sugeridos = new ArrayList<>();

        if (semanaAnterior != null) {
            sugeridos = detalleSeguimientoRepository
                    .pendientes( idInscripcion, semanaAnterior.getIdSeguimientoSemanal());
        }
        List<DetalleSeguimiento> detalles = new ArrayList<>();
        for(DetalleSeguimiento d : sugeridos){
                ActividadAlumno aa = d.getActividad();
                if (aa.getExcento() || aa.getAvanceGlobal() >= 100) {
                    continue;
                }
                boolean existe = detalleSeguimientoRepository
                    .existsBySemana_IdSeguimientoSemanalAndActividad_IdActividadAlumno(
                            semanaActual.getIdSeguimientoSemanal(),
                            aa.getIdActividadAlumno()
                    );
                if (existe) continue;
                Long avanceActual = Optional.ofNullable(aa.getAvanceGlobal()).orElse(0L);
                Long limite = aa.getActividadGrupo().getReqEntrega()? 99L : 100L;
                Long restante = limite - avanceActual;

                DetalleSeguimiento detalleSeguimiento = new DetalleSeguimiento();
                detalleSeguimiento.setActividad(aa);
                detalleSeguimiento.setAvanceReal(0L);
                detalleSeguimiento.setAvanceEsperado(restante);
                detalleSeguimiento.setEstadoSemana(EstadoTarea.En_Espera);
                detalleSeguimiento.setObservacionesAlumno("");
                detalleSeguimiento.setSemana(semanaActual);
                detalles.add(detalleSeguimiento);
        }
        detalleSeguimientoRepository.saveAll(detalles);
        seguimientoSemanalService.recalcularAvanceSemana(semanaActual);

    }
    @Override
    public DetalleDashboardDto actualizarEstado(Long idDetalleSeguimeinto, DetalleSeguimientoRequestDto dto) {

        LocalDate hoy = LocalDate.now();

        DetalleSeguimiento detalle = detalleSeguimientoRepository.findById(idDetalleSeguimeinto)
                .orElseThrow(()-> new NotFoundException("Detalle no encontrado"));


        if(hoy.isAfter(detalle.getSemana().getFechaLimiteEdicion())){
            throw new ResourceDisabledException("Semana cerrada");
        }

        ActividadAlumno actividadAlumno = detalle.getActividad();

        if (actividadAlumno.getEstadoTarea() == EstadoTarea.Completada
                || actividadAlumno.getEstadoTarea() == EstadoTarea.Aprobada
                || actividadAlumno.getEstadoTarea() == EstadoTarea.Exenta){
            throw new ResourceDisabledException("Actividad no editable");
        }
        List<EstadoTarea> estadosPermitidos = List.of(
                EstadoTarea.Sin_Iniciar,
                EstadoTarea.En_Progreso,
                EstadoTarea.En_Espera
        );

        if (!estadosPermitidos.contains(dto.estadoSemana())) {
            throw new BadRequestException(
                    "Estado no permitido"
            );
        }
        Long avanceGlobalActual = Optional.ofNullable(actividadAlumno.getAvanceGlobal()).orElse(0L);
        Long esperado = detalle.getAvanceEsperado();
        Long realActual = Optional.ofNullable(detalle.getAvanceReal()).orElse(0L);
        Long restanteSemana = esperado - realActual;

        if(dto.avanceReal() < 0){
            throw new BadRequestException("El avance no puede ser negativo");
        }


        if(dto.avanceReal() > restanteSemana ){
            throw new BadRequestException ("El avance ingresado supera el permitido");
        }
        actividadAlumno.setAvanceGlobal(avanceGlobalActual + dto.avanceReal());
        Long avanceSemanal = Optional.ofNullable(detalle.getAvanceReal()).orElse(0L);
        detalle.setAvanceReal(avanceSemanal + dto.avanceReal());

        detalle.setEstadoSemana(dto.estadoSemana());
        actividadAlumno.setEstadoTarea(dto.estadoSemana());

        detalle.setObservacionesAlumno(dto.observacionesAlumno());
        actividadAlumno.setComentarios(dto.observacionesAlumno());

        if (!actividadAlumno.getActividadGrupo().getReqEntrega() && actividadAlumno.getAvanceGlobal() == 100) {
            actividadAlumno.setEstadoTarea( EstadoTarea.Completada);
            detalle.setEstadoSemana(EstadoTarea.Completada);
        }

        actividadAlumnoRepository.save(actividadAlumno);
        DetalleSeguimiento detalleSeguimiento = detalleSeguimientoRepository.save(detalle);
        seguimientoSemanalService.recalcularAvanceSemana(detalle.getSemana());

        return detalleSeguimientoMapper.toDashboardDto(detalleSeguimiento);

    }


}
