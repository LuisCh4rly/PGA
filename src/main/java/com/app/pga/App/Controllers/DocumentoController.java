package com.app.pga.App.Controllers;

import com.app.pga.App.Models.Dtos.RequestDto.DocumentoRequestDto;
import com.app.pga.App.Models.Dtos.ResponseDto.DocumentoResponseDto;
import com.app.pga.App.Models.Filtros.DocumentoFiltro;
import com.app.pga.App.Services.Interfaces.IDocumentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity< DocumentoResponseDto > actualizarDocumento (@PathVariable Long id , @RequestBody @Valid DocumentoRequestDto dto){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(documentoService.actualizarDocumento(id,dto));
    }
    @PutMapping("habilitarDeshabilitar/{id}")
    public ResponseEntity<Void> habilitarDeshabilitarDocumento (@PathVariable Long id){
        documentoService.habilitarDeshabilitar(id);
        return ResponseEntity.ok()
                .build();
    }

    //consulta con paginacion
    @GetMapping("/listPage")
    public ResponseEntity<Page<DocumentoResponseDto>> consultaGeneralPage(@ModelAttribute DocumentoFiltro filtro, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "6") int size){
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(documentoService.findAll(filtro, pageable));
    }
}
