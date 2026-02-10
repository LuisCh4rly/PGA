package com.app.pga.App.Models.Dtos.ResponseDto;

import java.io.Serializable;

/**
 * DTO for {@link com.app.pga.App.Models.Entities.Documento}
 */
public record DocumentoResponseDto(
        Long idDocumento,
        String tipo,
        String nombre,
        Boolean obligatorio,
        Boolean activo
) implements Serializable {
}