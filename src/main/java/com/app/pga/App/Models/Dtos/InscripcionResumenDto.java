package com.app.pga.App.Models.Dtos;

import com.app.pga.App.Models.Enum.tipoInscripcion;

import java.io.Serializable;

/**
 * DTO for {@link com.app.pga.App.Models.Entities.Inscripcion}
 */
public record InscripcionResumenDto(
        Long idInscripcion,
        tipoInscripcion tipo,
        Boolean estado,
        String nombreUsuario0
        )        implements Serializable {
}