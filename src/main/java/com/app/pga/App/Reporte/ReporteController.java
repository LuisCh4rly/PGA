package com.app.pga.App.Reporte;


import com.app.pga.App.Models.Dtos.ResponseDto.ExpedienteResponseDto;
import com.app.pga.App.Models.Dtos.ResponseDto.UsuarioResponseDto;
import com.app.pga.App.Models.Entities.Inscripcion;
import com.app.pga.App.Services.Implements.ExpedienteService;
import com.app.pga.App.Services.Implements.InscripcionService;
import com.app.pga.App.Services.Implements.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.util.List;

@RestController
@RequestMapping("/api/reporte")
@RequiredArgsConstructor
public class ReporteController {

    private final UsuarioService usuarioService;
    private final InscripcionService inscripcionService;
    private final ExpedienteService expedienteService;
    private final ReporteService reporteService;
    @GetMapping("/alumnos/{id}/reporte")
    public ResponseEntity<InputStreamResource> descargarReporte(@PathVariable Long id) throws Exception {
        UsuarioResponseDto alumno = usuarioService.findById(id);
        List<Inscripcion> inscripciones = inscripcionService.obtenerPorAlumno(id);
        ExpedienteResponseDto expediente = expedienteService.verExpediente(id);
        ByteArrayInputStream pdf = reporteService.generarReporteAlumno(alumno, inscripciones, expediente);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition",
                "attachment; filename=reporte_alumno_" + alumno.nombre() + ".pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdf));
    }
}
