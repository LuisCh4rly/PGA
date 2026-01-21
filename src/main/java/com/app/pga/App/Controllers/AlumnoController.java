package com.app.pga.App.Controllers;

import com.app.pga.App.Models.Dtos.AlumnoDto;
import com.app.pga.App.Models.Entities.Alumno;
import com.app.pga.App.Services.Implements.AlumnoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/alumno")
@Validated
public class AlumnoController {
    private final AlumnoService alumnoService;

    public AlumnoController(AlumnoService alumnoService){this.alumnoService=alumnoService; }

    //-----crear----
    @PostMapping
    public ResponseEntity<AlumnoDto>createAlumno (@Valid @RequestBody AlumnoDto alumnoDto, UriComponentsBuilder uriComponentsBuilder){
        AlumnoDto createAlumno = alumnoService.ccreateAlumno(alumnoDto);
        URI location = uriComponentsBuilder.path("/api/alumno/{id}")
                .buildAndExpand(createAlumno.getClass()).toUri();

        return ResponseEntity.created(location).body(createAlumno);
    }


    //----actualizar----
    @PutMapping("/{id}")
    public ResponseEntity<AlumnoDto>actualizarAlumno(@PathVariable @Min(1) Long id, @Valid @RequestBody AlumnoDto alumnoDto){
        AlumnoDto actualizarAlumno = alumnoService.actualizarAlumno(id, alumnoDto);
        return ResponseEntity.ok(actualizarAlumno);
    }


    //----consulta por id----
    @GetMapping("/{id}")
    public ResponseEntity<AlumnoDto>consultaID(@PathVariable @Min(1) Long id){
        return ResponseEntity.ok(alumnoService.findById(id));
    }


    //----consulta para activos---
    @GetMapping("/activos")
    public ResponseEntity<List<AlumnoDto>>consultaActivos(){
        return ResponseEntity.ok(alumnoService.findAllActivos());
    }

    //----consulta---
    @GetMapping
    public ResponseEntity<List<AlumnoDto>>consulta(){
        return ResponseEntity.ok(alumnoService.findAll());
    }

    //---activar desactivar----
    @PutMapping("/desactivar/{id}")
    public ResponseEntity<AlumnoDto>desactivarActivar(@PathVariable @Min(1) Long id){
        AlumnoDto alumnoDesAct = alumnoService.desactivarActivarAlumno(id);
        return ResponseEntity.ok(alumnoDesAct);
    }
}
