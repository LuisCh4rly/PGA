package com.app.pga.App.Controllers;

import com.app.pga.App.Models.Dtos.RequestDto.UsuarioRequestDto;
import com.app.pga.App.Models.Dtos.ResponseDto.UsuarioResponseDto;
import com.app.pga.App.Services.Implements.UsuarioService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
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
    public ResponseEntity<List<UsuarioResponseDto>>Consulta(){

        return ResponseEntity.ok(usuarioService.findAll());
    }


    //----Desactivar usuario----
    @PutMapping("/desactivar/{id}")
    public ResponseEntity<UsuarioResponseDto>desactivarUsuario(@PathVariable Long id){
        UsuarioResponseDto usuarioDesactivado = usuarioService.desactivarUsuario(id);
        return ResponseEntity.ok(usuarioDesactivado);
    }

}
