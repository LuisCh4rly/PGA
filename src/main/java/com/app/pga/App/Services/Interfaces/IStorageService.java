package com.app.pga.App.Services.Interfaces;


import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface IStorageService {
    String guardarEntrega (Long idInscripción, Long idGrupo, Long idActividadGrupo, MultipartFile archivo);
    Resource loadAsResource(String path);
    String guardarDocumentoExpediente( Long idAlumno, String tipoDocumento,  MultipartFile archivo);
}
