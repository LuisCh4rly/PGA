package com.app.pga.Auth.Service;

import com.app.pga.App.Exception.BadRequestException;
import com.app.pga.App.Exception.NotFoundException;
import com.app.pga.App.Models.Dtos.RequestDto.UsuarioRequestDto;
import com.app.pga.App.Models.Entities.Expediente;
import com.app.pga.App.Models.Entities.Usuario;
import com.app.pga.App.Services.Implements.ExpedienteService;
import com.app.pga.App.Services.Implements.UsuarioService;
import com.app.pga.Auth.JWT.JWTService;
import com.app.pga.Auth.Models.Dtos.Request.AuthRegisterDto;
import com.app.pga.Auth.Models.Dtos.Request.cambioPasswordRequestDto;
import com.app.pga.Auth.Models.Dtos.Response.AuthResponseDto;
import com.app.pga.Auth.Models.Dtos.Response.CuentaResponseDto;
import com.app.pga.Auth.Models.Entities.Cuenta;
import com.app.pga.Auth.Models.Entities.Role;
import com.app.pga.Auth.Models.Enum.ERole;
import com.app.pga.Auth.Repositories.ICuentaRepository;
import com.app.pga.Auth.Repositories.IRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class CuentaService implements ICuentaService {
    private final ICuentaRepository cuentaRepository;
    private final IRoleRepository roleRepository;
    private final UsuarioService usuarioService;
    private final JWTService jwtService;
    private final PasswordEncoder encoder;
    private final EmailService emailService;
    private final ExpedienteService expedienteService;


    @Override
    public CuentaResponseDto register(AuthRegisterDto dto) {

        cuentaRepository.findByEmail(dto.email()).ifPresent(e -> {
            throw new IllegalArgumentException("El email ya está en uso");
        });
        Role role = roleRepository.findByName(ERole.valueOf(dto.rol()))
                .orElseThrow(() -> new NotFoundException("Rol no encontrado"));
        //REGISTRO DE USUARIOS
        UsuarioRequestDto usuarioDto = new UsuarioRequestDto(dto.nombre(), dto.apellidoPaterno(), dto.apellidoMaterno(), dto.telefono(), dto.direccion());
        Usuario usuarioNuevo = usuarioService.createUsuario(usuarioDto);


        String passwordTemporal = generate(8);

        Cuenta cuenta = Cuenta.builder()
                .usuario(usuarioNuevo)
                .role(role)
                .email(dto.email())
                .password(encoder.encode(passwordTemporal))
                .debeCambiarPassword(true)
                .build();
        cuentaRepository.save(cuenta);
        if(cuenta.getRole().getName().equals(ERole.ALUMNO)){
            Expediente expediente = expedienteService.obtenerPorAlumno(cuenta.getUsuario().getIdUsuario());
            expedienteService.sincronizarExpediente(expediente);
        }
        emailService.sendEmail(cuenta.getEmail(), "Alta Usuario", passwordTemporal);

        return new CuentaResponseDto(
                cuenta.getUsuario().getNombre(),
                cuenta.getUsuario().getApellidoPaterno(),
                cuenta.getUsuario().getApellidoMaterno(),
                cuenta.getEmail(),
                cuenta.getUsuario().getActivo(),
                cuenta.getRole().getName().toString());
    }


    @Override
    public List<CuentaResponseDto> listarCuentas() {
        List<Cuenta> cuentas = cuentaRepository.findAll();

        return cuentas.stream()
                .map(c -> new CuentaResponseDto(
                        c.getUsuario().getNombre(),
                        c.getUsuario().getApellidoPaterno(),
                        c.getUsuario().getApellidoMaterno(),
                        c.getEmail(),
                        c.getUsuario().getActivo(),
                        c.getRole().getName().toString()
                ))
                .collect(Collectors.toList());
    }
    @Override
    public CuentaResponseDto cuentaById(Long id) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Cuenta No encontrada"));
        return new CuentaResponseDto(
                cuenta.getUsuario().getNombre(),
                cuenta.getUsuario().getApellidoPaterno(),
                cuenta.getUsuario().getApellidoMaterno(),
                cuenta.getEmail(),
                cuenta.getUsuario().getActivo(),
                cuenta.getRole().getName().toString());
    }


    private String generate(int length) {
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%&*";
        SecureRandom random = new SecureRandom();

        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }


    public void cambioPassword( String email, cambioPasswordRequestDto request
    ) {

        Cuenta cuenta = cuentaRepository.findByEmail(email)
                .orElseThrow(()->new NotFoundException("Cuenta no registrada"));


        if (!encoder.matches(request.actualPassword(), cuenta.getPassword())) {
            throw new BadRequestException("Contraseña actual incorrecta");
        }

        cuenta.setPassword( encoder.encode(request.nuevaPassword()));
        cuenta.setDebeCambiarPassword(false);
        cuentaRepository.save(cuenta);
    }
}
