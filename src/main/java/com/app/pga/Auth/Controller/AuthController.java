package com.app.pga.Auth.Controller;

import com.app.pga.Auth.JWT.JWTService;
import com.app.pga.Auth.Models.Dtos.Request.AuthRegisterDto;
import com.app.pga.Auth.Models.Dtos.Request.AuthRequestDto;
import com.app.pga.Auth.Models.Dtos.Request.cambioPasswordRequestDto;
import com.app.pga.Auth.Models.Dtos.Response.AuthResponseDto;
import com.app.pga.Auth.Models.Dtos.Response.CuentaResponseDto;
import com.app.pga.Auth.Models.Dtos.Response.UsuarioActualDto;
import com.app.pga.Auth.Models.Entities.Cuenta;
import com.app.pga.Auth.Security.UserDetailsImp;
import com.app.pga.Auth.Security.UserDetailsServiceImp;
import com.app.pga.Auth.Service.CuentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;
@Tag(name ="Auth", description = "Endpoints encargados del registro y autorizacion de usuarios")
@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final CuentaService cuentaService;
    private final UserDetailsServiceImp userDetailsService;

    @Operation(summary = "Iniciar sesión", description = "Autentica un usuario y genera un token JWT para acceder a los endpoints protegidos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticación exitosa"),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    })
    @PostMapping("/login")
    public AuthResponseDto login (@RequestBody AuthRequestDto request) {
        //autenticar usuario

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        //obtener datos del usuario
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        //generar token //devolver respuesta
        return jwtService.generateToken(userDetails);
    }

    @Operation(summary = "Registrar usuario", description = "Registra un nuevo usuario y genera una cuenta con contraseña temporal.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario registrado correctamente"),
            @ApiResponse(responseCode = "404", description = "Rol no encontrado"),
            @ApiResponse(responseCode = "409", description = "El correo ya se encuentra registrado")
    })
    @PostMapping("/register")
    public ResponseEntity<Map <String,Object>> register(@Valid @RequestBody AuthRegisterDto request) {
        var user = cuentaService.register(request);
        return ResponseEntity.ok(Map.of("message","Usuario registrado","Email", user.email()));
    }

    @Operation(summary = "Consultar usuario actual", description = "Obtiene la información del usuario autenticado actualmente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente"),
            @ApiResponse(responseCode = "401", description = "Usuario no autenticado")
    })
    @GetMapping("/usuario-actual")

    public UsuarioActualDto getCurrentUser (Principal principal){
        UserDetailsImp userDetails = (UserDetailsImp) userDetailsService.loadUserByUsername(principal.getName());
        CuentaResponseDto cuenta = cuentaService.cuentaById(userDetails.getId());
        return UsuarioActualDto.builder()
                .id(cuenta.idUsuario())
                .email(userDetails.getEmail())
                .debeCambiarPassword(userDetails.getDebeCambiarPassword())
                .authorities(userDetails.getAuthorities())
                .username(userDetails.getUsername())
                .build();

    }

    @Operation(summary = "Cambiar contraseña", description = "Permite actualizar la contraseña de la cuenta autenticada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contraseña actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Contraseña actual incorrecta"),
            @ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
    })
    @PutMapping("/cambiar-password")
    public ResponseEntity<?> cambiarPassword (@Valid @RequestBody cambioPasswordRequestDto request, Authentication authentication) {
        cuentaService.cambioPassword(authentication.getName(),request);
        return ResponseEntity.ok(Map.of("message","Cambio de contraseña exitoso"));
    }
}
