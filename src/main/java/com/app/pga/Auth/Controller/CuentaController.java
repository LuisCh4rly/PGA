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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/cuentas")
@RequiredArgsConstructor
public class CuentaController {
    private final CuentaService cuentaService;
    @GetMapping


    public ResponseEntity<?> listarCuentas() {
        return ResponseEntity.ok(cuentaService.listarCuentas());
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> cuentaById(@PathVariable Long id) {
        return ResponseEntity.ok(cuentaService.cuentaById(id));
    }
}
