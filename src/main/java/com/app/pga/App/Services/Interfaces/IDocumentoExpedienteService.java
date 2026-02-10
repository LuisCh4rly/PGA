package com.app.pga.App.Services.Interfaces;

import com.app.pga.App.Models.Dtos.RequestDto.DocumentoRevisionDto;
import com.app.pga.App.Models.Dtos.ResponseDto.DocumentoExpedienteResponseDto;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface IDocumentoExpedienteService {
    DocumentoExpedienteResponseDto subirDocumento(Long idAlumno, String tipoDocumento, MultipartFile archivo);
    void revisarDocumento(Long idDocumento, DocumentoRevisionDto dto);
    Resource verDocumento(Long idDocumentoExpediente);
}
