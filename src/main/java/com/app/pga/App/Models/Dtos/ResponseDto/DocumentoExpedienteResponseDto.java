package com.app.pga.App.Models.Dtos.ResponseDto;

import com.app.pga.App.Models.Enum.EstadoDocumento;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link com.app.pga.App.Models.Entities.Documento_Expediente}
 */
public record DocumentoExpedienteResponseDto(

        Long idDocumentoExpediente,
        String tipoDocumento,
        EstadoDocumento estadoDocumento,
        String urlDocumento,
        LocalDate fechaCarga,
        String observacion,
        LocalDate fechaRevision

) implements Serializable {
}