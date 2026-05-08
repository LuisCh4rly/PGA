package com.app.pga.App.Models.Dtos.ResponseDto;

import com.app.pga.App.Models.Entities.ActividadAlumno;
import com.app.pga.App.Models.Entities.SeguimentoSemanal;
import com.app.pga.App.Models.Enum.EstadoTarea;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.time.LocalDate;

public record DetalleDashboardDto(
        Long idDetalleSeguimiento,
        EstadoTarea estadoSemana,

        Long avanceSemanal,

        String observacionesAlumno,
        String tituloActividad,
        Long avanceGlobalActividad,

        Long idActividadAlumno,
        Boolean requiereEntrega
) {
}
