package com.app.pga.App.Controllers;

import com.app.pga.App.Models.Dtos.RequestDto.ObservacionesExpedienteDto;
import com.app.pga.App.Models.Dtos.ResponseDto.ExpedienteReporteDto;
import com.app.pga.App.Models.Dtos.ResponseDto.ExpedienteResponseDto;
import com.app.pga.App.Models.Dtos.ResponseDto.InscripcionReporteDto;
import com.app.pga.App.Services.Interfaces.IExpedienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(name = "Expedientes", description = "Enpoints para la gestion del expediente de los alumnos")
@RestController
@RequestMapping("/api/expedientes")
@RequiredArgsConstructor
class ExpedienteController {

    private final IExpedienteService expedienteService;

    @Operation(summary = "Cosultar el expediente del alumno",
            description = "Trae la información del expediente y la información de los documntos que lo conforman"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Alumno no encontrado"),
            @ApiResponse(responseCode = "409", description = "Alumno deshabilitado")
    })

    @GetMapping("alumnos/{idAlumno}")
    public ResponseEntity<ExpedienteResponseDto> obtenerExpedienteporAlumno(
            @Parameter(description = "Id del alumno (usuario)", example = "6") @PathVariable Long idAlumno) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(expedienteService.verExpediente(idAlumno));
    }
    @Operation(summary = "Agrega observaciones del expediente", description = "Actualiza las observaciones generales del expediente del alumno")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Actualización realizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Expediente no encontrado"),
            @ApiResponse(responseCode = "409", description = "Expediente aprobado o alumno deshabilitado")
    })

    @PutMapping("/{idExpediente}/observaciones")
    public ResponseEntity< ExpedienteResponseDto> agregarObservaciones (
            @Parameter(description = "Id del Expediente", example = "1")@PathVariable Long idExpediente ,
                                                                       @RequestBody @Valid ObservacionesExpedienteDto dto){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(expedienteService.agregarObservaciones(idExpediente, dto.observacion()));
    }


}
