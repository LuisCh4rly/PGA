package com.app.pga.App.Models.Dtos.ResponseDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public record ActividadBaseResponseDto(
        Long idActividad,
        @Size(message = "Máximo 30 carácteres", max = 30)
        @NotBlank(message = "Campo obligatorio")
        String titulo,
        @Size(message = "Máximo 60 carácteres", max = 60)
        @NotBlank(message = "Campo obligatorio")
        String descripcion,

        Boolean activo,

        CampoResponseDto campoFormativo,
        String urlInstrucciones

) implements Serializable{}
