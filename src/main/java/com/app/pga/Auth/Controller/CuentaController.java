package com.app.pga.Auth.Controller;

import com.app.pga.Auth.JWT.JWTService;
import com.app.pga.Auth.Models.Dtos.Request.AuthRegisterDto;
import com.app.pga.Auth.Models.Dtos.Request.AuthRequestDto;
import com.app.pga.Auth.Models.Dtos.Response.AuthResponseDto;
import com.app.pga.Auth.Security.UserDetailsServiceImp;
import com.app.pga.Auth.Service.CuentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;
@Tag(name = "Cuentas", description = "Enpoints para la gestion de las cuentas del sistema")
@RestController
@RequestMapping("/api/cuentas")
@RequiredArgsConstructor
public class CuentaController {
    private final CuentaService cuentaService;
    @GetMapping

    @Operation(summary = "Consultar cuentas del sistema", description = "Lista todas las cuentas registradas en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Consulta  realizada correctamente"),
    })
    public ResponseEntity<?> listarCuentas() {
        return ResponseEntity.ok(cuentaService.listarCuentas());
    }

    @Operation(summary = "Cuenta por id", description = "Permite consultar la información de la cuenta de acuerdo al id especificado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cuenta consultada correctamente"),
            @ApiResponse(responseCode = "404", description = "Cuenta no encontrada"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> cuentaById(
            @Parameter(description = "Id de la cuenta", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(cuentaService.cuentaById(id));
    }
}
