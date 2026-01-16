package com.app.pga.App.Controllers;

import com.app.pga.App.Models.Dtos.UsuarioDto;
import com.app.pga.App.Services.Implements.UsuarioService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.hibernate.annotations.Parameter;
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
    @PostMapping
    public ResponseEntity<UsuarioDto>createUsuario(@Valid @RequestBody UsuarioDto usuarioDto, UriComponentsBuilder uriComponentsBuilder){

        UsuarioDto createUsuario = usuarioService.createUsuario(usuarioDto);
        URI location = uriComponentsBuilder.path("/api/usuario/{id}").buildAndExpand(createUsuario.getClass()).toUri();

        return ResponseEntity.created(location).body(createUsuario);
    }


    //---Actualizar---
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDto>actualizarUsuario(@PathVariable @Min(1) Long id, @Valid @RequestBody UsuarioDto usuarioDto){
        UsuarioDto actualizarUsuario = usuarioService.actualizarUsuario(id, usuarioDto);
        return ResponseEntity.ok(actualizarUsuario);
    }

    //---Consulta por id---
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDto>consultaID (@PathVariable @Min(1) Long id){
        return ResponseEntity.ok(usuarioService.findById(id));
    }


    //----Consulta general para activos----
    @GetMapping("/activos")
    public ResponseEntity<List<UsuarioDto>>ConsultaActivos(){
        return ResponseEntity.ok(usuarioService.findAllActivos());
    }


    //----Consulta general----
    @GetMapping
    public ResponseEntity<List<UsuarioDto>>Consulta(){
        return ResponseEntity.ok(usuarioService.findAll());
    }


    //----Desactivar usuario----
    @PutMapping("/desactivar/{id}")
    public ResponseEntity<UsuarioDto>desactivarUsuario(@PathVariable Long id){
        UsuarioDto usuarioDesactivado = usuarioService.desactivarUsuario(id);
        return ResponseEntity.ok(usuarioDesactivado);
    }

}
