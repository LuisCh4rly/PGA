package com.app.pga.App.Models.Dtos;

import com.app.pga.App.Models.Enum.Alcance;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link com.app.pga.App.Models.Entities.Sesion}
 */
public record SesionDetalletDto(
        Long idSesion,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
        LocalDateTime fecha,
        String tema,
        Alcance alcance,
        String plataforma,
        GrupoDto grupo,
        List<SesionAlumnoDetalleDto>alumnos)
        implements Serializable {
}