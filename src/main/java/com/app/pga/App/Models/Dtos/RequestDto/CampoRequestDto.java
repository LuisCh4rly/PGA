package com.app.pga.App.Models.Dtos.RequestDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CampoRequestDto(

        @Size(message = "Máximo 30 carácteres", max = 30)
        @NotBlank(message = "Campo obligatorio")
        String nombre,

        @Size(message = "Máximo 60 carácteres", max = 60)
        String descripcion

) {
}
