package com.app.pga.App.Controllers;


import com.app.pga.App.Models.Dtos.RequestDto.AgregarActividadesCursoDto;
import com.app.pga.App.Models.Dtos.RequestDto.CursoRequestDto;
import com.app.pga.App.Models.Dtos.ResponseDto.CursoResponseDto;
import com.app.pga.App.Models.Filtros.CursoFiltro;
import com.app.pga.App.Services.Interfaces.ICursoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(name = "Cursos", description = "Gestión de cursos y asignación de actividades")
@RestController
@RequestMapping("/api/curso")
@RequiredArgsConstructor
class CursoController {
    private final ICursoService cursoService;

    @Operation(summary = "Crear curso", description = "Registra un nuevo curso y permite asociar actividades base.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Curso creado correctamente"),
            @ApiResponse(responseCode = "404", description = "Actividad base no encontrada"),
            @ApiResponse(responseCode = "409", description = "El curso ya existe o una actividad está deshabilitada")
    })
    @PostMapping
    public ResponseEntity<CursoResponseDto> crearCurso (@RequestBody @Valid CursoRequestDto dto){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(cursoService.crearCurso(dto));
    }

    @Operation(summary = "Consultar curso por ID", description = "Obtiene la información de un curso específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Curso no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CursoResponseDto> obtenerCurso(
            @Parameter(description = "id de Curso", example = "1") @PathVariable Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cursoService.obtenerCurso(id));
    }

    @Operation(summary = "Consultar cursos", description = "Obtiene la lista general de cursos registrados.")
    @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente")
    @GetMapping()
    public ResponseEntity<List<CursoResponseDto>> obtenerCursos(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cursoService.obtenerCursosGeneral());
    }

    @Operation(summary = "Consultar cursos activos", description = "Obtiene todos los cursos activos registrados.")
    @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente")
    @GetMapping("/activos")
    public ResponseEntity<List<CursoResponseDto>> obtenerCursosActivos(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cursoService.obtenerCursosActivos());
    }
    @Operation(summary = "Actualizar curso", description = "Actualiza la información de un curso.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Curso actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Curso no encontrado"),
            @ApiResponse(responseCode = "409", description = "El curso está deshabilitado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CursoResponseDto> actualizarCurso(
            @Parameter(description = "id de Curso", example = "1") @PathVariable Long id, @RequestBody CursoRequestDto dto){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cursoService.actualizarCurso(dto,id));
    }

    @Operation(summary = "Habilitar o deshabilitar curso", description = "Cambia el estado de un curso.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Curso no encontrado"),
            @ApiResponse(responseCode = "409", description = "Existen grupos activos asociados al curso")
    })
    @PutMapping("/habilitarDeshabilitar/{id}")
    public ResponseEntity<CursoResponseDto> habilitarDeshabilitarCurso(
            @Parameter(description = "id de Curso", example = "1") @PathVariable Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cursoService.habitarDeshabilitar(id));
    }
    @Operation(summary = "Asignar actividades a curso", description = "Asocia actividades base a un curso.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Actividades asignadas correctamente"),
            @ApiResponse(responseCode = "404", description = "Curso o actividad no encontrada"),
            @ApiResponse(responseCode = "409", description = "La actividad ya está asignada o está deshabilitada")
    })
    @PutMapping("{id}/asignarActividades")
    public ResponseEntity<CursoResponseDto>asignarActividadesCurso(
            @Parameter(description = "id de Curso", example = "1") @PathVariable Long id,
            @RequestBody AgregarActividadesCursoDto agregarActividadesCursoDto){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cursoService.asignarActividades(id, agregarActividadesCursoDto));
    }

    @Operation(summary = "Quitar actividad de curso", description = "Elimina una actividad asignada a un curso.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Actividad eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "La actividad no está asignada al curso")
    })
    @DeleteMapping("/{idCurso}/quitarActividades/{idActividad}")
    public ResponseEntity<Void>asignarActividadesCurso(
            @Parameter(description = "id de Curso", example = "1") @PathVariable Long idCurso,
            @PathVariable Long idActividad){
        cursoService.quitarActividades(idCurso, idActividad);
        return ResponseEntity
                .noContent()
                .build();
    }

    //consulta con paginacion
    @Operation(summary = "Consultar cursos paginados", description = "Obtiene una lista paginada de cursos aplicando filtros.")
    @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente")
    @GetMapping("/listPage")
    public ResponseEntity<Page<CursoResponseDto>> consultaGeneralPage(@ModelAttribute CursoFiltro filtro, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "6") int size){
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(cursoService.findAll(filtro, pageable));
    }


}
