package com.app.pga.Auth.Models.Dtos.Request;

import com.app.pga.Auth.Models.Entities.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * DTO for {@link com.app.pga.Auth.Models.Entities.Cuenta}
 */
public record CuentaRequestDto(

        @NotBlank(message = "Campo obligatorio.")
        String password,

        @NotBlank(message = "Campo obligatorio.")
        Long idUsuario,

        @NotNull(message = "Campo obligatorio.")
        Role role,

        @Email(message = "Formato inválido", regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
        @NotBlank(message = "Campo obligatorio")
        String email

) implements Serializable {
}