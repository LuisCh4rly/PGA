package com.app.pga.App.Services.Implements;

import com.app.pga.App.Exception.BadRequestException;
import com.app.pga.App.Exception.DuplicateResourceException;
import com.app.pga.App.Models.Dtos.RequestDto.CampoRequestDto;
import com.app.pga.App.Models.Dtos.ResponseDto.CampoResponseDto;
import com.app.pga.App.Models.Entities.CampoFormativo;
import com.app.pga.App.Models.Mappers.CampoFormativoMapper;
import com.app.pga.App.Repositories.ICampoFormativoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)//inidicarle a Junit que use mockito
class CampoFormativoServiceTest {

    //dependencias que se usan
    @Mock
    private ICampoFormativoRepository campoFormativoRepository;
    @Mock
    private ActividadBaseService actividadBaseService;
    @Mock
    public CampoFormativoMapper campoFormativoMapper;
    @InjectMocks //instancia de la clase que se prueba e inyecta automaticamente los objetos simulados
    private CampoFormativoService campoFormativoService;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("Crea correctamente un Campo Formativo")
    void crearCampo() {
        //GIVEN (PREPARACIÓN)
        CampoRequestDto requestDto = new CampoRequestDto("Arquitectura de APIs", "Diseño de servicios REST y lógica de servidor en Java.");//respuesta simulada por parte del usuario

        CampoFormativo entidad = new CampoFormativo();
        entidad.setNombre("Arquitectura de APIs");
        entidad.setDescripcion("Diseño de servicios REST y lógica de servidor en Java.");

        CampoFormativo entidadGuardada = new CampoFormativo();
        entidadGuardada.setIdCampo(1L);
        entidadGuardada.setNombre("Arquitectura de APIs");
        entidadGuardada.setDescripcion("Diseño de servicios REST y lógica de servidor en Java.");
        entidadGuardada.setActivo(true);

        CampoResponseDto responseDto = new CampoResponseDto(1L, "Arquitectura de APIs", "Diseño de servicios REST y lógica de servidor en Java.", true);

        when(campoFormativoRepository.existsByNombreEqualsIgnoreCase("Arquitectura de APIs")).thenReturn(false);
        when(campoFormativoMapper.toEntity(requestDto)).thenReturn(entidad);
        when(campoFormativoRepository.save(entidad)).thenReturn(entidadGuardada);
        when(campoFormativoMapper.toDto(entidadGuardada)).thenReturn(responseDto);

        //WHEN (ACCIÓN)
        CampoResponseDto resultado = campoFormativoService.crearCampo(requestDto);

        //THEN (VERIFICACIÓN)
        assertNotNull(resultado);
        assertEquals(1L, resultado.idCampo());
        assertEquals("Arquitectura de APIs", resultado.nombre());
        assertEquals("Diseño de servicios REST y lógica de servidor en Java.", resultado.descripcion());
        assertTrue(resultado.activo());

        //VERIFICA QUE EL REPOSITORIO Y MAPPER FUE LLAMADO UNA VEZ
        verify(campoFormativoRepository).existsByNombreEqualsIgnoreCase("Arquitectura de APIs");
        verify(campoFormativoRepository).save(entidad);
        verify(campoFormativoMapper).toEntity(requestDto);
        verify(campoFormativoMapper).toDto(entidadGuardada);
    }

    @Test
    @DisplayName("Campo Formativo Duplicado")
    void deberia_lanzarExcepcionPorCampoDuplicado() {
        //GIVEN
        CampoRequestDto requestDto = new CampoRequestDto("Arquitectura de APIs", "Diseño de servicios REST y lógica de servidor en Java.");//respuesta simulada por parte del usuario

        when(campoFormativoRepository.existsByNombreEqualsIgnoreCase("Arquitectura de APIs")).thenReturn(true);

        //WHEN Y THEN
        com.app.pga.App.Exception.DuplicateResourceException exception = assertThrows(com.app.pga.App.Exception.DuplicateResourceException.class, () -> campoFormativoService.crearCampo(requestDto));
        assertEquals("Campo Formativo existente", exception.getMessage());

        verify(campoFormativoRepository).existsByNombreEqualsIgnoreCase("Arquitectura de APIs");
        verify(campoFormativoRepository, never()).save(any());
        verify(campoFormativoMapper, never()).toEntity(any());
    }

    @Test
    @DisplayName("Buscar Campo Formativo existente")
    void obtenerCampo() {
        CampoFormativo entidad = new CampoFormativo();
        entidad.setIdCampo(1L);
        entidad.setNombre("Arquitectura de APIs");
        entidad.setDescripcion("Diseño de servicios REST y lógica de servidor en Java.");
        entidad.setActivo(true);

        CampoResponseDto responseDto = new CampoResponseDto(1L, "Arquitectura de APIs", "Diseño de servicios REST y lógica de servidor en Java.", true);

        when(campoFormativoRepository.findById(1L)).thenReturn(Optional.of(entidad));
        when(campoFormativoMapper.toDto(entidad)).thenReturn(responseDto);

        CampoResponseDto resultado = campoFormativoService.obtenerCampo(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.idCampo());
        assertEquals("Arquitectura de APIs", resultado.nombre());
        assertEquals("Diseño de servicios REST y lógica de servidor en Java.", resultado.descripcion());
        assertEquals(true, resultado.activo());

        verify(campoFormativoRepository).findById(1L);
        verify(campoFormativoMapper).toDto(entidad);
    }

    @Test
    @DisplayName("Buscar Campo Formativo inexistente")
    void Deberia_lanzarExcepcionPorCampoInexistente() {
        when(campoFormativoRepository.findById(99L)).thenReturn(Optional.empty());

        com.app.pga.App.Exception.NotFoundException exception = assertThrows(com.app.pga.App.Exception.NotFoundException.class, () -> campoFormativoService.obtenerCampo(99L));
        assertEquals("Campo Formativo no encontrado", exception.getMessage());

        verify(campoFormativoRepository).findById(99L);
        verify(campoFormativoMapper, never()).toDto(any());
    }

    @Test
    @DisplayName("Actualizar descripción del campo formativo")
    void actualizarCampo() {
        CampoRequestDto requestDto = new CampoRequestDto("Arquitectura de APIs", "Diseño de la infraestructura centralizada: API Gateway, Cliente/Servidor, Endpoints.");//respuesta simulada por parte del usuario

        CampoFormativo entidad = new CampoFormativo();
        entidad.setIdCampo(1L);
        entidad.setNombre("Arquitectura de APIs");
        entidad.setDescripcion("Diseño de la infraestructura centralizada: API Gateway, Cliente/Servidor, Endpoints.");
        entidad.setActivo(true);

        CampoResponseDto responseDto = new CampoResponseDto(1L, "Arquitectura de APIs", "Diseño de la infraestructura centralizada: API Gateway, Cliente/Servidor, Endpoints.", true);

        when(campoFormativoRepository.findById(1L)).thenReturn(Optional.of(entidad));
        when(campoFormativoRepository.save(entidad)).thenReturn(entidad);
        when(campoFormativoMapper.toDto(entidad)).thenReturn(responseDto);

        CampoResponseDto resultado = campoFormativoService.actualizarCampo(requestDto, 1L);
        assertNotNull(resultado);
        assertEquals(1L, resultado.idCampo());
        assertEquals("Arquitectura de APIs", resultado.nombre());
        assertEquals("Diseño de la infraestructura centralizada: API Gateway, Cliente/Servidor, Endpoints.", resultado.descripcion());
        assertTrue(resultado.activo());

        verify(campoFormativoRepository).findById(1L);
        verify(campoFormativoRepository).save(entidad);
        verify(campoFormativoMapper).toDto(entidad);
    }

    @Test
    @DisplayName("Actualizar descripción de un campo formativo inexistente")
    void Deberia_LanzarExcepcionPorCampoInexistente() {
        CampoRequestDto requestDto = new CampoRequestDto("Arquitectura de APIs", "Diseño de la infraestructura centralizada: API Gateway, Cliente/Servidor, Endpoints.");//respuesta simulada por parte del usuario

        when(campoFormativoRepository.findById(99L)).thenReturn(Optional.empty());

        com.app.pga.App.Exception.NotFoundException exception = assertThrows(com.app.pga.App.Exception.NotFoundException.class, () -> campoFormativoService.actualizarCampo(requestDto, 99L));
        assertEquals("Campo Formativo no encontrado", exception.getMessage());

        verify(campoFormativoRepository).findById(99L);
        verify(campoFormativoRepository, never()).save(any());
        verify(campoFormativoMapper, never()).toDto(any());
    }

    @Test
    @DisplayName("Actualizar descripción  con campo formativo inhabilitado")
    void Debria_LanzarExcepcionPorCampoInhabilitado() {
        CampoRequestDto requestDto = new CampoRequestDto("Arquitectura de APIs", "Diseño de la infraestructura centralizada: API Gateway, Cliente/Servidor, Endpoints.");//respuesta simulada por parte del usuario

        CampoFormativo entidad = new CampoFormativo();
        entidad.setIdCampo(1L);
        entidad.setNombre("Arquitectura de APIs");
        entidad.setDescripcion("Diseño de la infraestructura centralizada: API Gateway, Cliente/Servidor, Endpoints.");
        entidad.setActivo(false);

        when(campoFormativoRepository.findById(1L)).thenReturn(Optional.of(entidad));

        com.app.pga.App.Exception.ResourceDisabledException exception = assertThrows(com.app.pga.App.Exception.ResourceDisabledException.class, () -> campoFormativoService.actualizarCampo(requestDto, 1L));
        assertEquals("Campo Formativo deshabilitado", exception.getMessage());

        verify(campoFormativoRepository).findById(1L);
        verify(campoFormativoRepository, never()).save(any());
        verify(campoFormativoMapper, never()).toDto(any());
    }

    @Test
    @DisplayName("Deshabilitar-Habilitar un campo formativo")
    void habitarDeshabilitar() {
        CampoFormativo entidad = new CampoFormativo();
        entidad.setIdCampo(1L);
        entidad.setNombre("Arquitectura de APIs");
        entidad.setDescripcion("Diseño de la infraestructura centralizada: API Gateway, Cliente/Servidor, Endpoints.");
        entidad.setActivo(true);

        CampoResponseDto responseDto = new CampoResponseDto(1L, "Arquitectura de APIs", "Diseño de la infraestructura centralizada: API Gateway, Cliente/Servidor, Endpoints.", false);

        when(campoFormativoRepository.findById(1L)).thenReturn(Optional.of(entidad));
        when(campoFormativoRepository.save(entidad)).thenReturn(entidad);
        when(campoFormativoMapper.toDto(entidad)).thenReturn(responseDto);

        CampoResponseDto resultado = campoFormativoService.habitarDeshabilitar(1L);
        assertNotNull(resultado);
        assertEquals(1L, resultado.idCampo());
        assertEquals("Arquitectura de APIs", resultado.nombre());
        assertEquals("Diseño de la infraestructura centralizada: API Gateway, Cliente/Servidor, Endpoints.", resultado.descripcion());
        assertFalse(resultado.activo());

        verify(campoFormativoRepository).findById(1L);
        verify(campoFormativoRepository).save(entidad);
        verify(campoFormativoMapper).toDto(entidad);
    }

    @Test
    @DisplayName("Deshabilitar-Habilitar un campo formativo inexistente")
    void Deberia_LanzarExcepcionPorCampoInexistenteParaHabDesha() {
        when(campoFormativoRepository.findById(99L)).thenReturn(Optional.empty());

        com.app.pga.App.Exception.NotFoundException exception = assertThrows(com.app.pga.App.Exception.NotFoundException.class, () -> campoFormativoService.habitarDeshabilitar(99L));
        assertEquals("Campo Formativo no encontrado", exception.getMessage());

        verify(campoFormativoRepository).findById(99L);
        verify(campoFormativoRepository, never()).save(any());
        verify(campoFormativoMapper, never()).toDto(any());
    }
}