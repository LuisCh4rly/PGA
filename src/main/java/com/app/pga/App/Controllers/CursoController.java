package com.app.pga.App.Controllers;


import com.app.pga.App.Models.Dtos.RequestDto.AgregarActividadesCursoDto;
import com.app.pga.App.Models.Dtos.RequestDto.CursoRequestDto;
import com.app.pga.App.Models.Dtos.ResponseDto.CursoResponseDto;
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
    public ResponseEntity<CursoResponseDto> crearActividadBase (@RequestBody @Valid CursoRequestDto dto){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(cursoService.crearCurso(dto));
    }
    @GetMapping("/{id}")
    public ResponseEntity<CursoResponseDto> obtenerCurso(@PathVariable Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cursoService.obtenerCurso(id));
    }
    @GetMapping()
    public ResponseEntity<List<CursoResponseDto>> obtenerCursos(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cursoService.obtenerCursosGeneral());
    }
    @GetMapping("/activos")
    public ResponseEntity<List<CursoResponseDto>> obtenerCursosActivos(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cursoService.obtenerCursosActivos());
    }
    @PutMapping("/{id}")
    public ResponseEntity<CursoResponseDto> actualizarCurso(@PathVariable Long id, @RequestBody CursoRequestDto dto){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cursoService.actualizarCurso(dto,id));
    }
    @PutMapping("/habilitarDeshabilitar/{id}")
    public ResponseEntity<CursoResponseDto> habilitarDeshabilitarCurso(@PathVariable Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cursoService.habitarDeshabilitar(id));
    }
    @PutMapping("{id}/asignarActividades")
    public ResponseEntity<CursoResponseDto>asignarActividadesCurso(@PathVariable Long id, @RequestBody AgregarActividadesCursoDto agregarActividadesCursoDto){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cursoService.asignarActividades(id, agregarActividadesCursoDto));
    }
    @DeleteMapping("/{idCurso}/quitarActividades/{idActividad}")
    public ResponseEntity<Void>asignarActividadesCurso(@PathVariable Long idCurso, @PathVariable Long idActividad){
        cursoService.quitarActividades(idCurso, idActividad);
        return ResponseEntity
                .noContent()
                .build();
    }


}
