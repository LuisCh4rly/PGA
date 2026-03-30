package com.app.pga.App.Services.Implements;

import com.app.pga.App.Exception.BadRequestException;
import com.app.pga.App.Exception.NotFoundException;
import com.app.pga.App.Services.Interfaces.IStorageService;

import org.apache.tika.Tika;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class StorageService implements IStorageService {

    private final String BASE_PATH = "storage";
    private static final List<String> extensionesPermitidas = List.of("pdf","png","jpeg","jpg");

    private static final List<String> MINEPermitidos = List.of ("application/pdf", "image/png", "image/jpeg");

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

            Files.copy(archivo.getInputStream(), destino,  StandardCopyOption.REPLACE_EXISTING);

            return destino.toString().replace("\\", "/");

        } catch (Exception e) {
            throw new RuntimeException("Error guardando archivo", e);
        }
    }
    @Override
    public String guardarDocumentoExpediente( Long idAlumno, String tipoDocumento,  MultipartFile archivo) {
        validarArchivo(archivo);
        try {
            Path carpeta = Paths.get(BASE_PATH,
                    "alumnos",
                    idAlumno.toString(),
                    "expediente");

            Files.createDirectories(carpeta);

            String nombre = UUID.randomUUID() + "_" + archivo.getOriginalFilename();
            Path destino = carpeta.resolve(nombre);

            Files.copy(archivo.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);

            return destino.toString().replace("\\", "/");

        } catch (Exception e) {
            throw new RuntimeException("Error guardando documento", e);
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
    //validador de documentos
    private void validarArchivo(MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) {
            throw new IllegalArgumentException("Archivo vacío");
        }
        //validar tamaño
        if (archivo.getSize() > 5 * 1024 * 1024) {
            throw new BadRequestException("El archivo supera el tamaño permitido");
        }

        ValidarExtension(archivo);
        ValidarMINE(archivo);
        try {
            Tika tika = new Tika();
            String tipoReal = tika.detect(archivo.getInputStream());
            if (!MINEPermitidos.contains(tipoReal)) {
                throw new BadRequestException("El archivo no corresponde al tipo permitido");
            }
        }catch (IOException e){
            throw new RuntimeException("");
        }
    }


    private void ValidarExtension(MultipartFile archivo){
        String nombre = archivo.getOriginalFilename();
        if(nombre == null || !nombre.contains(".")) {
            throw new BadRequestException("Nombre de archivo inválido");
        }
        String extension = nombre.substring(nombre.lastIndexOf(".") + 1).toLowerCase();

        if(!extensionesPermitidas.contains(extension)) {
            throw new BadRequestException("Tipo de archivo no permitido");
        }
    }
    private void ValidarMINE(MultipartFile archivo){
        if(!MINEPermitidos.contains(archivo.getContentType())) {
            throw new BadRequestException("Tipo MIME no permitido");
        }
    }

}



