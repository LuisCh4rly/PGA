package com.app.pga.App.Controllers;

import com.app.pga.App.Models.Dtos.ActividadAlumnoDto;
import com.app.pga.App.Models.Dtos.RequestDto.CambiarEstadoTareaDto;
import com.app.pga.App.Models.Enum.EstadoTarea;
import com.app.pga.App.Services.Implements.ActividadAlumnoService;
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
@RequestMapping("/api/actividades-alumnos")
@RequiredArgsConstructor
public class ActividadAlumnoController {
    private final ActividadAlumnoService actividadAlumnoService;

    @PostMapping ("/{id}/entrega")
    public ResponseEntity<Void> entregarActividad(@PathVariable Long id, @RequestParam MultipartFile archivo) {
        actividadAlumnoService.entregarActividad(id, archivo);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/inscripcion/{idInscripcion}")
    public ResponseEntity<List<ActividadAlumnoDto>> obtenerActividadesAlumno( @PathVariable Long idInscripcion) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(actividadAlumnoService.obtenerPorInscripcion(idInscripcion));
    }
    @PutMapping("/{id}/observacion")
    public ResponseEntity<Void> agregarObservacion(@PathVariable Long id, @RequestBody CambiarEstadoTareaDto dto) {
        actividadAlumnoService.agregarObservacion(id, dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/exentar")
    public ResponseEntity<Void> exentarActividad(@PathVariable Long id, @RequestBody CambiarEstadoTareaDto dto) {
        actividadAlumnoService.exentarActividad(id, dto);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/{id}/estado")
    public ResponseEntity<Void> cambiarEstado(@PathVariable Long id, @RequestBody CambiarEstadoTareaDto dto) {
        actividadAlumnoService.cambiarEstado(id, dto);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/{id}/entrega")
    public ResponseEntity<Resource> verEntrega(@PathVariable Long id) {
        Resource archivo = actividadAlumnoService.visualizarEntrega(id);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + archivo.getFilename() + "\"")
                .body(new InputStreamResource(archivo));
    }
}
