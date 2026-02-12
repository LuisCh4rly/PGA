package com.app.pga.Auth.Models.Dtos.Request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record AuthRegisterDto(
        @NotBlank
        String nombre,
        @NotBlank
        String apellidoPaterno,
        @NotBlank
        String apellidoMaterno,
        @Email(message = "Formato inválido", regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
        @NotBlank(message = "Campo obligatorio")
        String email,
        @NotBlank(message = "Campo Obligatorio")
        String telefono,
        @Size(message = "Máximo 60 carácteres", max = 60)
        @NotBlank(message = "Campo obligatorio")
        String direccion,



        @Size(message = "Máximo 60 carácteres", max = 60)
        String universidad,
        @Size(message = "Máximo 60 carácteres.", max = 60)
        String carrera,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate fechaTermino,


        @NotBlank
        String password,
        @NotBlank(message = "Campo obligatorio")
        String rol

) {
}
