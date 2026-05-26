package com.app.pga.App.Controllers;

import com.app.pga.App.Models.Dtos.RequestDto.UsuarioRequestDto;
import com.app.pga.App.Models.Dtos.ResponseDto.UsuarioResponseDto;
import com.app.pga.App.Models.Filtros.UsuarioFiltro;
import com.app.pga.App.Services.Implements.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
@Tag(name = "Usuarios", description = "Endpoints para gestión de usuarios")
@RestController
@RequestMapping("/api/usuario")
@Validated
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService=usuarioService;
    }

    //-----Crear------
    /*@PostMapping
    public ResponseEntity<UsuarioResponseDto>createUsuario(@Valid @RequestBody UsuarioRequestDto usuarioRequestDto, UriComponentsBuilder uriComponentsBuilder){

        UsuarioResponseDto createUsuario = usuarioService.createUsuario(usuarioRequestDto);
        URI location = uriComponentsBuilder.path("/api/usuario/{id}").buildAndExpand(createUsuario.getClass()).toUri();

        return ResponseEntity.created(location).body(createUsuario);
    }
*/

    //---Actualizar---
    @Operation(summary = "Actualizar usuario", description = "Actualiza la información de un usuario mediante su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "409", description = "Accion restringida")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto>actualizarUsuario(@PathVariable @Min(1) Long id, @Valid @RequestBody UsuarioRequestDto usuarioRequestDto){
        UsuarioResponseDto actualizarUsuario = usuarioService.actualizarUsuario(id, usuarioRequestDto);
        return ResponseEntity.ok(actualizarUsuario);
    }

    //---Consulta por id---
    @Operation(summary = "Consultar usuario por Id", description = "Obtiene un usuario específico mediante su ID  ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto>consultaID (@PathVariable @Min(1) Long id){
        return ResponseEntity.ok(usuarioService.findById(id));
    }

    @Operation(summary = "Consultar usuarios activos", description = "Obtiene todos los usuarios activos")
    @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente")
    @GetMapping("/activos")
    public ResponseEntity<List<UsuarioResponseDto>>ConsultaActivos(){
        return ResponseEntity.ok(usuarioService.findAllActivos());
    }

    //----Consulta general----
    @Operation(summary = "Consultar usuarios", description = "Obtiene una lista paginada de usuarios con filtros")
    @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente")
    @GetMapping
    public ResponseEntity<Page<UsuarioResponseDto>>Consulta(UsuarioFiltro filtro, Pageable pageable){
        return ResponseEntity.ok(usuarioService.findAll(filtro, pageable));
    }


    //----Desactivar usuario----
    @Operation(summary = "Desactivar usuario", description = "Desactiva un usuario mediante su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario desactivado correctamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "409", description = "Accion restringida")
    })
    @PutMapping("/desactivar/{id}")
    public ResponseEntity<UsuarioResponseDto>desactivarUsuario(@PathVariable Long id){
        UsuarioResponseDto usuarioDesactivado = usuarioService.desactivarUsuario(id);
        return ResponseEntity.ok(usuarioDesactivado);
    }
    //-----------DOCENTES--------

    //-------consulta DOCENTES ACTIVOS
    @Operation(summary = "Consultar docentes activos", description = "Obtiene todos los docentes activos")
    @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente")
    @GetMapping("/docentes-activos")
    public ResponseEntity<List<UsuarioResponseDto>>consultaDocentesActivos(){
        return ResponseEntity.ok(usuarioService.findAllActivosDocentes());
    }


    //-------consulta ALUMNOS ACTIVOS
    @Operation(summary = "Consultar alumnos activos", description = "Obtiene todos los alumnos activos")
    @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente")
    @GetMapping("/alumnos-activos")
    public ResponseEntity<List<UsuarioResponseDto>>consultaAlumnosActivos(){
        return ResponseEntity.ok(usuarioService.findAllActivosAlumnos());
    }


}
