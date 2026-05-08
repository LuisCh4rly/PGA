package com.app.pga.App.Models.Dtos.RequestDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;

public record SemanaRequestDto(
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate fechaInicio,

        @JsonFormat(pattern = "dd/MM/yyyy")

        Long idInscripcion
) {
}
