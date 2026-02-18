package com.app.pga.App.Models.Dtos.RequestDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public record ActividadBaseRequestDto(

        @Size(message = "Máximo 30 carácteres", max = 30)
        @NotBlank(message = "Campo obligatorio")
        String titulo,
        @Size(message = "Máximo 60 carácteres", max = 60)
        @NotBlank(message = "Campo obligatorio")
        String descripcion,
        @NotNull (message = "Campo obligatorio")
        Long  idCampoFormativo

) implements Serializable {
}
