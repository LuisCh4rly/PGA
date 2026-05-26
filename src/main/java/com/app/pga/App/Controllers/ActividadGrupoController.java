package com.app.pga.App.Controllers;

import com.app.pga.App.Models.Dtos.*;
import com.app.pga.App.Models.Dtos.RequestDto.*;
import com.app.pga.App.Models.Dtos.ResponseDto.ActividadGrupoAgrupadaDto;
import com.app.pga.App.Models.Dtos.ResponseDto.ActividadGrupoDashboardDto;
import com.app.pga.App.Models.Dtos.ResponseDto.GrupoResumenDto;
import com.app.pga.App.Services.Interfaces.IActividadGrupoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@Tag(name = "Actividades de Grupo", description = "Endpoints para la gestión de actividades grupales e individuales asignadas a los grupos")
@RestController
@RequestMapping("/api/grupo")
@RequiredArgsConstructor
public class ActividadGrupoController {

    private final IActividadGrupoService actividadGrupoService;
    @Operation(summary = "Asignar actividad desde catálogo", description = "Asigna una actividad del catálogo a un grupo, permitiendo adjuntar un archivo de instrucciones."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Actividad asignada correctamente"),
            @ApiResponse(responseCode = "404", description = "Grupo o actividad no encontrada"),
            @ApiResponse(responseCode = "409", description = "La actividad ya fue asignada")
    })
    @PostMapping("/{idGrupo}/actividades/catalogo")
    public ResponseEntity<ActividadGrupoDto> asignarDedeCatalogo (
            @Parameter(description = "ID de grupo", example = "1")@PathVariable Long idGrupo,
                                                                  @RequestPart("dto") @Valid AsignarActividadCatalogoDto dto,
                                                                  @RequestPart(value = "archivo", required = false) MultipartFile archivo){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(actividadGrupoService.asignarDesdeCatalogo(idGrupo, dto, archivo));
    }

    @Operation(summary = "Crear actividad extra", description = "Crea y asigna una actividad adicional a un grupo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Actividad creada correctamente"),
            @ApiResponse(responseCode = "404", description = "Grupo no encontrado")
    })
    @PostMapping("/{idGrupo}/actividades/extra")
    public ResponseEntity<ActividadGrupoDto> asignarExtra (
            @Parameter(description = "ID de grupo", example = "1")@PathVariable Long idGrupo,
                                                           @RequestPart("dto") @Valid AsignarActividadExtraDto dto,
                                                           @RequestPart(value = "archivo") MultipartFile archivo){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(actividadGrupoService.crearExtra(idGrupo, dto, archivo));
    }

    @Operation(summary = "Consultar actividades por grupo", description = "Obtiene todas las actividades asociadas a un grupo.")
    @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente")
    @GetMapping("/{idGrupo}/actividades")
    public ResponseEntity<List<ActividadGrupoDto>> obtenerActividadesGrupo(
            @Parameter(description = "ID de grupo", example = "1")@PathVariable Long idGrupo){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body( actividadGrupoService.obtenerActividadesPorGrupo(idGrupo));
    }
    @Operation(summary = "Consultar actividades grupales", description = "Obtiene únicamente las actividades grupales asociadas a un grupo.")
    @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente")
    @GetMapping("/{idGrupo}/actividades/grupales")
    public ResponseEntity<List<ActividadGrupoDto>> obtenerActividadesGrupales(@PathVariable Long idGrupo){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body( actividadGrupoService.obtenerActividadesGrupales(idGrupo));
    }

    @Operation(summary = "Consultar actividades individuales", description = "Obtiene las actividades individuales asociadas a un grupo.")
    @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente")
    @GetMapping("/{idGrupo}/actividades/individuales")
    public List<ActividadGrupoDto> obtenerActividadesIndividuales(
            @Parameter(description = "ID de grupo", example = "1") @PathVariable Long idGrupo){
        return actividadGrupoService.obtenerActividadesIndividuales(idGrupo);
    }

    @Operation(summary = "Agregar inscripciones a actividad", description = "Asocia inscripciones de alumnos a una actividad grupal.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inscripciones agregadas correctamente"),
            @ApiResponse(responseCode = "404", description = "Actividad no encontrada")
    })
    @PostMapping("/actividad-grupo/{idActividadGrupo}/inscripciones")
    public void agregarInscripcionesActividad(
            @Parameter(description = "ID de actividad grupo", example = "1")@PathVariable Long idActividadGrupo,  @RequestBody AgregarInscripcionesDto dto){
        actividadGrupoService.agregarInscripcionesActividad(idActividadGrupo, dto.idsInscripciones()
        );
    }

    @Operation(summary = "Consultar actividad por ID", description = "Obtiene el detalle de una actividad específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Actividad no encontrada")
    })
    @GetMapping("/actividad/{idActividad}")
    public ResponseEntity<ActividadGrupoDashboardDto> obtenerPorId (
            @Parameter(description = "ID de actividad grupo", example = "1")@PathVariable Long idActividad){
       return ResponseEntity
               .status(HttpStatus.OK)
               .body(actividadGrupoService.obtenerActividadPorId(idActividad));
    }

    @Operation(summary = "Visualizar instrucciones", description = "Permite visualizar el archivo de instrucciones asociado a una actividad.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Archivo obtenido correctamente"),
            @ApiResponse(responseCode = "404", description = "Archivo no encontrado")
    })
    @GetMapping("actividad/{idActividad}/instrucciones")
    public ResponseEntity<Resource> verInstrucciones(
            @Parameter(description = "ID de actividad grupo", example = "1") @PathVariable Long idActividad) {

        Resource archivo = actividadGrupoService.visualizarInstrucciones(idActividad);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + archivo.getFilename() + "\"")
                .body(archivo);
    }

    @Operation(summary = "Actualizar instrucciones de actividad", description = "Actualiza el archivo de instrucciones asociado a una actividad.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Archivo actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Actividad no encontrada")
    })
    @PutMapping ("/actividad/{idActividad}/actualizar")
    public ResponseEntity <ActividadGrupoDto> actualizarInstruccionesPorId (
            @Parameter(description = "ID de actividad grupo", example = "1") @PathVariable Long idActividad,
                                                                            @RequestParam(value =  "archivo", required =false) MultipartFile archivo){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(actividadGrupoService.actualizarInstruccionesPorId(idActividad,archivo));
    }

    @Operation(
            summary = "Consultar grupos sin actividad",
            description = "Obtiene los grupos de un docente que aún no tienen asignada una actividad con el título especificado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Docente no encontrado")
    })
    @GetMapping("{idDocente}/actividad")
    public ResponseEntity<List<GrupoResumenDto>> obtenerGruposSinActividad (
            @Parameter(description = "ID de actividad grupo", example = "1") @RequestParam("titulo") String titulo, @PathVariable Long idDocente){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(actividadGrupoService.obtenerGruposSinActividad(titulo,idDocente));
    }

    @Operation(summary = "Asignar actividad de catálogo a múltiples grupos", description = "Asigna una actividad del catálogo a múltiples grupos de manera simultánea.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Actividades asignadas correctamente"),
            @ApiResponse(responseCode = "404", description = "Grupo o actividad no encontrada")
    })
    @PostMapping("/actividades/catalogo/multiples")
    public  ResponseEntity <Void> asignarasignarDesdeCatalogoMultiple ( @RequestPart("dto") @Valid AsignarActividadCatalogoMultipleDto dto,
                                                                        @RequestPart(value = "archivo", required = false) MultipartFile archivo){
        actividadGrupoService.asignarDesdeCatalogoMultiple(dto,archivo);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Asignar actividad extra a múltiples grupos", description = "Crea y asigna una actividad extra a múltiples grupos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Actividades creadas correctamente"),
            @ApiResponse(responseCode = "404", description = "Grupo no encontrado")
    })
    @PostMapping("/actividades/extra/multiples")
    public  ResponseEntity <Void> asignarExtraMultiple (@RequestPart("dto") @Valid AsignarActividadExtraMultipleDto dto,
                                                         @RequestPart(value = "archivo") MultipartFile archivo){
        actividadGrupoService.crearExtraMultiple(dto,archivo);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Consultar actividades agrupadas por docente", description = "Obtiene las actividades agrupadas de los grupos asociados a un docente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Docente no encontrado")
    })
    @GetMapping("/docente/{idDocente}/actividades")
    public ResponseEntity<Set<ActividadGrupoAgrupadaDto>> obtenerActividadesPorGrupos (@PathVariable Long idDocente){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(actividadGrupoService.obtenerActividadesPorGrupos(idDocente));
    }

    @Operation(summary = "Actualizar instrucciones múltiples", description = "Actualiza el archivo de instrucciones de múltiples actividades.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Instrucciones actualizadas correctamente"),
            @ApiResponse(responseCode = "404", description = "Actividad no encontrada")
    })
    @PutMapping("actividades/instrucciones")
    public ResponseEntity<Void> actualizarMultiples (@RequestPart List<Long> idActividades,
                                                     @RequestPart MultipartFile archivo){
        actividadGrupoService.actualizarInstruccionesMultiple(idActividades, archivo);
        return ResponseEntity.ok().build();
    }

    }
