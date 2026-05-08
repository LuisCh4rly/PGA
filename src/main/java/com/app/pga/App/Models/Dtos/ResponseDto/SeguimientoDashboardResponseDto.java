package com.app.pga.App.Models.Dtos.ResponseDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.time.LocalDate;
import java.util.List;

public record SeguimientoDashboardResponseDto(
        Long idSeguimientoSemanal,
        Long numeroSemana,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate semanaInicio,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate semanaFin,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate fechaLimiteEdicion,

        Long porcentajeAvance,

        List<DetalleDashboardDto> detalles
) {


}
