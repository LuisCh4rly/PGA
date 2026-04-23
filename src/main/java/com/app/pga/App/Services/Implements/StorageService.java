package com.app.pga.App.Services.Implements;

import com.app.pga.App.Exception.BadRequestException;
import com.app.pga.App.Exception.FileException;
import com.app.pga.App.Exception.NotFoundException;
import com.app.pga.App.Services.Interfaces.IStorageService;

import org.apache.tika.Tika;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;



import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
@Transactional
public class StorageService implements IStorageService {

    private final String BASE_PATH = "storage";
    private static final List<String> extensionesPermitidas = List.of("pdf","png","jpeg","jpg");

    private static final List<String> MINEPermitidos = List.of ("application/pdf", "image/png", "image/jpeg");

    @Override
    public String guardarEntrega(Long idInscripcion, Long idGrupo, Long idActividadGrupo, MultipartFile archivo) {
        validarArchivo(archivo);
        Path carpeta = Paths.get(
                BASE_PATH,
                "alumnos",
                "alumno " + idInscripcion.toString(),
                "grupos",
                "grupo-" + idGrupo.toString(),
                "actividades",
                "actividad-" + idActividadGrupo.toString()
        );
        String nombreArchivo = archivo.getOriginalFilename();
        Path destino = carpeta.resolve(nombreArchivo);

        try {
            Files.createDirectories(carpeta);
            Files.copy(archivo.getInputStream(), destino,  StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new FileException("Error guardando archivo");
        }

        String rutaRelativa = Paths.get(BASE_PATH)
                .toAbsolutePath()
                .normalize()
                .relativize(destino.toAbsolutePath().normalize())
                .toString()
                .replace("\\", "/");

        return rutaRelativa;
    }
    @Override
    public String guardarDocumentoExpediente( Long idAlumno, String tipoDocumento,  MultipartFile archivo) {
        validarArchivo(archivo);

        Path carpeta = Paths.get(BASE_PATH,
                "alumnos",
                "alumno-" + idAlumno.toString(),
                "expediente");
        String nombre = archivo.getOriginalFilename();
        Path destino = carpeta.resolve(nombre);
        try {
            Files.createDirectories(carpeta);
            Files.copy(archivo.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new FileException("Error guardando documento");
        }

        String rutaRelativa = Paths.get(BASE_PATH)
                .toAbsolutePath()
                .normalize()
                .relativize(destino.toAbsolutePath().normalize())
                .toString()
                .replace("\\", "/");

        return rutaRelativa;
    }
    @Override
    public String guardarInstruccionesActividadBase(Long idActividadBase, MultipartFile archivo, String titulo) {
        validarArchivo(archivo);
        String extension = "";
        String nombreArchivo = "";
        Path carpeta = Paths.get(
                BASE_PATH,
                "actividades",
                "actividades-base",
                "actividad-" + idActividadBase.toString()
        );
        String nombre = archivo.getOriginalFilename();
        if (nombre != null || nombre.contains(".")) {
            extension =nombre.substring(nombre.lastIndexOf(".") + 1).toLowerCase();
            nombreArchivo = titulo.replace(" ", "_") +"_Instrucciones."+ extension;
        }
        else{
            nombreArchivo = archivo.getOriginalFilename();
        }

        Path destino = carpeta.resolve(nombreArchivo);
        try {
            Files.createDirectories(carpeta);
            Files.copy(archivo.getInputStream(), destino,  StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new FileException("Error guardando archivo");
        }
        String rutaRelativa = Paths.get(BASE_PATH)
                .toAbsolutePath()
                .normalize()
                .relativize(destino.toAbsolutePath().normalize())
                .toString()
                .replace("\\", "/");

        return rutaRelativa;
    }
    @Override
    public String guardarInstruccionesActividadGrupo(Long idActividadGrupo, MultipartFile archivo, String titulo) {
        validarArchivo(archivo);
        Path carpeta = Paths.get(
                BASE_PATH,
                "actividades",
                "actividades-grupo",
                "actividad-"+idActividadGrupo.toString()
        );

        String nombre = archivo.getOriginalFilename();
        String extension = "";
        String nombreArchivo = "";

        if (nombre != null || nombre.contains(".")) {
            extension = nombre.substring(nombre.lastIndexOf(".") + 1).toLowerCase();
            nombreArchivo = titulo.replace(" ", "_") +"_instrucciones."+ extension;
        }else{
            nombreArchivo = archivo.getOriginalFilename();
        }
        Path destino = carpeta.resolve(nombreArchivo);

        try {
            Files.createDirectories(carpeta);
            Files.copy(archivo.getInputStream(), destino,  StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new FileException("Error guardando archivo");
        }

        String rutaRelativa = Paths.get(BASE_PATH)
                .toAbsolutePath()
                .normalize()
                .relativize(destino.toAbsolutePath().normalize())
                .toString()
                .replace("\\", "/");

        return rutaRelativa;
    }

    @Override
    public String copiarInstruccionesActividadBase(String urlInstrucciones, Long idActividadGrupo) {
        Path basePath = Paths.get(BASE_PATH).toAbsolutePath().normalize();
        Path origen = basePath.resolve(urlInstrucciones).normalize();

        if (!Files.exists(origen)) {
            throw new FileException("Archivo origen no existe: " + urlInstrucciones);
        }

        String nombreArchivo = origen.getFileName().toString();

        Path carpeta = basePath.resolve(Paths.get("actividades","actividades-grupo","actividad-"+idActividadGrupo.toString() ));
        Path destino = carpeta.resolve(nombreArchivo);


       try{
            Files.createDirectories(destino.getParent());
            Files.copy(origen, destino, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new FileException("Error copiando archivo a actividad grupo");
        }
        String rutaRelativa = Paths.get(BASE_PATH)
                .toAbsolutePath()
                .normalize()
                .relativize(destino.toAbsolutePath().normalize())
                .toString()
                .replace("\\", "/");

        return rutaRelativa;
    }
    @Override
    public Resource loadAsResource(String path) {
        try {
            Path basePath = Paths.get("storage").toAbsolutePath().normalize();
            // Resolver la ruta que viene de BD
            Path file = basePath.resolve(path).normalize();
            if (!file.startsWith(basePath)) {
                throw new RuntimeException("Acceso denegado");
            }
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



