package com.app.pga.App.Controllers;

import com.app.pga.App.Exception.DuplicateResourceException;
import com.app.pga.App.Exception.NotFoundException;
import com.app.pga.App.Exception.ResourceDisabledException;
import com.app.pga.App.Models.Dtos.RequestDto.CampoRequestDto;
import com.app.pga.App.Models.Dtos.ResponseDto.CampoResponseDto;
import com.app.pga.App.Services.Interfaces.ICampoFormativoService;
import com.app.pga.Auth.JWT.JWTService;
import com.app.pga.Auth.Security.UserDetailsServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CampoFormativoController.class)//permite centrarnos en el controlador que especificamente vamos a probar
class CampoFormativoControllerTest {

    @Autowired
    private MockMvc mockMvc; //permite simular pediciones HTTP como POST, PUT, GET, etc
    @MockitoBean
    private ICampoFormativoService campoFormativoService; //creamos un mock del servicio
    @MockitoBean
    private JWTService jwtService;
    @MockitoBean
    private UserDetailsServiceImp userDetailsServiceImp;
    @Autowired
    private ObjectMapper objectMapper; //convierte objetos java a json


    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("Peticion para crear un campo formativo")
    void crearCampo() throws Exception{
        //GIVEN
        CampoRequestDto request = new CampoRequestDto("Arquitectura de APIs", "Diseño de servicios REST y lógica de servidor en Java.");

        CampoResponseDto response = new CampoResponseDto(1L,"Arquitectura de APIs", "Diseño de servicios REST y lógica de servidor en Java.", true);

        when(campoFormativoService.crearCampo(any(CampoRequestDto.class))).thenReturn(response);//cuando el controlador llame al servicio, mockito devolvera el dto de respuesta

        //WHEN Y THEN
        mockMvc.perform(post("/api/campo")//equivale a hacer POST /api/campo
                .contentType(MediaType.APPLICATION_JSON)//convierte en dto a {"nombre": "Arquitectura de APIs",
                .content(objectMapper.writeValueAsString(request)))//-------- "descripcion": ""Diseño de servicios REST y lógica de servidor en Java."}
                .andExpect(status().isCreated())//verifica el status 201 CREATED
                .andExpect(jsonPath("$.idCampo").value(1))//verifix EL JSON devuelto contenga {"idCampo":1}
                .andExpect(jsonPath("$.nombre").value("Arquitectura de APIs"));

        verify(campoFormativoService).crearCampo(any(CampoRequestDto.class));
    }

    @Test
    @DisplayName("Petición para crear un campo formativo con parámetros vacíos")
    void crearCampoConParametrosVacios() throws Exception{
        CampoRequestDto request = new CampoRequestDto("", "");

        mockMvc.perform(post("/api/campo")//equivale a hacer POST /api/campo
                        .contentType(MediaType.APPLICATION_JSON)//convierte en dto a {"nombre": "Arquitectura de APIs",
                        .content(objectMapper.writeValueAsString(request)))//-------- "descripcion": "Diseño de servicios REST y lógica de servidor en Java."}
                .andExpect(status().isBadRequest());

        verify(campoFormativoService, never()).crearCampo(any());
    }

    @Test
    @DisplayName("Petición para crear un campo formativo duplicado")
    void crearCampoDuplicado() throws Exception{
        CampoRequestDto request = new CampoRequestDto("Arquitectura de APIs", "Diseño de servicios REST y lógica de servidor en Java.");

        //SIMULA QUE EL SERVICIO LANZA UNA EXCEPCION DE CONFLICTO
        when(campoFormativoService.crearCampo(any())).thenThrow(new DuplicateResourceException("El campo formativo ya existe"));//cuando el controlador llame al servicio, mockito devolvera el dto de respuesta

        mockMvc.perform(post("/api/campo")//equivale a hacer POST /api/campo
                        .contentType(MediaType.APPLICATION_JSON)//convierte en dto a {"nombre": "Arquitectura de APIs",
                        .content(objectMapper.writeValueAsString(request)))//-------- "descripcion": "Diseño de servicios REST y lógica de servidor en Java."}
                .andExpect(status().isConflict());

        verify(campoFormativoService).crearCampo(any(CampoRequestDto.class));
    }

    @Test
    @DisplayName("Peticion para buscar un campo formativo")
    void obtenerCampo() throws Exception{
        CampoResponseDto response = new CampoResponseDto(1L,"Arquitectura de APIs", "Diseño de servicios REST y lógica de servidor en Java.", true);

        when(campoFormativoService.obtenerCampo(1L)).thenReturn(response);//cuando el controlador llame al servicio, mockito devolvera el dto de respuesta

        mockMvc.perform(get("/api/campo/{id}", 1L)//equivale a hacer POST /api/campo
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())//verifica el status 200 OK
                .andExpect(jsonPath("$.idCampo").value(1))
                .andExpect(jsonPath("$.nombre").value("Arquitectura de APIs"));//verifix EL JSON devuelto contenga {"idCampo":1}

        verify(campoFormativoService).obtenerCampo(1L);
    }

    @Test
    @DisplayName("Peticion para buscar un campo formativo inexistente")
    void obtenerCampoInexistente() throws Exception{
        when(campoFormativoService.obtenerCampo(99L)).thenThrow(new NotFoundException("Campo Formativo no encontrado"));//cuando el controlador llame al servicio, mockito devolvera el dto de respuesta

        mockMvc.perform(get("/api/campo/{id}", 99L)//equivale a hacer POST /api/campo
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());//verifica el status 404 Not Found

        verify(campoFormativoService).obtenerCampo(99L);
    }

    @Test
    @DisplayName("Actualización de la descripción del campo formativo")
    void actualizarCampo() throws Exception{
        CampoRequestDto request = new CampoRequestDto("Arquitectura de APIs", "Diseño de servicios REST y lógica de servidor en Java.");

        CampoResponseDto response = new CampoResponseDto(1L,"Arquitectura de APIs", "Diseño de servicios REST y lógica de servidor en Java.", true);

        when(campoFormativoService.actualizarCampo(request, 1l)).thenReturn(response);//cuando el controlador llame al servicio, mockito devolvera el dto de respuesta

        mockMvc.perform(put("/api/campo/{id}", 1L)//equivale a hacer POST /api/campo
                        .contentType(MediaType.APPLICATION_JSON)//convierte en dto a {"nombre": "Arquitectura de APIs",
                        .content(objectMapper.writeValueAsString(request)))//-------- "descripcion": ""Diseño de servicios REST y lógica de servidor en Java."}
                .andExpect(status().isOk())//verifica el status 200 OK
                .andExpect(jsonPath("$.idCampo").value(1))//verifix EL JSON devuelto contenga {"idCampo":1}
                .andExpect(jsonPath("$.nombre").value("Arquitectura de APIs"));

        verify(campoFormativoService).actualizarCampo(request, 1L);
    }

    @Test
    @DisplayName("Actualización de la descripción del campo formativo inexistente")
    void actualizarCampoInexistente() throws Exception{
        CampoRequestDto request = new CampoRequestDto("Arquitectura de APIs", "Diseño de servicios REST y lógica de servidor en Java.");

        when(campoFormativoService.actualizarCampo(request, 99l)).thenThrow(new NotFoundException("Campo Formativo no encontrado"));//cuando el controlador llame al servicio, mockito devolvera el dto de respuesta

        mockMvc.perform(put("/api/campo/{id}", 99L)//equivale a hacer POST /api/campo
                        .contentType(MediaType.APPLICATION_JSON)//convierte en dto a {"nombre": "Arquitectura de APIs",
                        .content(objectMapper.writeValueAsString(request)))//-------- "descripcion": ""Diseño de servicios REST y lógica de servidor en Java."}
                .andExpect(status().isNotFound());//verifica el status 404 Not Found

        verify(campoFormativoService).actualizarCampo(request, 99L);
    }

    @Test
    @DisplayName("Actualización de la descripción del campo formativo deshabilitado")
    void actualizarCampoDeshabilitado() throws Exception{
        CampoRequestDto request = new CampoRequestDto("Arquitectura de APIs", "Diseño de servicios REST y lógica de servidor en Java.");

        when(campoFormativoService.actualizarCampo(request, 1l)).thenThrow(new ResourceDisabledException("El campo formativo está deshabilitado"));//cuando el controlador llame al servicio, mockito devolvera el dto de respuesta

        mockMvc.perform(put("/api/campo/{id}", 1L)//equivale a hacer POST /api/campo
                        .contentType(MediaType.APPLICATION_JSON)//convierte en dto a {"nombre": "Arquitectura de APIs",
                        .content(objectMapper.writeValueAsString(request)))//-------- "descripcion": ""Diseño de servicios REST y lógica de servidor en Java."}
                .andExpect(status().isConflict());//verifica el status 409 conflict

        verify(campoFormativoService).actualizarCampo(request, 1L);
    }

    @Test
    @DisplayName("Habilitar/Deshabilitar un campo formativo")
    void habilitarDeshabilitar() throws Exception{
        CampoResponseDto response = new CampoResponseDto(1L,"Arquitectura de APIs", "Diseño de servicios REST y lógica de servidor en Java.", false);

        when(campoFormativoService.habitarDeshabilitar(1l)).thenReturn(response);//cuando el controlador llame al servicio, mockito devolvera el dto de respuesta

        mockMvc.perform(put("/api/campo/habilitarDeshabilitar/{id}", 1L)//equivale a hacer POST /api/campo
                        .contentType(MediaType.APPLICATION_JSON))//convierte en dto a {"nombre": "Arquitectura de APIs",
                .andExpect(status().isOk())//verifica el status 200 OK
                .andExpect(jsonPath("$.idCampo").value(1))//verifix EL JSON devuelto contenga {"idCampo":1}
                .andExpect(jsonPath("$.nombre").value("Arquitectura de APIs"));

        verify(campoFormativoService).habitarDeshabilitar(1L);
    }

    @Test
    @DisplayName("Habilitar/Deshabilitar un campo formativo no encontrado")
    void habilitarDeshabilitarCampoInexistente() throws Exception{
        when(campoFormativoService.habitarDeshabilitar(99l)).thenThrow(new NotFoundException("Campo Formativo no encontrado"));//cuando el controlador llame al servicio, mockito devolvera el dto de respuesta

        mockMvc.perform(put("/api/campo/habilitarDeshabilitar/{id}", 99L)//equivale a hacer POST /api/campo
                        .contentType(MediaType.APPLICATION_JSON))//convierte en dto a {"nombre": "Arquitectura de APIs",
                .andExpect(status().isNotFound());//verifica el status 200 OK

        verify(campoFormativoService).habitarDeshabilitar(99L);
    }
}