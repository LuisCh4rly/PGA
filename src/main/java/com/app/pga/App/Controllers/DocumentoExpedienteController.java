package com.app.pga.App.Controllers;

import com.app.pga.App.Models.Dtos.RequestDto.DocumentoRevisionDto;
import com.app.pga.App.Models.Dtos.ResponseDto.DocumentoExpedienteResponseDto;
import com.app.pga.App.Services.Interfaces.IDocumentoExpedienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

@Tag(name = "Documentos de Expedientes", description = "Enpoints para la gestion del los documentos que conforman el expediente de los alumnos")
@RestController
@RequestMapping("/api/expedientes")
@RequiredArgsConstructor
class DocumentoExpedienteController {
    private final IDocumentoExpedienteService documentoExpedienteService;

    @Operation(
            summary = "Subir documento de expediente",
            description = "Permite cargar un documento al expediente de un alumno según el tipo de documento especificado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Documento cargado correctamente"),
            @ApiResponse(responseCode = "404", description = "Alumno o tipo de documento no encontrado"),
            @ApiResponse(responseCode = "409", description = "Alumno deshabilitado"),
            @ApiResponse(responseCode = "500", description = "Error al guardar el archivo")
    })
    @PostMapping( "/{idAlumno}/documentos/{tipo}")
    public ResponseEntity<DocumentoExpedienteResponseDto> subirDocumento(
            @Parameter(description = "Id del alumno (usuario)", example = "6") @PathVariable("idAlumno") Long idExpediente,
            @RequestParam MultipartFile archivo,
            @Parameter(description = "Tipo de documento", example = "CV") @PathVariable("tipo") String tipo) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(documentoExpedienteService.subirDocumento(idExpediente, tipo, archivo));
    }

    @Operation(summary = "Ver documento de expediente", description = "Permite visualizar un documento del expediente de un alumno según el id de documento especificado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Documento visualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Documento no encontrado o no se ha subido el archivo"),
            @ApiResponse(responseCode = "500", description = "Error al cargar el archivo")
    })
    @GetMapping("documentos/{idDocumento}/ver")
    public ResponseEntity<Resource> visualizarDocumento(
            @Parameter(description = "Id del documento", example = "6") @PathVariable Long idDocumento ) {
        Resource doc = documentoExpedienteService.verDocumento(idDocumento);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + doc.getFilename() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM )
                .body(new InputStreamResource(doc));
    }

    @Operation(summary = "Actualiza las observaciones del documento de expediente", description = "Permite actualizar las observaciones y el estado de un documento del expediente de un alumno.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Documento actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Documento no encontrado"),

    })
    @PutMapping("/documentos/{idDocumento}/revision")
    public ResponseEntity<Void> revisarDocumento (
            @Parameter(description = "Id del documento", example = "6") @PathVariable Long idDocumento,
            @Valid @RequestBody DocumentoRevisionDto dto){
        documentoExpedienteService.revisarDocumento(idDocumento,dto);
        return ResponseEntity.ok().build();
    }
}



