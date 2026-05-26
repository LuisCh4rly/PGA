package com.app.pga.App.Controllers;

import com.app.pga.App.Models.Dtos.RequestDto.ActividadBaseRequestDto;
import com.app.pga.App.Models.Dtos.ResponseDto.ActividadBaseResponseDto;
import com.app.pga.App.Models.Filtros.ActividadBaseFiltro;
import com.app.pga.App.Services.Interfaces.IActividadBaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.Multipart;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Actividades Base", description = "Gestión de actividades base y archivos de instrucciones")
@RestController
@RequestMapping("/api/actividadBase")
@RequiredArgsConstructor
class ActividadBaseController {
    private final IActividadBaseService actividadBaseService;

    @Operation(summary = "Crear actividad base", description = "Registra una nueva actividad base y almacena el archivo de instrucciones.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Actividad creada correctamente"),
            @ApiResponse(responseCode = "404", description = "Campo formativo no encontrado"),
            @ApiResponse(responseCode = "409", description = "La actividad ya existe o el campo está deshabilitado")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ActividadBaseResponseDto> crearActividadBase (@RequestPart("actividadBaseRequestDto") @Valid ActividadBaseRequestDto actividadBaseRequestDto,
                                                                        @RequestPart MultipartFile archivo){

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(actividadBaseService.crearActividadBase(actividadBaseRequestDto, archivo));
    }
    @Operation(summary = "Consultar actividad base por ID", description = "Obtiene la información de una actividad base específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Actividad base no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ActividadBaseResponseDto> obtenerActividadBase (
            @Parameter(description = "Id de actividad base") @PathVariable @Min(1) Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(actividadBaseService.obtenerActividadBase(id));
    }
    @Operation(summary = "Consultar actividades base", description = "Obtiene una lista paginada de actividades base aplicando filtros.")
    @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente")
    @GetMapping()
    public ResponseEntity<Page<ActividadBaseResponseDto>> obtenerActividades (ActividadBaseFiltro filtro, Pageable pageable){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(actividadBaseService.obtenerActividadesBaseGeneral(filtro, pageable));
    }
    @Operation(summary = "Consultar actividades base activas", description = "Obtiene todas las actividades base activas.")
    @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente")
    @GetMapping ("/activas")
    public ResponseEntity< List <ActividadBaseResponseDto> > obtenerActividadesBaseActivas (){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(actividadBaseService.obtenerActividadesBaseActivas());
    }

    @Operation(summary = "Consultar actividades por campo formativo", description = "Obtiene las actividades base asociadas a un campo formativo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Campo formativo no encontrado")
    })
    @GetMapping ("campo/{idCampo}")
    public ResponseEntity< List <ActividadBaseResponseDto> > obtenerActividadesBasePorCampo(
            @Parameter(description = "Id de Campo Formativo")@PathVariable Long idCampo ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(actividadBaseService.obtenerActividadesBasePorCampo(idCampo));
    }

    @Operation(summary = "Actualizar actividad base", description = "Actualiza la información y archivo de instrucciones de una actividad base.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Actividad actualizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Actividad base no encontrada"),
            @ApiResponse(responseCode = "409", description = "La actividad está deshabilitada")
    })
    @PutMapping("/{id}")
    public ResponseEntity< ActividadBaseResponseDto > actualizarActividadBase (
            @Parameter(description = "Id de actividad Base") @PathVariable Long id ,
            @RequestPart("actividadBaseRequestDto") @Valid ActividadBaseRequestDto actividadBaseRequestDto,
            @RequestPart(value = "archivo", required = false) MultipartFile archivo){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(actividadBaseService.actualizarActividadBase(actividadBaseRequestDto, id, archivo));
    }

    @Operation(summary = "Habilitar o deshabilitar actividad base", description = "Cambia el estado de una actividad base.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Actividad base no encontrada"),
            @ApiResponse(responseCode = "409", description = "Campo formativo deshabilitado")
    })
    @PutMapping("habilitarDeshabilitar/{id}")
    public ResponseEntity< ActividadBaseResponseDto > habilitarDeshabilitarActividadBase (
            @Parameter( description = "Id de Actividad Base") @PathVariable @Min(1) Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(actividadBaseService.habitarDeshabilitar(id));
    }

    @Operation(summary = "Visualizar instrucciones", description = "Obtiene el archivo PDF de instrucciones de una actividad base.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Archivo obtenido correctamente"),
            @ApiResponse(responseCode = "404", description = "Actividad o archivo no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error al cargar el archivo")
    })

    @GetMapping("/{idActividad}/instrucciones")
    public ResponseEntity<Resource> verInstrucciones(
            @Parameter( description = "Id de Actividad Base") @PathVariable Long idActividad) {

        Resource archivo = actividadBaseService.visualizarInstrucciones(idActividad);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + archivo.getFilename() + "\"")
                .body(archivo);
    }
}


