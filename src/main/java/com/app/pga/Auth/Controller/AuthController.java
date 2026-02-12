package com.app.pga.Auth.Controller;

import com.app.pga.Auth.JWT.JWTService;
import com.app.pga.Auth.Models.Dtos.Request.AuthRegisterDto;
import com.app.pga.Auth.Models.Dtos.Request.AuthRequestDto;
import com.app.pga.Auth.Models.Dtos.Response.AuthResponseDto;
import com.app.pga.Auth.Security.UserDetailsServiceImp;
import com.app.pga.Auth.Service.CuentaService;
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

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final CuentaService cuentaService;
    private final UserDetailsServiceImp userDetailsService;
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

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody AuthRegisterDto request) {
        var user = cuentaService.register(request);
        return ResponseEntity.ok(Map.of("message","Usuario registrado","Email", user.email()));
    }
    @GetMapping("/usuario-actual")

    public UserDetails getCurrentUser (Principal principal){
        return userDetailsService.loadUserByUsername(principal.getName());
    }
}
