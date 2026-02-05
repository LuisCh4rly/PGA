package com.app.pga.App.Services.Implements;

import com.app.pga.App.Exception.NotFoundException;
import com.app.pga.App.Services.Interfaces.IStorageService;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Transactional
public class StorageService implements IStorageService {

    private final String BASE_PATH = "storage";

    @Override
    public String guardarEntrega(Long idInscripcion, Long idGrupo, Long idActividadGrupo, MultipartFile archivo) {

        validarArchivo(archivo);

        try {
            Path carpeta = Paths.get(
                    BASE_PATH,
                    "alumnos",
                    idInscripcion.toString(),
                    "grupos",
                    idGrupo.toString(),
                    "actividades",
                    idActividadGrupo.toString()
            );
            Files.createDirectories(carpeta);

            String nombreArchivo = UUID.randomUUID()
                    + "_" + archivo.getOriginalFilename();

            Path destino = carpeta.resolve(nombreArchivo);

            Files.copy(archivo.getInputStream(), destino);

            return destino.toString().replace("\\", "/");

        } catch (Exception e) {
            throw new RuntimeException("Error guardando archivo", e);
        }
    }

    @Override
    public Resource loadAsResource(String path) {
        try {
            Path file = Paths.get(path);
            Resource resource = new UrlResource(file.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new NotFoundException("Archivo no encontrado");
            }

            return resource;

        } catch (MalformedURLException e) {
            throw new RuntimeException("Error al cargar archivo", e);
        }
    }

    private void validarArchivo(MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) {
            throw new IllegalArgumentException("Archivo vacío");
        }
    }
}



