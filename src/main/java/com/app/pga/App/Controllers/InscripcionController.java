package com.app.pga.App.Controllers;

import com.app.pga.App.Models.Dtos.InscripcionDto;
import com.app.pga.App.Services.Implements.InscripcionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/inscripcion")
@Validated
public class InscripcionController {
    private final InscripcionService inscripcionService;

    public InscripcionController(InscripcionService inscripcionService){this.inscripcionService=inscripcionService;}

    //---crear---
    @PostMapping
    public ResponseEntity<InscripcionDto>createInscripcion(@Valid @RequestBody InscripcionDto inscripcionDto, UriComponentsBuilder uriComponentsBuilder){
        InscripcionDto createInscripcion = inscripcionService.createInscripcion(inscripcionDto);
        URI location = uriComponentsBuilder.path("/api/inscripcion/{id}")
                .buildAndExpand(createInscripcion.getClass()).toUri();
        return ResponseEntity.created(location).body(createInscripcion);
    }

    //---consulta por id---
    @GetMapping("/{id}")
    public ResponseEntity<InscripcionDto>consultaId(@PathVariable @Min(1) Long id){
        return ResponseEntity.ok(inscripcionService.finfById(id));
    }

    //---consulta activos---
    @GetMapping("/activos")
    public ResponseEntity<List<InscripcionDto>> consultaActivos(){
        return ResponseEntity.ok(inscripcionService.findAllActivos());
    }

    //---consulta ---
    @GetMapping
    public ResponseEntity<List<InscripcionDto>> listarResumen() {
        return ResponseEntity.ok(inscripcionService.findAll());
    }

    //---consulta por id---
    @PutMapping("/desactivar/{id}")
    public ResponseEntity<InscripcionDto>desactivarActivar(@PathVariable @Min(1) Long id){
        InscripcionDto inscripcionDes = inscripcionService.desactivarActivarInscripcion(id);
        return ResponseEntity.ok(inscripcionDes);
    }
    @PutMapping("/asignarGrupo")
    public ResponseEntity<InscripcionDto>asignarGrupo(@RequestBody InscripcionDto dto){
        return ResponseEntity.ok(inscripcionService.asignarGrupo(dto));
    }
}
