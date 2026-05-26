package com.app.pga.App.Controllers;

import com.app.pga.App.Models.Dtos.ActividadAlumnoDto;
import com.app.pga.App.Models.Dtos.ReporteSeguimientoDto;
import com.app.pga.App.Models.Dtos.RequestDto.CambiarEstadoTareaDto;
import com.app.pga.App.Models.Dtos.ResponseDto.ActividadALumnoListaDto;
import com.app.pga.App.Models.Dtos.ResponseDto.ActividadAlumnoResponseDTO;
import com.app.pga.App.Models.Enum.EstadoTarea;
import com.app.pga.App.Services.Implements.ActividadAlumnoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Actividades de Alumno", description = "Gestión de actividades, entregas y seguimiento de alumnos")
@RestController
@RequestMapping("/api/actividades-alumnos")
@RequiredArgsConstructor
public class ActividadAlumnoController {
    private final ActividadAlumnoService actividadAlumnoService;

    @Operation(summary = "Entregar actividad", description = "Permite al alumno subir la entrega de una actividad.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Entrega realizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Actividad no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error subiendo el archivo")
    })
    @PostMapping("/{id}/entrega")
    public ResponseEntity<Void> entregarActividad(
            @Parameter(description = "Id de Actividad Alumno", example = "1") @PathVariable Long id,
            @RequestParam MultipartFile archivo) {
        actividadAlumnoService.entregarActividad(id, archivo);
        return ResponseEntity.ok().build();
    }
    @Operation(summary = "Consultar actividades por inscripción", description = "Obtiene las actividades asociadas a una inscripción.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Inscripción no encontrada")
    })
    @GetMapping("/inscripcion/{idInscripcion}")
    public ResponseEntity<List<ActividadALumnoListaDto>> obtenerActividadesAlumno(
            @Parameter(description = "Id de Inscripción", example = "1") @PathVariable Long idInscripcion) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(actividadAlumnoService.obtenerPorInscripcion(idInscripcion));
    }
    @Operation(summary = "Consultar actividad de alumno", description = "Obtiene el detalle de una actividad específica de un alumno.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Actividad o inscripción no encontrada")
    })
    @GetMapping("/inscripcion/{idInscripcion}/{idActividadAlumno}")
    public ResponseEntity<ActividadAlumnoDto> obtenerActividadAlumno(
            @Parameter(description = "Id de Inscripción", example = "1") @PathVariable Long idInscripcion,
            @Parameter(description = "Id de Actividad Alumno", example = "1")@PathVariable Long idActividadAlumno) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(actividadAlumnoService.obtenerPorInscripcionYActividad(idInscripcion, idActividadAlumno));
    }
    @Operation(summary = "Agregar observación", description = "Permite agregar observaciones a una actividad entregada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Observación agregada correctamente"),
            @ApiResponse(responseCode = "404", description = "Actividad no encontrada")
    })
    @PutMapping("/{id}/observacion")
    public ResponseEntity<Void> agregarObservacion(
            @Parameter(description = "Id de Actividad Alumno", example = "1") @PathVariable Long id,
            @RequestBody CambiarEstadoTareaDto dto) {
        actividadAlumnoService.agregarObservacion(id, dto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Exentar actividad", description = "Marca una actividad como exentada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Actividad exentada correctamente"),
            @ApiResponse(responseCode = "404", description = "Actividad no encontrada")
    })
    @PutMapping("/{id}/exentar")
    public ResponseEntity<Void> exentarActividad(
            @Parameter(description = "Id de Actividad Alumno", example = "1") @PathVariable Long id, @RequestBody CambiarEstadoTareaDto dto) {
        actividadAlumnoService.exentarActividad(id, dto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Cambiar estado de actividad", description = "Actualiza el estado de una actividad de alumno.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Actividad no encontrada")
    })
    @PutMapping("/{id}/estado")
    public ResponseEntity<Void> cambiarEstado(
            @Parameter(description = "Id de Actividad Alumno", example = "1") @PathVariable Long id,
            @RequestBody CambiarEstadoTareaDto dto) {
        actividadAlumnoService.cambiarEstado(id, dto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Descargar entrega", description = "Permite visualizar o descargar el archivo entregado por el alumno.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Archivo obtenido correctamente"),
            @ApiResponse(responseCode = "404", description = "Entrega no encontrada"),
            @ApiResponse(responseCode = "500", description = "Problema al cargar el archivo")
    })
    @GetMapping("/{id}/entrega")
    public ResponseEntity<Resource> verEntrega(
            @Parameter(description = "Id de Actividad Alumno", example = "1")  @PathVariable Long id) {
        Resource archivo = actividadAlumnoService.visualizarEntrega(id);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + archivo.getFilename() + "\"")
                .body(new InputStreamResource(archivo));
    }
    @Operation(summary = "Reporte de seguimiento por grupo", description = "Obtiene el reporte de seguimiento de actividades de un grupo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Grupo no encontrado")
    })
    @GetMapping("/grupos/seguimiento/{idGrupo}")
    public ResponseEntity<List<ReporteSeguimientoDto>> reporteSeguimientoGrupo(
            @Parameter(description = "Id de Grupo", example = "1") @PathVariable Long idGrupo) {
        return ResponseEntity.ok(actividadAlumnoService.obtenerReporteSeguimientoPorGrupo(idGrupo));
    }


    @Operation(summary = "Consultar asignaciones de actividad", description = "Obtiene las asignaciones de alumnos asociadas a una actividad grupal.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Actividad no encontrada")
    })
    @GetMapping("/actividad/{idActividadGrupo}")
    public ResponseEntity<List<ActividadAlumnoResponseDTO>> verAsignaciones(
            @Parameter(description = "Id de Actividad Grupo", example = "1") @PathVariable Long idActividadGrupo) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(actividadAlumnoService.verAsignaciones(idActividadGrupo));
    }

    @Operation(summary = "Consultar actividades disponibles", description = "Obtiene las actividades disponibles asociadas a una inscripción.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Inscripción no encontrada")
    })
    @GetMapping("/inscripcion/{idInscripcion}/disponibles")
    public ResponseEntity<List<ActividadALumnoListaDto>> obtenerActividadesAlumnoDisponibles(
            @Parameter(description = "Id de Inscripción", example = "1") @PathVariable Long idInscripcion) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(actividadAlumnoService.obtenerPorInscripcionDisponibles(idInscripcion));
    }
}
