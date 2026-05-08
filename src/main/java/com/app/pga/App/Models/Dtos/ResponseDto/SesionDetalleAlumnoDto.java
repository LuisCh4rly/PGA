package com.app.pga.App.Models.Dtos.ResponseDto;

import com.app.pga.App.Models.Enum.Alcance;
import com.app.pga.App.Models.Enum.EstadoAsistencia;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

public record SesionDetalleAlumnoDto(
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
        LocalDateTime fecha,
        String tema,
        Alcance alcance,
        String url,
        EstadoAsistencia estadoAsistencia
) implements Serializable {
}
