package com.app.pga.App.Controllers;

import com.app.pga.App.Models.Dtos.RequestDto.UsuarioRequestDto;
import com.app.pga.App.Models.Dtos.ResponseDto.UsuarioResponseDto;
import com.app.pga.App.Models.Filtros.UsuarioFiltro;
import com.app.pga.App.Services.Implements.UsuarioService;
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
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto>actualizarUsuario(@PathVariable @Min(1) Long id, @Valid @RequestBody UsuarioRequestDto usuarioRequestDto){
        UsuarioResponseDto actualizarUsuario = usuarioService.actualizarUsuario(id, usuarioRequestDto);
        return ResponseEntity.ok(actualizarUsuario);
    }

    //---Consulta por id---
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto>consultaID (@PathVariable @Min(1) Long id){
        return ResponseEntity.ok(usuarioService.findById(id));
    }


    //----Consulta general para activos----
    @GetMapping("/activos")
    public ResponseEntity<List<UsuarioResponseDto>>ConsultaActivos(){
        return ResponseEntity.ok(usuarioService.findAllActivos());
    }


    //----Consulta general----
    @GetMapping
    public ResponseEntity<Page<UsuarioResponseDto>>Consulta(UsuarioFiltro filtro, Pageable pageable){
        return ResponseEntity.ok(usuarioService.findAll(filtro, pageable));
    }


    //----Desactivar usuario----
    @PutMapping("/desactivar/{id}")
    public ResponseEntity<UsuarioResponseDto>desactivarUsuario(@PathVariable Long id){
        UsuarioResponseDto usuarioDesactivado = usuarioService.desactivarUsuario(id);
        return ResponseEntity.ok(usuarioDesactivado);
    }
    //-----------DOCENTES--------

    //-------consulta DOCENTES ACTIVOS
    @GetMapping("/docentes-activos")
    public ResponseEntity<List<UsuarioResponseDto>>consultaDocentesActivos(){
        return ResponseEntity.ok(usuarioService.findAllActivosDocentes());
    }


    //-------consulta ALUMNOS ACTIVOS
    @GetMapping("/alumnos-activos")
    public ResponseEntity<List<UsuarioResponseDto>>consultaAlumnosActivos(){
        return ResponseEntity.ok(usuarioService.findAllActivosAlumnos());
    }


}
