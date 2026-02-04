package com.app.pga.App.Models.Dtos;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.app.pga.App.Models.Entities.Sesion}
 */
public record SesiondocenteDto(
        Long idSesion,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
        LocalDateTime fecha,
        String tema,
        String plataforma,
        String nombreGrupo)
        implements Serializable {
}