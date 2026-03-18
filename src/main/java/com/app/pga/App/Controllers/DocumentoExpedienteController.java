package com.app.pga.App.Controllers;

import com.app.pga.App.Models.Dtos.RequestDto.DocumentoRevisionDto;
import com.app.pga.App.Models.Dtos.ResponseDto.DocumentoExpedienteResponseDto;
import com.app.pga.App.Services.Interfaces.IDocumentoExpedienteService;
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

@RestController
@RequestMapping("/api/expedientes")
@RequiredArgsConstructor
class DocumentoExpedienteController {
    private final IDocumentoExpedienteService documentoExpedienteService;

    @PostMapping( "/{idAlumno}/documentos/{tipo}")
    public ResponseEntity<DocumentoExpedienteResponseDto> subirDocumento(@PathVariable("idAlumno") Long idExpediente, @RequestParam MultipartFile archivo, @PathVariable("tipo") String tipo) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(documentoExpedienteService.subirDocumento(idExpediente, tipo, archivo));
    }


    @GetMapping("documentos/{idDocumento}/ver")
    public ResponseEntity<Resource> visualizarDocumento(@PathVariable Long idDocumento ) {
        Resource doc = documentoExpedienteService.verDocumento(idDocumento);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + doc.getFilename() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM )
                .body(new InputStreamResource(doc));
    }

    @PutMapping("/documentos/{idDocumento}/revision")
    public ResponseEntity<Void> revisarDocumento (@PathVariable Long idDocumento, @RequestBody DocumentoRevisionDto dto){
        documentoExpedienteService.revisarDocumento(idDocumento,dto);
        return ResponseEntity.ok().build();
    }
}



