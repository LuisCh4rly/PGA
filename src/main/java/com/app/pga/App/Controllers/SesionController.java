package com.app.pga.App.Controllers;

import com.app.pga.App.Exception.NotFoundException;
import com.app.pga.App.Models.Dtos.*;
import com.app.pga.App.Models.Dtos.RequestDto.SesionRequestDto;
import com.app.pga.App.Models.Dtos.RequestDto.SesionUpdateDto;
import com.app.pga.App.Models.Dtos.ResponseDto.SesionDetalleAlumnoDto;
import com.app.pga.App.Models.Dtos.ResponseDto.SesionDetalletDto;
import com.app.pga.App.Models.Dtos.ResponseDto.SesionDto;
import com.app.pga.App.Models.Dtos.ResponseDto.SesiondocenteDto;
import com.app.pga.App.Models.Entities.Sesion;
import com.app.pga.App.Models.Enum.EstadoAsistencia;
import com.app.pga.App.Models.Filtros.SesionFiltro;
import com.app.pga.App.Models.Mappers.InscripcionMapper;
import com.app.pga.App.Repositories.ISesionAlumnoRepository;
import com.app.pga.App.Repositories.ISesionRepository;
import com.app.pga.App.Services.Implements.SesionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Tag(name = "Sesiones", description = "Endpoints para gestión de sesiones")
@RestController
@RequestMapping("/api/sesiones")
@Validated
class SesionController {

    private final SesionService sesionService;
    private final ISesionRepository iSesionRepository;
    private final ISesionAlumnoRepository iSesionAlumnoRepository;

    SesionController(SesionService sesionService, ISesionRepository iSesionRepository, ISesionAlumnoRepository iSesionAlumnoRepository) {
        this.sesionService = sesionService;
        this.iSesionRepository = iSesionRepository;
        this.iSesionAlumnoRepository = iSesionAlumnoRepository;
    }

    //crear
    @Operation(summary = "Crear sesión", description = "Crea una nueva sesión asociada a un grupo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sesión creada correctamente"),
            @ApiResponse(responseCode = "404", description = "Grupo no encontrado o no se encuentran inscripciones"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "409", description = "Sesion duplicada o Grupo deshabilitado")
    })
    @PostMapping("/grupo/{idGrupo}")
    public ResponseEntity<SesionDto> crearSesion(@Parameter(description = "ID del grupo", example = "1") @PathVariable Long idGrupo,  @Valid @RequestBody SesionRequestDto dto) {
        SesionDto sesion = sesionService.createSesion(idGrupo, dto);
        return new ResponseEntity<>(sesion, HttpStatus.CREATED);
    }

    //actualizar lista de inscripciones en alcance individual
    @Operation(
            summary = "Actualizar inscripciones de sesión",
            description = "Actualiza la lista de inscripciones asociadas a una sesión"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inscripciones actualizadas"),
            @ApiResponse(responseCode = "404", description = "Sesión no encontrada"),
            @ApiResponse(responseCode = "409", description = "Accion restringida")
    })
    @PutMapping("/{idSesion}/inscripciones")
    public ResponseEntity<SesionDetalletDto> actualizarInscripciones(@Parameter(description = "ID de la sesión", example = "3") @PathVariable Long idSesion, @RequestBody List<Long> idsInscripcion) {
        SesionDetalletDto sesion = sesionService.actualizarInscripcionesSesion(idSesion, idsInscripcion);
        return ResponseEntity.ok(sesion);
    }
    //actualizar detalles de la sesion
    @Operation(summary = "Actualizar sesión", description = "Actualiza los detalles generales de una sesión")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sesión actualizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Sesión no encontrada o inscripcion no encontrada"),
            @ApiResponse(responseCode = "409", description = "Acción restringida")

    })
    @PutMapping("/actualizar/{idSesion}")
    public ResponseEntity<SesionDetalletDto> actualizarDetallesSesion(@Parameter(description = "ID de la sesión", example = "3")@PathVariable Long idSesion, @Valid @RequestBody SesionUpdateDto dto){
        return ResponseEntity.status(HttpStatus.OK)
                .body(sesionService.actualizarSesion(idSesion, dto));
    }

    //tomar asistencia
    @Operation(summary = "Tomar asistencia", description = "Registra la asistencia de los alumnos en una sesión")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Asistencia registrada correctamente"),
            @ApiResponse(responseCode = "404", description = "Sesión no encontrada")
    })
    @PostMapping("/{idSesion}/asistencia")
    public ResponseEntity<SesionDetalletDto> tomarAsistencia( @Parameter(description = "ID de la sesión", example = "10") @PathVariable Long idSesion, @Valid @RequestBody List<AsistenciaDto> listaAsistencia) {
        return ResponseEntity.ok(sesionService.tomarAsistencia(idSesion, listaAsistencia));
    }

    //detalles de asistencia por grupo
    @Operation(summary = "Consultar detalle de sesión", description = "Obtiene el detalle completo de una sesión")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Detalle obtenido correctamente"),
            @ApiResponse(responseCode = "404", description = "Sesión no encontrada")
    })
    @GetMapping("/asistencia/{idSesion}")
    public ResponseEntity<SesionDetalletDto>obtenerDetalleSesion( @Parameter(description = "ID de la sesión", example = "1") @PathVariable @Min(1) Long idSesion){
        return ResponseEntity.ok(sesionService.obtenerDetalleSesion(idSesion));
    }

    //consultar sesiones por grupo
    @Operation(summary = "Consultar sesiones por grupo", description = "Obtiene todas las sesiones asociadas a un grupo")
    @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente")

    @GetMapping("/porGrupo/{idGrupo}")
    public ResponseEntity<List<SesionDto>>obtenerSesionesPorGrupo(@Parameter(description = "ID del grupo", example = "2")@PathVariable @Min(1) Long idGrupo){
        return ResponseEntity.status(HttpStatus.OK)
                .body(sesionService.obtenerSesionesPorGrupo(idGrupo));
    }


    //consultar sesiones por docente
    @Operation(summary = "Consultar sesiones por docente", description = "Obtiene las sesiones asociadas a un docente")
    @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente")

    @GetMapping("/porDocente/{idDocente}")
    public ResponseEntity<List<SesiondocenteDto>>obtenerSesionesPorDocente(
            @Parameter(description = "ID de docente", example = "8")@PathVariable @Min(1) Long idDocente){
        return ResponseEntity.status(HttpStatus.OK)
                .body(sesionService.obtenerSesionPorDocente(idDocente));
    }


    //consultar sesiones por alumno
    @Operation(summary = "Consultar sesiones por alumno", description = "Obtiene las sesiones asociadas a un alumno")
    @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente")
    @GetMapping("/porAlumno/{idInscripcion}")
    public ResponseEntity<List<SesiondocenteDto>>obtenerSesionesPorAlumno(
            @Parameter(description = "ID de inscripción", example = "8")@PathVariable @Min(1) Long idInscripcion){
        return ResponseEntity.status(HttpStatus.OK)
                .body(sesionService.obtenerSesionPorAlumno(idInscripcion));
    }

    @Operation(summary = "Reporte de asistencia por grupo", description = "Obtiene el reporte de asistencia de un grupo")
    @ApiResponse(responseCode = "200", description = "Reporte generado correctamente")

    @GetMapping("/grupos/asistencias/{idGrupo}")
    public ResponseEntity<List<ReporteAsistenciaGrupoDto>> reporteAsistenciaGrupo(
            @Parameter(description = "ID del grupo", example = "2") @PathVariable Long idGrupo) {
        return ResponseEntity.ok(sesionService.obtenerReporteAsistenciaPorGrupo(idGrupo));
    }

    @Operation(summary = "Consultar sesiones filtradas por docente", description = "Obtiene sesiones paginadas y filtradas de un docente")
    @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente")
    @GetMapping("/docente/{id}/sesiones")
    public ResponseEntity<Page<SesiondocenteDto>> listar(
            @Parameter(description = "ID del docente", example = "1") @PathVariable Long id,
            @ParameterObject SesionFiltro filtro,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "6") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(sesionService.obtenerSesionesFiltradas(id, filtro, pageable));
    }


    @Operation(summary = "Consultar sesiones filtradas por alumno", description = "Obtiene sesiones paginadas y filtradas de un alumno")
    @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente")
    @GetMapping("/alumno/{id}/sesiones")
    public ResponseEntity<Page<SesionDetalleAlumnoDto>> alumnoListar(
            @Parameter(description = "ID del alumno", example = "1") @PathVariable Long id,
            @ParameterObject SesionFiltro filtro,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "6") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(sesionService.obtenerSesionesFiltradasAlumno(id, filtro, pageable));
    }
}
