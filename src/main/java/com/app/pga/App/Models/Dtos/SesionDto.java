package com.app.pga.App.Models.Dtos;

import com.app.pga.App.Models.Entities.SesionAlumno;
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
public record SesionDto(//Response

        Long idSesion,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
        LocalDateTime fecha,
        String tema,
        String urlSesion,
        Alcance alcance,
        String plataforma,
        GrupoDto grupo)
        implements Serializable {
}