package com.app.pga.App.Controllers;

import com.app.pga.App.Models.Dtos.RequestDto.DocumentoRequestDto;
import com.app.pga.App.Models.Dtos.ResponseDto.DocumentoResponseDto;
import com.app.pga.App.Models.Filtros.DocumentoFiltro;
import com.app.pga.App.Services.Interfaces.IDocumentoService;
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

@Tag(name = "Documentos", description = "Endpoints para gestionar el catalogo de documentso que conforman un expediente en el sistema")
@RestController
@RequestMapping("/api/documentos")
 @RequiredArgsConstructor
class DocumentoController {
    private final IDocumentoService documentoService;

    @Operation(summary = "Crear un nuevo documento", description = "Permite crear un nuevo tipo de documento e incorporarlo a los expedientes de cada alumno.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tipo de documento creado correctamente"),
            @ApiResponse(responseCode = "409", description = "Tipo de documento duplicado"),
    })
    @PostMapping()
    public ResponseEntity<DocumentoResponseDto> crearDocumento (@RequestBody @Valid DocumentoRequestDto dto){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(documentoService.crearDocumento(dto));
    }

    @Operation(summary = "Consulta de catalogo de documentos", description = "Permite listar los diferentes tipos de documento registrados en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente"),
    })
    @GetMapping()
    public ResponseEntity<List<DocumentoResponseDto>> obtenerDocumentos (){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(documentoService.obtenerDocumentos());
    }

    @Operation(summary = "Consulta de tipo de documento", description = "Permite consultar un tipo de documento de acuerdo al id especificado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Tipo de documento no encontrado"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<DocumentoResponseDto> obtenerDocumentos (
            @Parameter(description = "Id de Tipo de Documento", example = "1") @PathVariable Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(documentoService.obtenerDocumento(id));
    }

    @Operation(summary = "Actualizar tipo de documento", description = "Permite actualizar el nombre y de un tipo de documento asi como si e requerido obligatoriamente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Actualizaqción realizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Tipo de documento no encontrado"),
            @ApiResponse(responseCode = "409", description = "Tipo de documento no deshabilitado"),
    })
    @PutMapping("/{id}")
    public ResponseEntity< DocumentoResponseDto > actualizarDocumento (
            @Parameter(description = "Id de Tipo de Documento", example = "1") @PathVariable Long id ,
            @RequestBody @Valid DocumentoRequestDto dto){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(documentoService.actualizarDocumento(id,dto));
    }
    @PutMapping("habilitarDeshabilitar/{id}")
    public ResponseEntity<Void> habilitarDeshabilitarDocumento (
            @Parameter(description = "Id de Tipo de Documento", example = "1") @PathVariable Long id){
        documentoService.habilitarDeshabilitar(id);
        return ResponseEntity.ok()
                .build();
    }

    //consulta con paginacion
    @Operation(summary = "Consultar tipo de documentos", description = "Obtiene la lista general de tipo de documentos registrados.")
    @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente")
    @GetMapping("/listPage")
    public ResponseEntity<Page<DocumentoResponseDto>> consultaGeneralPage(@ModelAttribute DocumentoFiltro filtro, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "6") int size){
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(documentoService.findAll(filtro, pageable));
    }
}
