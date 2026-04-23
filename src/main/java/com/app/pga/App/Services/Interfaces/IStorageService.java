package com.app.pga.App.Services.Interfaces;


import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;

public interface IStorageService {
    String guardarEntrega (Long idInscripción, Long idGrupo, Long idActividadGrupo, MultipartFile archivo);
    Resource loadAsResource(String path);
    String guardarDocumentoExpediente( Long idAlumno, String tipoDocumento,  MultipartFile archivo);
    String guardarInstruccionesActividadBase(Long idActividadBase, MultipartFile archivo, String titulo);
    String guardarInstruccionesActividadGrupo(Long idActividadGrupo, MultipartFile archivo, String titulo);

    String copiarInstruccionesActividadBase(String urlInstrucciones, Long idActividad);
}
