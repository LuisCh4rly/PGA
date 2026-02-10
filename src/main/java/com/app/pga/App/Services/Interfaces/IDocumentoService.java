package com.app.pga.App.Services.Interfaces;

import com.app.pga.App.Models.Dtos.RequestDto.DocumentoRequestDto;
import com.app.pga.App.Models.Dtos.ResponseDto.DocumentoResponseDto;

import java.util.List;

public interface IDocumentoService {
    DocumentoResponseDto crearDocumento (DocumentoRequestDto dto);
    List<DocumentoResponseDto> obtenerDocumentos();
    DocumentoResponseDto obtenerDocumento(Long id);
    DocumentoResponseDto actualizarDocumento(Long id, DocumentoRequestDto dto);
    void habilitarDeshabilitar(Long id);
}
