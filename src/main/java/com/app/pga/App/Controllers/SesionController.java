package com.app.pga.App.Controllers;

import com.app.pga.App.Exception.NotFoundException;
import com.app.pga.App.Models.Dtos.*;
import com.app.pga.App.Models.Dtos.RequestDto.SesionRequestDto;
import com.app.pga.App.Models.Dtos.RequestDto.SesionUpdateDto;
import com.app.pga.App.Models.Dtos.ResponseDto.SesionDetalleAlumnoDto;
import com.app.pga.App.Models.Dtos.ResponseDto.SesionDetalletDto;
import com.app.pga.App.Models.Dtos.ResponseDto.SesionDto;
import com.app.pga.App.Models.Dtos.ResponseDto.SesiondocenteDto;
import com.app.pga.App.Models.Entities.Sesion;
import com.app.pga.App.Models.Enum.EstadoAsistencia;
import com.app.pga.App.Models.Filtros.SesionFiltro;
import com.app.pga.App.Models.Mappers.InscripcionMapper;
import com.app.pga.App.Repositories.ISesionAlumnoRepository;
import com.app.pga.App.Repositories.ISesionRepository;
import com.app.pga.App.Services.Implements.SesionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sesiones")
@Validated
class SesionController {

    private final SesionService sesionService;
    private final ISesionRepository iSesionRepository;
    private final ISesionAlumnoRepository iSesionAlumnoRepository;

    SesionController(SesionService sesionService, ISesionRepository iSesionRepository, ISesionAlumnoRepository iSesionAlumnoRepository) {
        this.sesionService = sesionService;
        this.iSesionRepository = iSesionRepository;
        this.iSesionAlumnoRepository = iSesionAlumnoRepository;
    }

    //crear
    @PostMapping("/grupo/{idGrupo}")
    public ResponseEntity<SesionDto> crearSesion(@PathVariable Long idGrupo,  @Valid @RequestBody SesionRequestDto dto) {
        SesionDto sesion = sesionService.createSesion(idGrupo, dto);
        return new ResponseEntity<>(sesion, HttpStatus.CREATED);
    }

    //actualizar lista de inscripciones en alcance individual
    @PutMapping("/{idSesion}/inscripciones")
    public ResponseEntity<SesionDetalletDto> actualizarInscripciones(@PathVariable Long idSesion, @RequestBody List<Long> idsInscripcion) {
        SesionDetalletDto sesion = sesionService.actualizarInscripcionesSesion(idSesion, idsInscripcion);
        return ResponseEntity.ok(sesion);
    }

    //tomar asistencia
    @PostMapping("/{idSesion}/asistencia")
    public ResponseEntity<SesionDetalletDto> tomarAsistencia(@PathVariable Long idSesion, @Valid @RequestBody List<AsistenciaDto> listaAsistencia) {
        return ResponseEntity.ok(sesionService.tomarAsistencia(idSesion, listaAsistencia));
    }

    //detalles de asistencia por grupo
    @GetMapping("/asistencia/{idSesion}")
    public ResponseEntity<SesionDetalletDto>obtenerDetalleSesion(@PathVariable @Min(1) Long idSesion){
        return ResponseEntity.ok(sesionService.obtenerDetalleSesion(idSesion));
    }

    //consultar sesiones por grupo
    @GetMapping("/porGrupo/{idGrupo}")
    public ResponseEntity<List<SesionDto>>obtenerSesionesPorGrupo(@PathVariable @Min(1) Long idGrupo){
        return ResponseEntity.status(HttpStatus.OK)
                .body(sesionService.obtenerSesionesPorGrupo(idGrupo));
    }


    //consultar sesiones por docente
    @GetMapping("/porDocente/{idDocente}")
    public ResponseEntity<List<SesiondocenteDto>>obtenerSesionesPorDocente(@PathVariable @Min(1) Long idDocente){
        return ResponseEntity.status(HttpStatus.OK)
                .body(sesionService.obtenerSesionPorDocente(idDocente));
    }


    //consultar sesiones por alumno
    @GetMapping("/porAlumno/{idInscripcion}")
    public ResponseEntity<List<SesiondocenteDto>>obtenerSesionesPorAlumno(@PathVariable @Min(1) Long idInscripcion){
        return ResponseEntity.status(HttpStatus.OK)
                .body(sesionService.obtenerSesionPorAlumno(idInscripcion));
    }

    //actualizar detalles de la sesion
    @PutMapping("/actualizar/{idSesion}")
    public ResponseEntity<SesionDetalletDto> actualizarDetallesSesion(@PathVariable Long idSesion, @Valid @RequestBody SesionUpdateDto dto){
        return ResponseEntity.status(HttpStatus.OK)
                .body(sesionService.actualizarSesion(idSesion, dto));
    }

    @GetMapping("/grupos/asistencias/{idGrupo}")
    public ResponseEntity<List<ReporteAsistenciaGrupoDto>> reporteAsistenciaGrupo(@PathVariable Long idGrupo) {
        return ResponseEntity.ok(sesionService.obtenerReporteAsistenciaPorGrupo(idGrupo));
    }

    @GetMapping("/docente/{id}/sesiones")
    public ResponseEntity<Page<SesiondocenteDto>> listar(@PathVariable Long id, SesionFiltro filtro,@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "6") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(sesionService.obtenerSesionesFiltradas(id, filtro, pageable));
    }

    @GetMapping("/alumno/{id}/sesiones")
    public ResponseEntity<Page<SesionDetalleAlumnoDto>> alumnoListar(@PathVariable Long id, SesionFiltro filtro, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "6") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(sesionService.obtenerSesionesFiltradasAlumno(id, filtro, pageable));
    }
}
