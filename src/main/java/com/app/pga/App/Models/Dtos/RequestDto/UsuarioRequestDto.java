package com.app.pga.App.Models.Dtos.RequestDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * DTO for {@link com.app.pga.App.Models.Entities.Usuario}
 */
public record UsuarioRequestDto(

        String nombre,
        String apellidoPaterno,
        String apellidoMaterno,
        @Pattern(message = "Debe tener exactamente 10 dígitos", regexp = "^\\d{10}$")
        @NotBlank(message = "Campo Obligatorio")
        String telefono,
        @Size(message = "Máximo 60 carácteres", max = 60)
        @NotBlank(message = "Campo obligatorio")
        String direccion

) implements Serializable {
}