package com.app.pga.App.Reporte;


import com.app.pga.App.Models.Dtos.ReporteAsistenciaGrupoDto;
import com.app.pga.App.Models.Dtos.ReporteSeguimientoDto;
import com.app.pga.App.Models.Dtos.ResponseDto.*;
import com.app.pga.App.Models.Entities.Inscripcion;
import com.app.pga.App.Services.Implements.*;
import com.app.pga.App.Services.Interfaces.IDocumentoService;
import com.app.pga.App.Services.Interfaces.IGrupoService;
import com.app.pga.App.Services.Interfaces.ISeguimientoSemanalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Reportes", description = "Endpoints para la generación de reportes de usuarios, inscripciones, avance académico ")
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

    @Operation(summary = "Reporte de Inscripciones" ,description = "Genera un reporte que incluye el historial deinscripciones, situación del expediente e información del alumno")
    @ApiResponses( value  = {
         @ApiResponse(responseCode = "200", description = "Reporte generado correctamente"),
         @ApiResponse(responseCode = "404", description = "Inscripcion no encontrada, alumno no encontrado,"),
            @ApiResponse (responseCode = "500", description = "Error al generar el reporte")
        }
    )
    @GetMapping("/alumnos/{id}/reporte")
    public ResponseEntity<InputStreamResource> descargarReporte(
            @Parameter(description = "Id de Inscripción", example = "1") @PathVariable Long id
    ) throws Exception {
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
    @Operation(summary = "Generar reporte de asistencias", description = "Genera un reporte PDF de asistencias por grupo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reporte generado correctamente"),
            @ApiResponse(responseCode = "404", description = "Grupo no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error generando el reporte")
    })
    @GetMapping("/asistencias/{id}/reporte")
    public ResponseEntity<InputStreamResource> asistenciasReporte(
            @Parameter(description = "Id de Grupo", example = "1") @PathVariable Long id) throws Exception {
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

    @Operation(summary = "Generar reporte de seguimiento grupal", description = "Genera un reporte PDF de seguimiento de actividades por grupo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reporte generado correctamente"),
            @ApiResponse(responseCode = "404", description = "Grupo no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error generando el reporte")
    })
    @GetMapping("/grupos/{id}/reporte")
    public ResponseEntity<InputStreamResource> seguimientoReporte(
            @Parameter(description = "Id de Grupo", example = "1") @PathVariable Long id
    ) throws Exception {
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

    @Operation(summary = "Generar reporte de Usuarios", description = "Genera un reporte PDF con estadísticas e información general de usuarios.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reporte generado correctamente"),
            @ApiResponse(responseCode = "500", description = "Error generando el reporte")
    })
    @GetMapping("/inscripciones/reporte")
    public ResponseEntity<InputStreamResource> usuariosReporte() throws Exception {
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

    @Operation(summary = "Generar reporte de expedientes", description = "Genera un reporte PDF con el estado de expedientes y documentos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reporte generado correctamente"),
            @ApiResponse(responseCode = "500", description = "Error generando el reporte")
    })
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

    @Operation(summary = "Generar reporte de seguimiento semanal", description = "Genera un reporte PDF del seguimiento semanal de un alumno.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reporte generado correctamente"),
            @ApiResponse(responseCode = "404", description = "Inscripción no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error generando el reporte")
    })
    @GetMapping("/seguimientoSemanal/{idInscripcion}/reporte")
    public ResponseEntity<InputStreamResource> SeguimientoSemanalReporte(
            @Parameter(description = "Id de Inscripción", example = "1") @PathVariable Long idInscripcion
    ) throws Exception {

        List <SeguimientoDashboardResponseDto> semanas = seguimientoSemanalService.obtenerSeguimientoAlumno(idInscripcion);
        InscripcionResponseDto inscripcion = inscripcionService.finfById(idInscripcion);
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
