package com.app.pga.App.Controllers;

import com.app.pga.App.Models.Dtos.RequestDto.SemanaRequestDto;
import com.app.pga.App.Models.Dtos.ResponseDto.SeguimientoDashboardResponseDto;
import com.app.pga.App.Models.Dtos.ResponseDto.SemanaResponseDto;
import com.app.pga.App.Services.Interfaces.ISeguimientoSemanalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Seguimiento semanal", description = "Enpoints para la gestion del seguimiento semanal del alumno")
@RestController
@RequestMapping("/api/seguimiento-semanal")
@RequiredArgsConstructor
class SeguimientoSemanalController {
    private final ISeguimientoSemanalService seguimientoSemanalService;

    @Operation(summary = "Crear semana del alumno", description = "Permite crear la semana en al que el alumno puede registrar las actividades a realizar.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Semana creada correctamente"),
            @ApiResponse(responseCode = "404", description = "Inscripcion no encontrada, "),
            @ApiResponse(responseCode = "409", description = "Inscripcion deshabilitada o semana ya registrada"),
    })
    @PostMapping("/{idInscripcion}")
    public ResponseEntity<SemanaResponseDto> crearSemana (@PathVariable Long idInscripcion){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(seguimientoSemanalService.crearSeguimientoSemanal(idInscripcion));
    }

    @Operation(summary = "Consultar seguimiento semanal actual", description = "Obtiene el seguimiento semanal actual asociado a una inscripción.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente"),
            @ApiResponse(responseCode = "204", description = "No existe seguimiento semanal actual"),
            @ApiResponse(responseCode = "404", description = "Inscripción no encontrada")
    })

    @GetMapping("/{idInscripcion}/actual")
    public ResponseEntity<SeguimientoDashboardResponseDto> obtenerSemanaActual (
            @Parameter(description = "Id de Inscripción", example = "1") @PathVariable Long idInscripcion){

        SeguimientoDashboardResponseDto semana =  seguimientoSemanalService.obtenerSeguimientoSemanalActual(idInscripcion);
        if (semana == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(semana);
    }

    @Operation(summary = "Consultar seguimientos semanales por alumno", description = "Obtiene el historial de seguimientos semanales asociados a una inscripción.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Inscripción no encontrada")
    })
    @GetMapping("/{idInscripcion}")
    public ResponseEntity<List<SeguimientoDashboardResponseDto>> obtenerSemanasPorAlumno (
            @Parameter(description = "Id de Inscripción", example = "1") @PathVariable Long idInscripcion){

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(seguimientoSemanalService.obtenerSeguimientoAlumno(idInscripcion));
    }



}
