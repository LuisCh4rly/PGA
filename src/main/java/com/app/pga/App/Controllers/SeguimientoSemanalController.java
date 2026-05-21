package com.app.pga.App.Controllers;

import com.app.pga.App.Models.Dtos.RequestDto.SemanaRequestDto;
import com.app.pga.App.Models.Dtos.ResponseDto.SeguimientoDashboardResponseDto;
import com.app.pga.App.Models.Dtos.ResponseDto.SemanaResponseDto;
import com.app.pga.App.Services.Interfaces.ISeguimientoSemanalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seguimiento-semanal")
@RequiredArgsConstructor
class SeguimientoSemanalController {
    private final ISeguimientoSemanalService seguimientoSemanalService;

    @PostMapping("/{idInscripcion}")
    public ResponseEntity<SemanaResponseDto> crearSemana (@PathVariable Long idInscripcion){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(seguimientoSemanalService.crearSeguimientoSemanal(idInscripcion));
    }

    @GetMapping("/{idInscripcion}/actual")
    public ResponseEntity<SeguimientoDashboardResponseDto> obtenerSemanaActual (@PathVariable Long idInscripcion){

        SeguimientoDashboardResponseDto semana =  seguimientoSemanalService.obtenerSeguimientoSemanalActual(idInscripcion);
        if (semana == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(semana);
    }
    @GetMapping("/{idInscripcion}")
    public ResponseEntity<List<SeguimientoDashboardResponseDto>> obtenerSemanasPorAlumno (@PathVariable Long idInscripcion){

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(seguimientoSemanalService.obtenerSeguimientoAlumno(idInscripcion));
    }



}
