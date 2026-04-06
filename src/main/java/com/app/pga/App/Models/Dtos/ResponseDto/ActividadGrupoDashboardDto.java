package com.app.pga.App.Models.Dtos.ResponseDto;

import com.app.pga.App.Models.Enum.Alcance;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record ActividadGrupoDashboardDto(
        Long idActividadGrupo,
        String titulo,
        String descripcion,
        LocalDate fechaAsignacion,
        Boolean reqEntrega,
        Alcance alcance,
        Long  idGrupo,
        String nombreGrupo,
        String campo,

        Long asignadas,
        Long entregadas,
        Long aprobadas,
        Long incompletas,
        Long excentadas

) {
}
