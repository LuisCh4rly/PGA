package com.app.pga.App.Controllers;

import com.app.pga.App.Models.Dtos.RequestDto.InscripcionRequestDto;
import com.app.pga.App.Models.Dtos.ResponseDto.*;
import com.app.pga.App.Models.Filtros.InscripcionFiltro;
import com.app.pga.App.Services.Implements.InscripcionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import java.util.Optional;

@Tag(name = "Inscripciones", description = "Endpoints para la gestion de inscripciones del alumno")
@RestController
@RequestMapping("/api/inscripcion")
@Validated
public class InscripcionController {
    private final InscripcionService inscripcionService;

    public InscripcionController(InscripcionService inscripcionService){this.inscripcionService=inscripcionService;}

    //---crear---
    @Operation(summary = "Crear inscripción", description = "Registra una nueva inscripción para un alumno.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Inscripción creada correctamente"),
            @ApiResponse(responseCode = "404", description = "Alumno no encontrado"),
            @ApiResponse(responseCode = "409", description = "El alumno ya cuenta con una inscripción activa")
    })

    @PostMapping
    public ResponseEntity<InscripcionResponseDto>createInscripcion(@Valid @RequestBody InscripcionRequestDto inscripcionDto, UriComponentsBuilder uriComponentsBuilder){
        InscripcionResponseDto createInscripcion = inscripcionService.createInscripcion(inscripcionDto);
        URI location = uriComponentsBuilder.path("/api/inscripcion/{id}")
                .buildAndExpand(createInscripcion.getClass()).toUri();
        return ResponseEntity.created(location).body(createInscripcion);
    }

    //---consulta por id---
    @Operation(summary = "Consultar inscripción por ID", description = "Obtiene la información de una inscripción específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Inscripción no encontrada")
    })

    @GetMapping("/{id}")
    public ResponseEntity<InscripcionResponseDto>consultaId(
            @Parameter(description = "Id de la Inscripción") @PathVariable @Min(1) Long id){
        return ResponseEntity.ok(inscripcionService.finfById(id));
    }

    //---consulta activos---
    @Operation(summary = "Consultar inscripciones activas", description = "Obtiene todas las inscripciones activas registradas.")
    @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente")
    @GetMapping("/activos")
    public ResponseEntity<List<InscripcionResponseDto>> consultaActivos(){
        return ResponseEntity.ok(inscripcionService.findAllActivos());
    }

    @Operation(summary = "Listar inscripciones", description = "Obtiene un listado paginado de inscripciones aplicando filtros.")
    @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente")
    @GetMapping
    public ResponseEntity<Page<InscripcionResponseDto>> listar(InscripcionFiltro filtro, Pageable pageable) {
        return ResponseEntity.ok(inscripcionService.findAll(filtro, pageable));
    }
    @Operation( summary = "Activar o desactivar inscripción", description = "Cambia el estado de una inscripción.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Inscripción no encontrada"),
            @ApiResponse(responseCode = "409", description = "Conflicto con el estado de la inscripción")
    })
    @PutMapping("/desactivar/{id}")
    public ResponseEntity<InscripcionResponseDto>desactivarActivar(
            @Parameter(description = "Id de la Inscripción")@PathVariable @Min(1) Long id){
        InscripcionResponseDto inscripcionDes = inscripcionService.desactivarActivarInscripcion(id);
        return ResponseEntity.ok(inscripcionDes);
    }

    @Operation(summary = "Asignar grupo a inscripción", description = "Asigna un grupo a una inscripción activa.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grupo asignado correctamente"),
            @ApiResponse(responseCode = "404", description = "Inscripción o grupo no encontrado"),
            @ApiResponse(responseCode = "409", description = "El alumno ya pertenece al grupo")
    })
    @PutMapping("{idInscripcion}/asignarGrupo/{idGrupo}")
    public ResponseEntity<InscripcionResponseDto>asignarGrupo(
            @Parameter(description = "Id de la Inscripción") @PathVariable @Min(1) Long idInscripcion,
            @Parameter(description = "Id del grupo")@PathVariable @Min(1) Long idGrupo){
        return ResponseEntity.ok(inscripcionService.asignarGrupo(idInscripcion,idGrupo));
    }

    //-----consulta de alumnos por grupo
    @Operation(summary = "Consultar alumnos por grupo", description = "Obtiene la lista de alumnos activos pertenecientes a un grupo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Grupo no encontrado")
    })
    @GetMapping("/grupo/{idGrupo}/alumnos")
    public ResponseEntity<List<AlumnoGrupoDto>> obtenerAlumnosPorGrupo(
            @Parameter(description = "Id del grupo")@PathVariable Long idGrupo){
        return ResponseEntity.ok(inscripcionService.obtenerAlumnosPorGrupo(idGrupo));
    }
    //-----consulta de alumnos por grupo general
    @Operation(summary = "Consultar alumnos por grupo general", description = "Obtiene la lista general de alumnos asociados a un grupo, incluyendo registros activos e inactivos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Grupo no encontrado")
    })
    @GetMapping("/grupo/{idGrupo}/alumnos/general")
    public ResponseEntity<List<AlumnoGrupoDto>> obtenerAlumnosPorGrupoGeneral(
            @Parameter(description = "Id del grupo")@PathVariable Long idGrupo){
        return ResponseEntity.ok(inscripcionService.obtenerAlumnosPorGrupoGeneral(idGrupo));
    }

    //---consulta alumno grupo ---
    @Operation(summary = "Consultar grupo activo de alumno", description = "Obtiene el grupo asociado a la inscripción activa de un alumno.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Alumno no encontrado"),
            @ApiResponse(responseCode = "409", description = "El alumno no cuenta con inscripción activa")
    })
    @GetMapping("/grupoAlumno/{idUsuario}")
    public ResponseEntity<Optional<GrupoAlumnoDto>>consultaAlumnoActivo(
            @Parameter(description = "Id del alumno (usuario)")@PathVariable @Min(1) Long idUsuario){
        return ResponseEntity.ok(inscripcionService.grupoAlumnoInscripcion(idUsuario));
    }

    //---consulta alumno grupos---
    @Operation(summary = "Consultar grupos anteriores de alumno", description = "Obtiene la lista de grupos asociados a inscripciones inactivas del alumno.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Alumno no encontrado"),
            @ApiResponse(responseCode = "409", description = "El alumno no cuenta con inscripciones inactivas")
    })
    @GetMapping("/gruposAlumno/{idUsuario}")
    public ResponseEntity<List<GrupoAlumnoDto>>consultaAlumnoGrupos(
            @Parameter(description = "Id del alumno (usuario)")@PathVariable @Min(1) Long idUsuario){
        return ResponseEntity.ok(inscripcionService.gruposAlumnoInscripcion(idUsuario));
    }

}
