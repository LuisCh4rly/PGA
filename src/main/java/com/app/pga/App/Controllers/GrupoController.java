package com.app.pga.App.Controllers;

import com.app.pga.App.Models.Dtos.RequestDto.GrupoRequestDto;
import com.app.pga.App.Models.Dtos.ResponseDto.GrupoResponseDashboardDto;
import com.app.pga.App.Models.Dtos.ResponseDto.GrupoResponseDto;
import com.app.pga.App.Models.Filtros.GrupoFiltro;
import com.app.pga.App.Services.Interfaces.IGrupoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Grupos", description = "Endpoints para la gestion de grupos")
@RestController
@RequestMapping("/api/grupo")
@RequiredArgsConstructor
class GrupoController {
    private final IGrupoService grupoService;

    @Operation(summary = "Crear grupo", description = "Registra un nuevo grupo asociado a un curso y docente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Grupo creado correctamente"),
            @ApiResponse(responseCode = "404", description = "Curso o docente no encontrado"),
            @ApiResponse(responseCode = "409", description = "El grupo ya existe o el recurso está deshabilitado")
    })
    @PostMapping
    public ResponseEntity<GrupoResponseDto> crearGrupo (@RequestBody @Valid GrupoRequestDto dto){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(grupoService.crear(dto));
    }

    @Operation(summary = "Consultar grupo por ID", description = "Obtiene la información de un grupo específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Grupo no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity <GrupoResponseDto> obtenerGrupo(
             @Parameter(description = "Id del Grupo") @PathVariable Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(grupoService.obtenerPorId(id));
    }

    @Operation(summary = "Consultar grupos", description = "Obtiene la lista general de grupos registrados."
    )
    @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente")
    @GetMapping()
    public ResponseEntity<List<GrupoResponseDto>> obtenerGruposGeneral(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(grupoService.obtenerGrupoGeneral());
    }

    @Operation(summary = "Consultar grupos por curso", description = "Obtiene los grupos asociados a un curso.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Curso no encontrado")
    })
    @GetMapping("/porCurso/{id}")
    public ResponseEntity<List<GrupoResponseDto>> obtenerGruposPorCurso(
            @Parameter(description = "Id del Curso")@PathVariable Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(grupoService.obtenerPorCurso(id));
    }

    @Operation(summary = "Consultar grupos por docente", description = "Obtiene los grupos asignados a un docente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Docente no encontrado")
    })
    @GetMapping("/porDocente/{id}")
    public ResponseEntity<List<GrupoResponseDashboardDto>> obtenerGruposPorDocente(
            @Parameter(description = "Id del Docente")@PathVariable Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(grupoService.obtenerPorDocente(id));
    }

    @Operation(summary = "Consultar grupos activos por docente", description = "Obtiene los grupos activos asignados a un docente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Docente no encontrado")
    })
    @GetMapping("/Activos/porDocente/{id}")
    public ResponseEntity<List<GrupoResponseDto>> obtenerGruposActivosPorDocente(
            @Parameter(description = "Id del Docente")@PathVariable Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(grupoService.obtenerActivosPorDocente(id));
    }

    @Operation(summary = "Habilitar o deshabilitar grupo", description = "Cambia el estado de un grupo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Grupo no encontrado"),
            @ApiResponse(responseCode = "409", description = "El grupo cuenta con inscripciones activas")
    })
    @PutMapping("habilitarDeshabilitar/{id}")
    public ResponseEntity< GrupoResponseDto > habilitarDeshabilitarGrupo (
            @Parameter(description = "Id del Grupo")@PathVariable Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(grupoService.cambiarEstado(id));
    }
    @Operation(summary = "Cambiar docente de grupo", description = "Asigna un nuevo docente a un grupo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Docente asignado correctamente"),
            @ApiResponse(responseCode = "404", description = "Grupo o docente no encontrado"),
            @ApiResponse(responseCode = "409", description = "El docente se encuentra deshabilitado")
    })
    @PutMapping("/{idGrupo}/cambiarDocente/{idDocente}")
    public ResponseEntity< GrupoResponseDto > cambiarDocente(
            @Parameter(description = "Id del Grupo")@PathVariable Long idGrupo,
            @Parameter(description = "Id del Docente")@PathVariable Long idDocente ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(grupoService.cambiarDocente(idGrupo, idDocente));
    }

    //consulta general con paginacion
    @Operation(summary = "Consultar grupos paginados", description = "Obtiene una lista paginada de grupos aplicando filtros.")
    @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente")
    @GetMapping("/listPage")
    public ResponseEntity<Page<GrupoResponseDto>> consultaGeneralPage(
            @ModelAttribute GrupoFiltro filtro,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size){
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(grupoService.findAll(filtro, pageable));
    }

    //consulta de grupos activos
    @Operation(summary = "Consultar grupos activos", description = "Obtiene todos los grupos activos registrados.")
    @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente")
    @GetMapping("/activos")
    public ResponseEntity<List<GrupoResponseDto>> obtenerGruposActivos(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(grupoService.obtenerGruposActivos());
    }



}
