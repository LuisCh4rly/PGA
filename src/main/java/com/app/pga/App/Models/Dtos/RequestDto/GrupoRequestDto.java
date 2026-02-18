package com.app.pga.App.Models.Dtos.RequestDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public record GrupoRequestDto(

        @Size(message = "Máximo 60 carácteres", max = 60)
        @NotBlank(message = "Campo obligatorio")
        String nombre,
        @NotBlank(message = "Campo obligatorio")
        String periodo,
        @NotNull(message = "Campo obligatorio")
        Long idCurso,
        @NotNull(message = "Campo obligatorio")
        Long idDocente

)  implements Serializable {
}