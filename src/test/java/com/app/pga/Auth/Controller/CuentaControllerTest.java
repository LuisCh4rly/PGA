package com.app.pga.Auth.Controller;

import com.app.pga.App.Exception.NotFoundException;
import com.app.pga.Auth.JWT.JWTService;
import com.app.pga.Auth.Models.Dtos.Response.CuentaResponseDto;
import com.app.pga.Auth.Security.UserDetailsServiceImp;
import com.app.pga.Auth.Service.CuentaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.web.servlet.support.WebContentGenerator;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CuentaController.class)
class CuentaControllerTest {
    @Autowired
    private MockMvc mockMvc; // simular peticiones HTTP

    @Autowired
    private ObjectMapper objectMapper; // convertir objetos Java a JSON

    @MockitoBean
    private CuentaService cuentaService; //simular la capa de servicio
    @Autowired
    private WebContentGenerator webContentGenerator;
    @MockitoBean
    private JWTService jwtService;
    @MockitoBean
    private UserDetailsServiceImp userDetailsServiceImp;

    @BeforeEach
    void setUp() {

    }

    @Test
    @DisplayName("Dado que consulto las cuentas del sistema, entonces debería retornar una lista de cuentas con estado 200 OK")
    void listarCuentas() throws Exception {
        //dado
        CuentaResponseDto cuenta1 = new CuentaResponseDto(
                1L,
                1L,
                "Luis",
                "Cruz",
                "Ibarra",
                "Luis_Charly10@outlook.com",
                true,
                "ALUMNO");
        CuentaResponseDto cuenta2 = new CuentaResponseDto(
                2L,
                2L,
                "Carlos",
                "Cruz",
                "Mendo",
                "Charly06@gmail.com",
                true,
                "DOCENTE");

        List<CuentaResponseDto> listaSimulada = List.of(cuenta1, cuenta2);
        when ( cuentaService.listarCuentas()).thenReturn(listaSimulada);
        //cuando y entonces
        mockMvc.perform(
                        get("/api/cuentas")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Verifica que el HTTP Status sea 200 OK
                .andExpect( jsonPath("$.size()").value(2))
                .andExpect( jsonPath("$[0].email").value("Luis_Charly10@outlook.com"))
                .andExpect( jsonPath("$[1].rol").value("DOCENTE")
                );
        verify(cuentaService, times(1)).listarCuentas();
    }
    @Test
    @DisplayName("Dado quue el sistema no tiene cuentas registradas, entonces debería retornar una lista vacía con estado 200 OK")
    void listarCuentas_DeberiaRetornarListaVacia() throws Exception {
        when(cuentaService.listarCuentas()).thenReturn(List.of());
        mockMvc.perform(get("/api/cuentas")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
        verify(cuentaService,times(1)).listarCuentas();
    }



    @Test
    @DisplayName("Dado que busco una cuenta existente por id, entonces ebería retornar la información de la cuenta con estado 200 OK")
    void cuentaById() throws  Exception {
        CuentaResponseDto cuentaSimulada = new CuentaResponseDto(
                1L,
                1L,
                "Luis",
                "Cruz",
                "Ibarra",
                "Luis_Charly10@outlook.com",
                true,
                "ALUMNO");

        when(cuentaService.cuentaById(1L)).thenReturn(cuentaSimulada);

        //cuando y entonces
        mockMvc.perform(
                        get("/api/cuentas/{id}", 1L)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Verifica que el HTTP Status sea 200 OK
                .andExpect( jsonPath("$.idCuenta").value(1L))
                .andExpect( jsonPath("$.email").value("Luis_Charly10@outlook.com"))
                .andExpect( jsonPath("$.rol").value("ALUMNO")
                );
        verify(cuentaService, times(1)).cuentaById(1L);
    }

    @Test
    @DisplayName("Dado que busco una cuenta inexistente por Id, entonces debería retornar 404 Not Found")
    void cuentaById_NotFound() throws Exception {

        when(cuentaService.cuentaById(999L)).thenThrow(new NotFoundException("Cuenta No encontrada"));

        mockMvc.perform(get("/api/cuentas/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(cuentaService, times(1)).cuentaById(999L);
    }
}