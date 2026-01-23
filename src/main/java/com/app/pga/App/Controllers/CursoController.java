package com.app.pga.App.Controllers;


import com.app.pga.App.Models.Dtos.ActividadBaseDto;
import com.app.pga.App.Models.Dtos.CursoDto;

import com.app.pga.App.Services.Interfaces.ICursoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/curso")
@RequiredArgsConstructor
class CursoController {
    private final ICursoService cursoService;
    @PostMapping
    public ResponseEntity<CursoDto> crearActividadBase (@RequestBody @Valid CursoDto dto){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(cursoService.crearCurso(dto));
    }
    @GetMapping("/{id}")
    public ResponseEntity<CursoDto> obtenerCurso(@PathVariable Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cursoService.obtenerCurso(id));
    }
    @GetMapping()
    public ResponseEntity<List<CursoDto>> obtenerCursos(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cursoService.obtenerCursosGeneral());
    }
    @GetMapping("/activos")
    public ResponseEntity<List<CursoDto>> obtenerCursosActivos(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cursoService.obtenerCursosActivos());
    }
    @PutMapping("/{id}")
    public ResponseEntity<CursoDto> actualizarCurso(@PathVariable Long id, @RequestBody CursoDto dto){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cursoService.actualizarCurso(dto,id));
    }
    @PutMapping("/habilitarDeshabilitar/{id}")
    public ResponseEntity<CursoDto> habilitarDeshabilitarCurso(@PathVariable Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cursoService.habitarDeshabilitar(id));
    }
    @PutMapping("/asignarActividades/{id}")
    public ResponseEntity<CursoDto>asignarActividadesCurso(@PathVariable Long id, @RequestBody List <ActividadBaseDto> actividadBaseDtos){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cursoService.asignarActividades(id, actividadBaseDtos));
    }
    @DeleteMapping("/{idCurso}/quitarActividades/{idActividad}")
    public ResponseEntity<Void>asignarActividadesCurso(@PathVariable Long idCurso, @PathVariable Long idActividad){
        cursoService.quitarActividades(idCurso, idActividad);
        return ResponseEntity
                .noContent()
                .build();
    }


}
