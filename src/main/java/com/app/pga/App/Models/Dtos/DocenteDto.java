package com.app.pga.App.Models.Dtos;

import com.app.pga.App.Models.Entities.Docente;
import com.app.pga.App.Models.Entities.Usuario;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for {@link Docente}
 */
public record DocenteDto(
        Long idDocente,
        Date fechaAlta,
        Date fechaBaja,
        Boolean activo,
        @NotNull
        Long idUser)
        implements Serializable {
}