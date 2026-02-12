package com.app.pga.Auth.Models.Dtos.Request;

import jakarta.validation.constraints.NotBlank;

public record AuthRequestDto(
        @NotBlank String username,
        @NotBlank String password
) {

}
