package com.app.pga.App.Models.Dtos.RequestDto;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record CursoRequestDto(

        @Size(message = "Máximo 60 carácteres", max = 60)
        @NotBlank(message = "Campo obligatorio")
        String nombre,
        @Size(message = "Máximo 120 carácteres", max = 120)
        @NotBlank String descripcion,

        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate fechaAlta,

        @JsonFormat (pattern = "dd/MM/yyyy")
        LocalDate fechaBaja,

        Boolean activo,

        List<Long> idActividadesBase
) {
}
