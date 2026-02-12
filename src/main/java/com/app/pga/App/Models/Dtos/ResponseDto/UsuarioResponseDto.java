package com.app.pga.App.Models.Dtos.ResponseDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.LocalDate;

public record UsuarioResponseDto(
        Long idUser,
        @Size(message = "Máximo 30 carácteres", max = 30)
        @NotBlank(message = "Campo obligatorio")
        String nombre,
        @Size(message = "Máximo 30 carácteres", max = 30)
        @NotBlank(message = "Campo obligatorio")
        String apellidoPaterno,
        @Size(message = "Máximo 30 caráteres", max = 30)
        @NotBlank(message = "Camppo obligatorio")
        String apellidoMaterno,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate created_At,
        @Email(message = "Formato inválido", regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
        @NotBlank(message = "Campo obligatorio")
        String email,
        @Pattern(message = "Debe tener exactamente 10 dígitos", regexp = "^\\d{10}$")
        @NotBlank(message = "Campo Obligatorio")
        String telefono,
        @Size(message = "Máximo 60 carácteres", max = 60)
        @NotBlank(message = "Campo obligatorio")
        String direccion,
        Boolean activo

) implements Serializable {
}
