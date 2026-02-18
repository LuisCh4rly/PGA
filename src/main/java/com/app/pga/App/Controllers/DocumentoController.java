package com.app.pga.App.Controllers;

import com.app.pga.App.Models.Dtos.RequestDto.DocumentoRequestDto;
import com.app.pga.App.Models.Dtos.ResponseDto.DocumentoResponseDto;
import com.app.pga.App.Services.Interfaces.IDocumentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documentos")
 @RequiredArgsConstructor
class DocumentoController {
    private final IDocumentoService documentoService;
    @PostMapping()
    public ResponseEntity<DocumentoResponseDto> crearDocumento (@RequestBody @Valid DocumentoRequestDto dto){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(documentoService.crearDocumento(dto));
    }
    @GetMapping()
    public ResponseEntity<List<DocumentoResponseDto>> obtenerDocumentos (){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(documentoService.obtenerDocumentos());
    }
    @GetMapping("/{id}")
    public ResponseEntity<DocumentoResponseDto> obtenerDocumentos (@PathVariable Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(documentoService.obtenerDocumento(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity< DocumentoResponseDto > actualizarActividadBase (@PathVariable Long id , @RequestBody @Valid DocumentoRequestDto dto){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(documentoService.actualizarDocumento(id,dto));
    }
    @PutMapping("habilitarDeshabilitar/{id}")
    public ResponseEntity<Void> habilitarDeshabilitarActividadBase (@PathVariable Long id){
        documentoService.habilitarDeshabilitar(id);
        return ResponseEntity.ok()
                .build();
    }
}
