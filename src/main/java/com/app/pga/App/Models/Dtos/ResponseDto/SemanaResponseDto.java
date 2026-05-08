package com.app.pga.App.Models.Dtos.ResponseDto;

import com.app.pga.App.Models.Dtos.ActividadAlumnoDto;
import com.app.pga.App.Models.Entities.Inscripcion;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.time.LocalDate;
import java.util.List;

public record SemanaResponseDto(

        Long idSeguimientoSemanal,
        Long numeroSemana,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate semanaInicio,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate semanaFin,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate fechaLimiteEdicion,
        @Min(value = 0, message = "El valor mínimo es 0")
        @Max(value = 100, message = "El valor máximo es 100")
        Long porcentajeAvance,
        Long idInscripción,

        List<ActividadAlumnoDto>  actividadesSugeridas

) {
}
