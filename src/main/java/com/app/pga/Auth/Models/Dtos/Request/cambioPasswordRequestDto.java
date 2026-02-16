package com.app.pga.Auth.Models.Dtos.Request;

public record cambioPasswordRequestDto(
        String email,
        String actualPassword,
        String nuevaPassword
) {
}
