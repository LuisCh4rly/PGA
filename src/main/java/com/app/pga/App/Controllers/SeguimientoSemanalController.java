package com.app.pga.App.Controllers;

import com.app.pga.App.Models.Dtos.RequestDto.SemanaRequestDto;
import com.app.pga.App.Models.Dtos.ResponseDto.SeguimientoDashboardResponseDto;
import com.app.pga.App.Models.Dtos.ResponseDto.SemanaResponseDto;
import com.app.pga.App.Services.Interfaces.ISeguimientoSemanalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/seguimiento-semanal")
@RequiredArgsConstructor
class SeguimientoSemanalController {
    private final ISeguimientoSemanalService seguimientoSemanalService;

    @PostMapping()
    public ResponseEntity<SemanaResponseDto> crearSemana (@RequestBody SemanaRequestDto dto){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(seguimientoSemanalService.crearSeguimientoSemanal(dto));
    }

    @GetMapping("/{idInscripcion}/actual")
    public ResponseEntity<SeguimientoDashboardResponseDto> obtenerSemanaActual (@PathVariable Long idInscripcion){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(seguimientoSemanalService.obtenerSeguimientoSemanalActual(idInscripcion));
    }
    @GetMapping("/{idInscripcion}")
    public ResponseEntity<SeguimientoDashboardResponseDto> obtenerSemanasPorAlumno (@PathVariable Long idInscripcion){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(seguimientoSemanalService.obtenerSeguimientoSemanalActual(idInscripcion));
    }



}
