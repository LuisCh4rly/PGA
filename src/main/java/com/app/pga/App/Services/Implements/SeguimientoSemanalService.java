package com.app.pga.App.Services.Implements;

import com.app.pga.App.Exception.NotFoundException;
import com.app.pga.App.Exception.ResourceDisabledException;
import com.app.pga.App.Models.Dtos.ActividadAlumnoDto;
import com.app.pga.App.Models.Dtos.RequestDto.SemanaRequestDto;
import com.app.pga.App.Models.Dtos.ResponseDto.DetalleDashboardDto;
import com.app.pga.App.Models.Dtos.ResponseDto.SeguimientoDashboardResponseDto;
import com.app.pga.App.Models.Dtos.ResponseDto.SemanaResponseDto;
import com.app.pga.App.Models.Entities.ActividadAlumno;
import com.app.pga.App.Models.Entities.DetalleSeguimiento;
import com.app.pga.App.Models.Entities.Inscripcion;
import com.app.pga.App.Models.Entities.SeguimentoSemanal;
import com.app.pga.App.Models.Mappers.ActividadAlumnoMapper;
import com.app.pga.App.Repositories.IDetalleSeguimientoRepository;
import com.app.pga.App.Repositories.IInscripcionRepository;
import com.app.pga.App.Repositories.ISeguimentoSemanalRepository;
import com.app.pga.App.Services.Interfaces.ISeguimientoSemanalService;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.DisabledException;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class SeguimientoSemanalService implements ISeguimientoSemanalService {
    private final IInscripcionRepository inscripcionRepository;
    private final ISeguimentoSemanalRepository seguimentoSemanalRepository;
    private final IDetalleSeguimientoRepository detalleSeguimientoRepository;
    private final ActividadAlumnoMapper actividadAlumnoMapper;


    @Override
    public SemanaResponseDto crearSeguimientoSemanal(Long idInscripcion) {

        Inscripcion inscripcion = inscripcionRepository.findById(idInscripcion)
                .orElseThrow(() -> new NotFoundException("Inscripción no encontrada"));
        if(!inscripcion.getEstado()) throw new ResourceDisabledException("Inscripción deshabilitada");

        LocalDate hoy = LocalDate.now();
        LocalDate inicioBase = inscripcion.getFechaInicio().with(DayOfWeek.MONDAY);
        LocalDate inicioInscripción = inscripcion.getFechaInicio();
        LocalDate finInscripcion = inscripcion.getFechaFin();

        LocalDate inicioSemana = hoy.with(DayOfWeek.MONDAY);
        LocalDate finSemana = inicioSemana.plusDays(4);
        LocalDate fechaLimiteEdicion= hoy.with(DayOfWeek.SUNDAY);


        if (inicioSemana.isAfter(hoy.with(DayOfWeek.MONDAY))) {
            throw new IllegalStateException("No se puede crear semanas futuras");
        }
        if (inicioSemana.isBefore(inicioInscripción.with(DayOfWeek.MONDAY))) {
            throw new IllegalStateException("Semana anterior a la inscripción");
        }
        if (inicioSemana.isAfter(finInscripcion)) {
            throw new IllegalStateException("Semana posterior al fin de la inscripción");
        }

        Long numeroSemana = ChronoUnit.WEEKS.between(inicioBase, inicioSemana) + 1;

        if (seguimentoSemanalRepository.existsByInscripcion_IdInscripcionAndNumeroSemana(inscripcion.getIdInscripcion(), numeroSemana)) {
            throw new DisabledException("La semana " + numeroSemana + " ya existe");
        }

        boolean traslape = seguimentoSemanalRepository
                .existsByInscripcionAndSemanaInicioLessThanEqualAndSemanaFinGreaterThanEqual(
                        inscripcion, finSemana, inicioSemana);

        if (traslape) {
            throw new IllegalStateException("Las fechas se traslapan");
        }

        SeguimentoSemanal semanaAnterior =  seguimentoSemanalRepository
                .findTopByInscripcion_IdInscripcionAndNumeroSemanaLessThanOrderByNumeroSemanaDesc(inscripcion.getIdInscripcion(), numeroSemana);

        List<ActividadAlumno> sugeridos = new ArrayList<>();
        if (semanaAnterior != null) {
            sugeridos = detalleSeguimientoRepository
                    .actividadesPendientes( inscripcion.getIdInscripcion(), semanaAnterior.getIdSeguimientoSemanal());
        }


        SeguimentoSemanal semana = new SeguimentoSemanal();
        semana.setInscripcion(inscripcion);
        semana.setSemanaInicio(inicioSemana);
        semana.setSemanaFin(finSemana);
        semana.setNumeroSemana(numeroSemana);
        semana.setFechaLimiteEdicion(fechaLimiteEdicion);

        semana = seguimentoSemanalRepository.save(semana);

        return new SemanaResponseDto(
                semana.getIdSeguimientoSemanal(),
                semana.getNumeroSemana(),
                semana.getSemanaInicio(),
                semana.getSemanaFin(),
                semana.getFechaLimiteEdicion(),
                semana.getPorcentajeAvance(),
                semana.getInscripcion().getIdInscripcion(),
                sugeridos.stream().map(actividadAlumnoMapper::toDto).collect(Collectors.toList()));
    }




    @Override
    public SeguimientoDashboardResponseDto obtenerSeguimientoSemanalActual( Long idInscripcion) {
        Optional<SeguimientoDashboardResponseDto> semanaOptional = seguimentoSemanalRepository
                .obtenerSemanaActual(idInscripcion);
        if (semanaOptional.isEmpty()) {
            return null;
        }
        SeguimientoDashboardResponseDto semanaBase = semanaOptional.get();

        List<Long> idSemana = List.of(semanaBase.idSeguimientoSemanal());

        List<DetalleDashboardDto> detalles= detalleSeguimientoRepository.obtenerDetalles(idSemana);

        return new SeguimientoDashboardResponseDto(
                semanaBase.idSeguimientoSemanal(),
                semanaBase.numeroSemana(),
                semanaBase.semanaInicio(),
                semanaBase.semanaFin(),
                semanaBase.fechaLimiteEdicion(),
                semanaBase.porcentajeAvance(),
                detalles
        );

    }

    @Override
    public List<SeguimientoDashboardResponseDto> obtenerSeguimientoAlumno(Long idInscripcion) {
    List<SeguimientoDashboardResponseDto> semanas = seguimentoSemanalRepository
                .findSemanas(idInscripcion);
    if (semanas.isEmpty()) {
        List.of();
    }
    List<Long> idSemanas = new ArrayList<>();
    semanas.forEach(s-> idSemanas.add(s.idSeguimientoSemanal()));
    List<DetalleDashboardDto> detalles = detalleSeguimientoRepository.obtenerDetalles(idSemanas);

    Map<Long, List<DetalleDashboardDto>> detallesPorSemana =
            detalles.stream().collect(Collectors.groupingBy(d-> d.idSeguimientoSemanal()));

     return semanas.stream()
            .map (s ->
                new SeguimientoDashboardResponseDto(
                        s.idSeguimientoSemanal(),
                        s.numeroSemana(),
                        s.semanaInicio(),
                        s.semanaFin(),
                        s.fechaLimiteEdicion(),
                        s.porcentajeAvance(),
                        detallesPorSemana.getOrDefault(s.idSeguimientoSemanal(), List.of())
                )

            ).toList();

    }

    public void recalcularAvanceSemana(SeguimentoSemanal semana) {

        List<DetalleSeguimiento> detalles = detalleSeguimientoRepository.findBySemana_IdSeguimientoSemanal(semana.getIdSeguimientoSemanal());
        if(detalles.isEmpty()) {
            semana.setPorcentajeAvance(0L);
            seguimentoSemanalRepository.save(semana);
            return;
        }
        Long sumaEsperada = detalles.stream()
                .mapToLong(d -> d.getAvanceEsperado())
                .sum();
        Long sumaReal = detalles.stream()
                .mapToLong(d-> d.getAvanceReal())
                .sum();

        Long porcentaje = sumaEsperada == 0 ? 100L : (sumaReal * 100) / sumaEsperada;

        semana.setPorcentajeAvance(
                Math.min(porcentaje, 100L)
        );

        seguimentoSemanalRepository.save(semana);
    }

}

