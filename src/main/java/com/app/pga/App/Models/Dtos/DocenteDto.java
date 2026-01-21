package com.app.pga.App.Models.Dtos;

import com.app.pga.App.Models.Entities.Docente;
import com.app.pga.App.Models.Entities.Usuario;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

/**
 * DTO for {@link Docente}
 */
public record DocenteDto(
        Long idDocente,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate fechaAlta,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate fechaBaja,
        Boolean activo,
        @NotNull
        UsuarioDto usuarioDto)
        implements Serializable {
}