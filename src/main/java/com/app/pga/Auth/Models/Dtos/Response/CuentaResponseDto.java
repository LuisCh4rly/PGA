package com.app.pga.Auth.Models.Dtos.Response;

import com.app.pga.Auth.Models.Entities.Role;

public record CuentaResponseDto(

        String nombre,
        String apellidoPaterno,
        String apellidoMaterno,
        String email,
        Boolean activo,
        String rol
) {
}
