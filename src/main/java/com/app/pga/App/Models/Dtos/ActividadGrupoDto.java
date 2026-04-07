package com.app.pga.App.Models.Dtos;

import com.app.pga.App.Models.Enum.Alcance;
import com.app.pga.App.Models.Enum.Origen;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link com.app.pga.App.Models.Entities.ActividadGrupo}
 */
public record ActividadGrupoDto(

        Long idActividadGrupo,
        @NotBlank
        String titulo,
        String descripcion,

        @PastOrPresent
        @NotNull
        LocalDate fechaAsignacion,
        Boolean reqEntrega,

        Alcance alcance,
        Long  idGrupo,
        String nombreGrupo,
        String campo,
        Origen origen

) implements Serializable {
}