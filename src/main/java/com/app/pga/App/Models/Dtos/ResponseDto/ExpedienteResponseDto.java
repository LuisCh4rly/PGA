package com.app.pga.App.Models.Dtos.ResponseDto;

import com.app.pga.App.Models.Enum.EstadoExpediente;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO for {@link com.app.pga.App.Models.Entities.Expediente}
 */
public record ExpedienteResponseDto(
        Long idExpediente,
        Long idAlumno,
        EstadoExpediente estado,
        String observaciones,
        List <DocumentoExpedienteResponseDto> documentos

) implements Serializable {
}