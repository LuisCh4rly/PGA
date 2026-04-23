package com.app.pga.App.Models.Dtos.ResponseDto;

import com.app.pga.App.Models.Enum.EstadoExpediente;

import java.io.Serializable;

public record ExpedienteReporteDto (
        String alumno,
        EstadoExpediente estadoExpediente,
        Boolean estadoAlumno,
        String Observaciones
) implements Serializable {

}
