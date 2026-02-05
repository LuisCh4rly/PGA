package com.app.pga.App.Models.Dtos.ResponseDto;

import com.app.pga.App.Models.Dtos.GrupoDto;
import com.app.pga.App.Models.Enum.Alcance;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

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