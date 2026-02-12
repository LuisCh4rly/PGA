package com.app.pga.Auth.Service;

import com.app.pga.App.Exception.NotFoundException;
import com.app.pga.App.Models.Dtos.RequestDto.AlumnoRequestDto;
import com.app.pga.App.Models.Dtos.RequestDto.DocenteRequestDto;
import com.app.pga.App.Models.Dtos.RequestDto.UsuarioRequestDto;
import com.app.pga.App.Models.Entities.Usuario;
import com.app.pga.App.Services.Implements.AlumnoService;
import com.app.pga.App.Services.Implements.DocenteService;
import com.app.pga.App.Services.Implements.UsuarioService;
import com.app.pga.Auth.Models.Dtos.Request.AuthRegisterDto;
import com.app.pga.Auth.Models.Dtos.Response.CuentaResponseDto;
import com.app.pga.Auth.Models.Entities.Cuenta;
import com.app.pga.Auth.Models.Entities.Role;
import com.app.pga.Auth.Models.Enum.ERole;
import com.app.pga.Auth.Repositories.ICuentaRepository;
import com.app.pga.Auth.Repositories.IRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class CuentaService implements ICuentaService {
    private final ICuentaRepository cuentaRepository;
    private final IRoleRepository roleRepository;
    private final UsuarioService usuarioService;
    private final AlumnoService alumnoService;
    private final DocenteService docenteService;
    private final PasswordEncoder encoder;


    @Override
    public CuentaResponseDto register(AuthRegisterDto dto) {

        cuentaRepository.findByEmail(dto.email()).ifPresent(e -> {
            throw new IllegalArgumentException("El email ya está en uso");
        });
        Role role = roleRepository .findByName(ERole.valueOf(dto.rol()))
                .orElseThrow(() -> new NotFoundException("Rol no encontrado"));
        //REGISTRO DE USUARIOS
        UsuarioRequestDto usuarioDto = new UsuarioRequestDto(dto.nombre(),dto.apellidoPaterno(),dto.apellidoMaterno(),dto.telefono(), dto.direccion());
        Usuario usuarioNuevo = usuarioService.createUsuario(usuarioDto);

       //REGISTRO DE ALUMNOS O DOCENTES
       if (role.getName().equals(ERole.ALUMNO)){
           AlumnoRequestDto alumnoDto = new AlumnoRequestDto( dto.universidad(), dto.carrera(),dto.fechaTermino(),usuarioNuevo.getIdUsuario());
           alumnoService.createAlumno(alumnoDto);

       }else if(role.getName().equals(ERole.DOCENTE)){
           DocenteRequestDto docenteRequestDto = new DocenteRequestDto(usuarioNuevo.getIdUsuario());
           docenteService.createDocente(docenteRequestDto);
       }
       //REGISTRO DE CUENTA
        Cuenta cuenta = Cuenta.builder()
                .usuario(usuarioNuevo)
                .role(role)
                .email(dto.email())
                .password(encoder.encode(dto.password()))
                .build();

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
                .map(c-> new CuentaResponseDto(
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
}
