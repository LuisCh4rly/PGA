package com.app.pga.App.Reporte;


import com.app.pga.App.Models.Dtos.ReporteAsistenciaGrupoDto;
import com.app.pga.App.Models.Dtos.ReporteSeguimientoDto;
import com.app.pga.App.Models.Dtos.ResponseDto.*;
import com.app.pga.App.Models.Entities.Inscripcion;
import com.app.pga.App.Services.Implements.*;
import com.app.pga.App.Services.Interfaces.IDocumentoService;
import com.app.pga.App.Services.Interfaces.IGrupoService;
import com.app.pga.App.Services.Interfaces.ISeguimientoSemanalService;
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
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/reporte")
@RequiredArgsConstructor
public class ReporteController {

    private final UsuarioService usuarioService;
    private final InscripcionService inscripcionService;
    private final ExpedienteService expedienteService;
    private final ReporteService reporteService;
    private final ReporteAsistenciaService asistenciaService;
    private final SesionService sesionService;
    private final IGrupoService grupoService;
    private final ActividadAlumnoService actividadAlumnoService;
    private final ReporteSeguimientoService seguimientoService;
    private final ReporteInscripcionesService reporteInscripcionesService;
    private final IDocumentoService documentoService;
    private final ReporteExpGenService reporteExpGenService;
    private final ISeguimientoSemanalService seguimientoSemanalService;
    private final ReporteSeguimientoAlumnoService reporteSeguimientoAlumnoService;

    @GetMapping("/alumnos/{id}/reporte")
    public ResponseEntity<InputStreamResource> descargarReporte(@PathVariable Long id) throws Exception {
        UsuarioResponseDto alumno = usuarioService.findById(id);
        List<Inscripcion> inscripciones = inscripcionService.obtenerPorAlumno(id);
        ExpedienteResponseDto expediente = expedienteService.verExpediente(id);
        ByteArrayInputStream pdf = reporteService.generarReporteAlumno(alumno, inscripciones, expediente);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition",
                "inline; filename=reporte_alumno_" + alumno.nombre() + ".pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdf));
    }

    @GetMapping("/asistencias/{id}/reporte")
    public ResponseEntity<InputStreamResource> asistenciasReporte(@PathVariable Long id) throws Exception {
        List<ReporteAsistenciaGrupoDto> sesiones = sesionService.obtenerReporteAsistenciaPorGrupo(id);
        GrupoResponseDto grupo = grupoService.obtenerPorId(id);
        List<AlumnoGrupoDto> inscripciones = inscripcionService.obtenerAlumnosPorGrupo(id);
        ByteArrayInputStream pdf = asistenciaService.generarReporteAsistencia(sesiones, grupo, inscripciones);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition",
                "inline; filename=reporte_asistencia"+grupo.nombre()+".pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdf));
    }

    @GetMapping("/grupos/{id}/reporte")
    public ResponseEntity<InputStreamResource> seguimientoReporte(@PathVariable Long id) throws Exception {
        List<ReporteSeguimientoDto> seguimiento = actividadAlumnoService.obtenerReporteSeguimientoPorGrupo(id);
        ByteArrayInputStream pdf = seguimientoService.generarReporteSeguimiento(seguimiento);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition",
                "inline; filename=reporte_seguimiento.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdf));
    }

    @GetMapping("/inscripciones/reporte")
    public ResponseEntity<InputStreamResource> inscripcionesReporte() throws Exception {
        List<InscripcionReporteDto> inscripciones = inscripcionService.findAll();
        int conteoAlumAct = inscripcionService.obtenerAlumnosActivosConteo();
        int conteoAlumInac = inscripcionService.obtenerAlumnosInactivosConteo();
        int conteoDocAct = inscripcionService.obtenerDocentesActivosConteo();
        int conteoDocInac = inscripcionService.obtenerDocentesInactivosConteo();
        ByteArrayInputStream pdf = reporteInscripcionesService.generarReporteInscripciones(inscripciones, conteoAlumAct, conteoAlumInac, conteoDocAct, conteoDocInac);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition",
                "inline; filename=reporte_seguimiento.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdf));
    }

    @GetMapping("/expediente/reporte")
    public ResponseEntity<InputStreamResource> ExpedieneteReporte() throws Exception {
        List<ExpedienteReporteDto> expedientes = expedienteService.findAll();
        List <DocumentoResponseDto> documentos = documentoService.obtenerDocumentos();
        ByteArrayInputStream pdf = reporteExpGenService.generarResporteExpediente(expedientes, documentos);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition",
                "inline; filename=reporte_seguimiento.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdf));
    }

    @GetMapping("/seguimientoSemanal/{idUsuario}/reporte")
    public ResponseEntity<InputStreamResource> SeguimientoSemanalReporte(@PathVariable Long idUsuario) throws Exception {
        Optional<GrupoAlumnoDto> grupo = inscripcionService.grupoAlumnoInscripcion(idUsuario);
        List <SeguimientoDashboardResponseDto> semanas = seguimientoSemanalService.obtenerSeguimientoAlumno(grupo.get().idInscripcion());
        InscripcionResponseDto inscripcion = inscripcionService.finfById(grupo.get().idInscripcion());
        ByteArrayInputStream pdf = reporteSeguimientoAlumnoService.generarSeguimientoSemanal(semanas, inscripcion);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition",
                "inline; filename=reporte_seguimiento.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdf));
    }
}
