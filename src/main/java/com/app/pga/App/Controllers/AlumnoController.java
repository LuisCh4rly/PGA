package com.app.pga.App.Controllers;

import com.app.pga.App.Models.Dtos.RequestDto.AlumnoRequestDto;
import com.app.pga.App.Models.Dtos.ResponseDto.AlumnoResponseDto;
import com.app.pga.App.Services.Implements.AlumnoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
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
   /* @PostMapping
    public ResponseEntity<AlumnoResponseDto>createAlumno (@Valid @RequestBody AlumnoRequestDto alumnoRequestDto, UriComponentsBuilder uriComponentsBuilder){
        AlumnoResponseDto createAlumno = alumnoService.createAlumno(alumnoRequestDto);
        URI location = uriComponentsBuilder.path("/api/alumno/{id}")
                .buildAndExpand(createAlumno.getClass()).toUri();

        return ResponseEntity.created(location).body(createAlumno);
    }*/


    //----actualizar----
    @PutMapping("/{id}")
    public ResponseEntity<AlumnoResponseDto>actualizarAlumno(@PathVariable @Min(1) Long id, @Valid @RequestBody AlumnoRequestDto alumnoRequestDto){
        AlumnoResponseDto actualizarAlumno = alumnoService.actualizarAlumno(id, alumnoRequestDto);
        return ResponseEntity.ok(actualizarAlumno);
    }


    //----consulta por id----
    @GetMapping("/{id}")
    public ResponseEntity<AlumnoResponseDto>consultaID(@PathVariable @Min(1) Long id){
        return ResponseEntity.ok(alumnoService.findById(id));
    }


    //----consulta para activos---
    @GetMapping("/activos")
    public ResponseEntity<List<AlumnoResponseDto>>consultaActivos(){
        return ResponseEntity.ok(alumnoService.findAllActivos());
    }

    //----consulta---
    @GetMapping
    public ResponseEntity<List<AlumnoResponseDto>>consulta(){
        return ResponseEntity.ok(alumnoService.findAll());
    }

    //---activar desactivar----
    @PutMapping("/desactivar/{id}")
    public ResponseEntity<AlumnoResponseDto>desactivarActivar(@PathVariable @Min(1) Long id){
        AlumnoResponseDto alumnoDesAct = alumnoService.desactivarActivarAlumno(id);
        return ResponseEntity.ok(alumnoDesAct);
    }
}
