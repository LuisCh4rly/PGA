package com.app.pga.App.Controllers;

import com.app.pga.App.Models.Dtos.RequestDto.DetalleSeguimientoAgrupadoDto;
import com.app.pga.App.Models.Dtos.RequestDto.DetalleSeguimientoRequestDto;
import com.app.pga.App.Models.Dtos.ResponseDto.DetalleDashboardDto;
import com.app.pga.App.Services.Interfaces.IDetalleSeguimientoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Detalle Seguimiento ", description = "Enpoints para la gestion de actividades semanales de un alumno")
@RestController
@RequestMapping("/api/detalles")
@RequiredArgsConstructor
@Transactional
class DetalleSeguimientoController {
    private final IDetalleSeguimientoService detalleSeguimientoService;

    @Operation(
            summary = "Crear detalle de seguimiento",
            description = "Permite registrar la actividad del alumno en su semana aberta "
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Detalle semanal regitrada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Actividad no perteneciente al alumno"),
            @ApiResponse(responseCode = "404", description = "Actividad no encontrada o Semana no encontrada"),
            @ApiResponse(responseCode = "409", description = "Actividad ya registrada o actividad exenta o actividad ya completada o semana deshabilitada")
    })
    @PostMapping()
    public ResponseEntity<List<DetalleDashboardDto>> crearDetalle (@RequestBody DetalleSeguimientoAgrupadoDto dto){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(detalleSeguimientoService.crearDetalleSeguimiento(dto));
    }

    @Operation(summary = "Actualizar los avances de la actividad", description = "Permite registrar los avances de la  actividad del alumno en su semana aberta ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Detalle semanal actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Avance ingresado o estado no permitido"),
            @ApiResponse(responseCode = "404", description = "detalle semanal no encontrado"),
            @ApiResponse(responseCode = "409", description = "Semana cerrada o Actividad no editable")
    })
    @PutMapping("/{idDetalle}")
    public ResponseEntity<DetalleDashboardDto> actualizarDetalle (
            @Parameter(description = "Id del detalle semanal", example= "1") @PathVariable Long idDetalle, @RequestBody DetalleSeguimientoRequestDto dto){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(detalleSeguimientoService.actualizarEstado(idDetalle, dto));
    }




}
