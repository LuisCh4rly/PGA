package com.app.pga.Auth.Models.Dtos.Response;

import java.time.Instant;

public record AuthResponseDto (
    String token,
    String tokenType,
    Instant expiresInSeconds
){

}
