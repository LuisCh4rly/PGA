package com.app.pga.App.Models.Dtos.RequestDto;

import com.app.pga.App.Models.Enum.Alcance;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link com.app.pga.App.Models.Entities.Sesion}
 */

public record SesionRequestDto (//creacion
        @NotNull(message = "Campo Obligatorio")
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
        LocalDateTime fecha,
        @Size(message = "Máximo 120 carácteres.", max = 120)
        @NotBlank(message = "Campo Obligatorio")
        String tema,
        @Pattern(message = "El link debe ser una URL válida ", regexp = "^(http|https)://.*$")
        @NotBlank(message = "Campo Obligatorio")
        String urlSesion,
        @NotNull(message = "Campo Obligatorio")
        Alcance alcance,
        @NotBlank(message = "Campo Obligatorio")
        String plataforma,
        @NotNull(message = "Campo Obligatorio")
        Long idGrupo,
        //individual
        List<Long> idsInscripcion)
        implements Serializable{ }