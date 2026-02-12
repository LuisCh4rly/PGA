package com.app.pga.App.Models.Dtos.RequestDto;

import com.app.pga.App.Models.Entities.Usuario;

import java.io.Serializable;

/**
 * DTO for {@link com.app.pga.App.Models.Entities.Docente}
 */
public record DocenteRequestDto(

        Long idUsuario
) implements Serializable {
}