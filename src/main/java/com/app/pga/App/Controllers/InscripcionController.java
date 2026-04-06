package com.app.pga.App.Controllers;

import com.app.pga.App.Models.Dtos.RequestDto.InscripcionRequestDto;
import com.app.pga.App.Models.Dtos.ResponseDto.AlumnoGrupoDto;
import com.app.pga.App.Models.Dtos.ResponseDto.InscripcionResponseDto;
import com.app.pga.App.Models.Filtros.InscripcionFiltro;
import com.app.pga.App.Services.Implements.InscripcionService;
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
@RequestMapping("/api/inscripcion")
@Validated
public class InscripcionController {
    private final InscripcionService inscripcionService;

    public InscripcionController(InscripcionService inscripcionService){this.inscripcionService=inscripcionService;}

    //---crear---
    @PostMapping
    public ResponseEntity<InscripcionResponseDto>createInscripcion(@Valid @RequestBody InscripcionRequestDto inscripcionDto, UriComponentsBuilder uriComponentsBuilder){
        InscripcionResponseDto createInscripcion = inscripcionService.createInscripcion(inscripcionDto);
        URI location = uriComponentsBuilder.path("/api/inscripcion/{id}")
                .buildAndExpand(createInscripcion.getClass()).toUri();
        return ResponseEntity.created(location).body(createInscripcion);
    }

    //---consulta por id---
    @GetMapping("/{id}")
    public ResponseEntity<InscripcionResponseDto>consultaId(@PathVariable @Min(1) Long id){
        return ResponseEntity.ok(inscripcionService.finfById(id));
    }

    //---consulta activos---
    @GetMapping("/activos")
    public ResponseEntity<List<InscripcionResponseDto>> consultaActivos(){
        return ResponseEntity.ok(inscripcionService.findAllActivos());
    }

    //-----consulta de alumnos por grupo
    @GetMapping("/grupo/{idGrupo}/alumnos")
    public ResponseEntity<List<AlumnoGrupoDto>> obtenerAlumnosPorGrupo(@PathVariable Long idGrupo){
        return ResponseEntity.ok(inscripcionService.obtenerAlumnosPorGrupo(idGrupo));
    }

    //-----consulta de alumnos por grupo general
    @GetMapping("/grupo/{idGrupo}/alumnos/general")
    public ResponseEntity<List<AlumnoGrupoDto>> obtenerAlumnosPorGrupoGeneral(@PathVariable Long idGrupo){
        return ResponseEntity.ok(inscripcionService.obtenerAlumnosPorGrupoGeneral(idGrupo));
    }

    //---consulta ---
    @GetMapping
    public ResponseEntity<Page<InscripcionResponseDto>> listar(InscripcionFiltro filtro, Pageable pageable) {
        return ResponseEntity.ok(inscripcionService.findAll(filtro, pageable));
    }

    //---consulta por id---
    @PutMapping("/desactivar/{id}")
    public ResponseEntity<InscripcionResponseDto>desactivarActivar(@PathVariable @Min(1) Long id){
        InscripcionResponseDto inscripcionDes = inscripcionService.desactivarActivarInscripcion(id);
        return ResponseEntity.ok(inscripcionDes);
    }
    @PutMapping("{idInscripcion}/asignarGrupo/{idGrupo}")
    public ResponseEntity<InscripcionResponseDto>asignarGrupo(@PathVariable @Min(1) Long idInscripcion,@PathVariable @Min(1) Long idGrupo){
        return ResponseEntity.ok(inscripcionService.asignarGrupo(idInscripcion,idGrupo));
    }
}
