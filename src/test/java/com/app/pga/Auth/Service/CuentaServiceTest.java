package com.app.pga.Auth.Service;

import com.app.pga.App.Exception.BadRequestException;
import com.app.pga.App.Exception.DuplicateResourceException;
import com.app.pga.App.Exception.NotFoundException;
import com.app.pga.App.Models.Dtos.RequestDto.UsuarioRequestDto;
import com.app.pga.App.Models.Entities.Expediente;
import com.app.pga.App.Models.Entities.Usuario;
import com.app.pga.App.Models.Enum.EstadoExpediente;
import com.app.pga.App.Services.Implements.ExpedienteService;
import com.app.pga.App.Services.Implements.UsuarioService;
import com.app.pga.Auth.JWT.JWTService;
import com.app.pga.Auth.Models.Dtos.Request.AuthRegisterDto;
import com.app.pga.Auth.Models.Dtos.Request.cambioPasswordRequestDto;
import com.app.pga.Auth.Models.Dtos.Response.CuentaResponseDto;
import com.app.pga.Auth.Models.Entities.Cuenta;
import com.app.pga.Auth.Models.Entities.Role;
import com.app.pga.Auth.Models.Enum.ERole;
import com.app.pga.Auth.Repositories.ICuentaRepository;
import com.app.pga.Auth.Repositories.IRoleRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CuentaServiceTest {
    @Mock
    private  ICuentaRepository cuentaRepository;
    @Mock
    private  IRoleRepository roleRepository;
    @Mock
    private  UsuarioService usuarioService;
    @Mock
    private  JWTService jwtService;
    @Mock
    private  PasswordEncoder encoder;
    @Mock
    private  EmailService emailService;
    @Mock
    private  ExpedienteService expedienteService;

    @InjectMocks
    private CuentaService cuentaService;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("Dado que envío un dto con datos correctos, entonces se registra en BD exitosamente")

    void registerTest_DeberiaGuardarCuentaCuandoDatosSonCorrectos() {
        // dado
        AuthRegisterDto dto = new AuthRegisterDto(
                "Luis",
                "Cruz",
                "Ibarra",
                "5555555555",
                "volcanes 11",
                "UNAM",
                "Ingeniería",
                LocalDate.of(2027,7,12),
                "Luis_charly10@outlook.com",
                "ALUMNO"
        );
        Role roleSimulado = new Role(1L, ERole.ALUMNO);
        Usuario usuarioSimulado = Usuario.builder()
                .idUsuario(1L)
                .nombre("Luis")
                .apellidoPaterno("Cruz")
                .apellidoMaterno("Ibarra")
                .direccion("volcanes 11")
                .telefono("5555555555")
                .created_At(LocalDate.now())
                .fechaAlta(LocalDate.now())
                .fechaBaja(null)
                .build();
        CuentaResponseDto esperado = new CuentaResponseDto(
                1l,
                1l,
                "Luis",
                "Cruz",
                "Ibarra",
                "Luis_charly10@outlook.com",
                true,
                "ALUMNO"

        );
        when(cuentaRepository.findByEmail(dto.email())).thenReturn(Optional.empty());
        when(roleRepository.findByName(ERole.valueOf(dto.rol()))).thenReturn(Optional.of(roleSimulado));
        when(usuarioService.createUsuario(any(UsuarioRequestDto.class))).thenReturn(usuarioSimulado);
        when(encoder.encode(anyString())).thenReturn("3Ng362Zi");
        Expediente expedienteSimulado = new Expediente( 1L, EstadoExpediente.NO_APROBADO, null,usuarioSimulado);
        when(expedienteService.obtenerPorAlumno(1l)).thenReturn(expedienteSimulado);
        // cuando
        CuentaResponseDto resultado = cuentaService.register(dto);
        // entonces
        assertNotNull(resultado, "No se retornó un objeto");
        assertEquals(esperado.email(), resultado.email());
        assertEquals(esperado.rol(), resultado.rol());
        verify(cuentaRepository, times(1)).save(any(Cuenta.class));
        verify(expedienteService, times(1)).sincronizarExpediente(expedienteSimulado);
        verify(emailService, times(1)).sendEmail(eq("Luis_charly10@outlook.com"), eq("Alta Usuario"), anyString());
    }

    @Test
    @DisplayName("Dado que envío un dto con email ya registrado, entonces se lanza una excepción y no se registra en BD")

    void registerTest_DeberiaMandarExcepcionCuandoEmailEsDuplicado () {
        // dado
        AuthRegisterDto dto = new AuthRegisterDto(
                "Luis",
                "Cruz",
                "Ibarra",
                "5555555555",
                "volcanes 11",
                "UNAM",
                "Ingeniería",
                LocalDate.of(2027,7,12),
                "Luis_charly10@outlook.com",
                "ALUMNO"
        );

        when(cuentaRepository.findByEmail(dto.email())).thenReturn(Optional.of(new Cuenta()));
        //cuando
        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class,
        ()-> cuentaService.register(dto));

        //entonces
        assertEquals("El email ya está en uso", exception.getMessage());
        verify(roleRepository, never()).findByName(any(ERole.class));
        verify(usuarioService, never()).createUsuario(any(UsuarioRequestDto.class));
        verify(encoder, never()).encode(anyString());
        verify(expedienteService, never()).obtenerPorAlumno(anyLong());
        verify(cuentaRepository, never()).save(any(Cuenta.class));
        verify(emailService,never()).sendEmail(anyString(),anyString(),anyString());
    }

    @Test
    @DisplayName("Dado que envío un dto con un Rol inexistente, entonces se lanza una excepción y no se registra en BD")

    void registerTest_DeberiaMandarExcepcionCuandoRolNoExiste () {
        // dado
        AuthRegisterDto dto = new AuthRegisterDto(
                "Luis",
                "Cruz",
                "Ibarra",
                "5555555555",
                "volcanes 11",
                "UNAM",
                "Ingeniería",
                LocalDate.of(2027,7,12),
                "Luis_charly10@outlook.com",
                "ALUMNO"
        );

        when(cuentaRepository.findByEmail(dto.email())).thenReturn(Optional.empty());
        when(roleRepository.findByName(any())).thenReturn(Optional.empty());
        //cuando
        NotFoundException exception = assertThrows(NotFoundException.class,
                ()-> cuentaService.register(dto));

        //entonces
        assertEquals("Rol no encontrado", exception.getMessage());
        verify(usuarioService, never()).createUsuario(any(UsuarioRequestDto.class));
        verify(encoder, never()).encode(anyString());
        verify(expedienteService, never()).obtenerPorAlumno(anyLong());
        verify(cuentaRepository, never()).save(any(Cuenta.class));
        verify(emailService,never()).sendEmail(anyString(),anyString(),anyString());
    }


    @Test
    @DisplayName("Dado que busco una cuenta con id existente, se retorna la información de la cuenta correctamente")

    void cuentaById_DeberiaRetornarInformacionDeLaCuentaCuandoIdEsValido(){
        //DADO
        Role roleSimulado = new Role(1L, ERole.ALUMNO);
        Usuario usuarioSimulado = Usuario.builder()
                .idUsuario(1L)
                .nombre("Luis")
                .apellidoPaterno("Cruz")
                .apellidoMaterno("Ibarra")
                .direccion("volcanes 11")
                .telefono("5555555555")
                .created_At(LocalDate.now())
                .fechaAlta(LocalDate.now())
                .fechaBaja(null)
                .build();

        Cuenta cuentaGuardada =  new Cuenta(
                1L,
                "",
                roleSimulado,
                usuarioSimulado,
                "Luis_Charly10@outlook.com",
                true
        );

        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuentaGuardada));
        //cuando
        CuentaResponseDto resultado = cuentaService.cuentaById(1L);

        //entonces
        assertNotNull(resultado);
        assertEquals("Luis_Charly10@outlook.com", resultado.email(), "los correos son diferentes");
        assertEquals("ALUMNO", resultado.rol(), "los roles son diferentes");
        assertEquals(1L, resultado.idUsuario(), "los id de cuenta son diferentes");
        verify(cuentaRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Dado que busco una cuenta con id inexistente, se lanza una excepcion")

    void cuentaById_DeberiaLanzarExcepcionCuandoIdEsInvalido(){
        //DADO
        when(cuentaRepository.findById(999L)).thenReturn(Optional.empty());
        //cuando
       NotFoundException exception = assertThrows(NotFoundException.class,
               ()->cuentaService.cuentaById(999L));

        //entonces
        assertEquals("Cuenta No encontrada", exception.getMessage());

        verify(cuentaRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("dado que envio datos correctos, entonces se cambia la contraseña")
    void cambioPassword_DeberiaCambiarPasswordCuandoDatosSonCorrectos() {
        //dado
        String email = "Luis_Charly10@outlook.com";
        Role roleSimulado = new Role(1L, ERole.ALUMNO);
        Usuario usuarioSimulado = Usuario.builder()
                .idUsuario(1L)
                .nombre("Luis")
                .apellidoPaterno("Cruz")
                .apellidoMaterno("Ibarra")
                .direccion("volcanes 11")
                .telefono("5555555555")
                .created_At(LocalDate.now())
                .fechaAlta(LocalDate.now())
                .fechaBaja(null)
                .build();

        Cuenta cuentaGuardada =  new Cuenta(
                1L,
                "alumno123",
                roleSimulado,
                usuarioSimulado,
                "Luis_Charly10@outlook.com",
                true
        );
        cambioPasswordRequestDto requestDto = new cambioPasswordRequestDto(
                "Luis_Charly10@outlook.com",
                "alumno123",
                "alumno12345"
        );

        when(cuentaRepository.findByEmail(email)).thenReturn(Optional.of(cuentaGuardada));
        when(encoder.matches(requestDto.actualPassword(),cuentaGuardada.getPassword())).thenReturn(true);
        when(encoder.encode(requestDto.nuevaPassword())).thenReturn("claveEncriptadaNueva");
        //cuando
        cuentaService.cambioPassword(email, requestDto);

        //entonces
        assertEquals("claveEncriptadaNueva", cuentaGuardada.getPassword());
        assertFalse(cuentaGuardada.getDebeCambiarPassword());
        verify(cuentaRepository, times(1)).save(cuentaGuardada);
    }

    @Test
    @DisplayName("Dado que no se encuentra la cuenta, se lanza una excepcion y no se cambia la contraseña")
    void cambioPassword_DeberiaLanzarExcepcionCuandoCuentaNoExiste() {

        //dado
        String email = "Luis_Charly10@outlook.com";
        when(cuentaRepository.findByEmail(email)).thenReturn(Optional.empty());

        //cuando
        NotFoundException exception = assertThrows(NotFoundException.class,
                ()->cuentaService.cambioPassword(email,any(cambioPasswordRequestDto.class) ));

        //entonces

        assertEquals("Cuenta no registrada", exception.getMessage());
        verify(encoder,never()).matches(anyString(),anyString());
        verify(encoder,never()).encode(anyString());
        verify(cuentaRepository, never()).save(any(Cuenta.class));
    }

    @Test
    @DisplayName("Dado que las contraseñas actuales no coinciden, se lanza una excepcion y no se cambia la contraseña")
    void cambioPassword_DeberiaLanzarExcepcionCuandoContraseñasNoCoinciden() {

        //dado
        String email = "Luis_Charly10@outlook.com";
        cambioPasswordRequestDto request = new cambioPasswordRequestDto("Luis_Charly10@outlook.com","ClaveEquivocada", "NuevaClave456");

        Cuenta cuentaSimulada = Cuenta.builder()
                .email(email)
                .password("claveEncriptadaReal")
                .debeCambiarPassword(true)
                .build();


        when(cuentaRepository.findByEmail(email)).thenReturn(Optional.of(cuentaSimulada));
        when(encoder.matches(request.actualPassword(),cuentaSimulada.getPassword())).thenReturn(false);
        //cuando
        BadRequestException exception = assertThrows(BadRequestException.class,
                ()->cuentaService.cambioPassword(email,request));

        //entonces

        assertEquals("Contraseña actual incorrecta", exception.getMessage());
        verify(encoder,times(1)).matches(anyString(),anyString());
        verify(encoder,never()).encode(anyString());
        verify(cuentaRepository, never()).save(any(Cuenta.class));
    }
}