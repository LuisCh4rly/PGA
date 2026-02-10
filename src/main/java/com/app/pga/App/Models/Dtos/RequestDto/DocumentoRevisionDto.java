package com.app.pga.App.Models.Dtos.RequestDto;

import com.app.pga.App.Models.Enum.EstadoDocumento;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * DTO for {@link com.app.pga.App.Models.Entities.Documento_Expediente}
 */
public record DocumentoRevisionDto(
        @NotNull(message = "Campo Obligatorio")
        EstadoDocumento estadoDocumento,
        String observaciones

) implements Serializable {
}