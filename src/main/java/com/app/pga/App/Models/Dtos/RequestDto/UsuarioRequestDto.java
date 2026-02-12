package com.app.pga.App.Models.Dtos.RequestDto;

import java.io.Serializable;

/**
 * DTO for {@link com.app.pga.App.Models.Entities.Usuario}
 */
public record UsuarioRequestDto(
        String nombre,
        String apellidoPaterno,
        String apellidoMaterno,
        String telefono,
        String direccion

) implements Serializable {
}