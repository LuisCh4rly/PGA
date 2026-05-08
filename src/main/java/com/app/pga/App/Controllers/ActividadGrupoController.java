package com.app.pga.App.Controllers;

import com.app.pga.App.Models.Dtos.*;
import com.app.pga.App.Models.Dtos.RequestDto.*;
import com.app.pga.App.Models.Dtos.ResponseDto.ActividadGrupoAgrupadaDto;
import com.app.pga.App.Models.Dtos.ResponseDto.ActividadGrupoDashboardDto;
import com.app.pga.App.Models.Dtos.ResponseDto.GrupoResumenDto;
import com.app.pga.App.Services.Interfaces.IActividadGrupoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/grupo")
@RequiredArgsConstructor
public class ActividadGrupoController {

    private final IActividadGrupoService actividadGrupoService;
    @PostMapping("/{idGrupo}/actividades/catalogo")
    public ResponseEntity<ActividadGrupoDto> asignarDedeCatalogo (@PathVariable Long idGrupo,
                                                                  @RequestPart("dto") @Valid AsignarActividadCatalogoDto dto,
                                                                  @RequestPart(value = "archivo", required = false) MultipartFile archivo){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(actividadGrupoService.asignarDesdeCatalogo(idGrupo, dto, archivo));
    }

    @PostMapping("/{idGrupo}/actividades/extra")
    public ResponseEntity<ActividadGrupoDto> asignarExtra (@PathVariable Long idGrupo,
                                                           @RequestPart("dto") @Valid AsignarActividadExtraDto dto,
                                                           @RequestPart(value = "archivo") MultipartFile archivo){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(actividadGrupoService.crearExtra(idGrupo, dto, archivo));
    }
    @GetMapping("/{idGrupo}/actividades")
    public ResponseEntity<List<ActividadGrupoDto>> obtenerActividadesGrupo(@PathVariable Long idGrupo){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body( actividadGrupoService.obtenerActividadesPorGrupo(idGrupo));
    }
    @GetMapping("/{idGrupo}/actividades/grupales")
    public ResponseEntity<List<ActividadGrupoDto>> obtenerActividadesGrupales(@PathVariable Long idGrupo){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body( actividadGrupoService.obtenerActividadesGrupales(idGrupo));
    }
    @GetMapping("/{idGrupo}/actividades/individuales")
    public List<ActividadGrupoDto> obtenerActividadesIndividuales(@PathVariable Long idGrupo){
        return actividadGrupoService.obtenerActividadesIndividuales(idGrupo);
    }

    @PostMapping("/actividad-grupo/{idActividadGrupo}/inscripciones")
    public void agregarInscripcionesActividad( @PathVariable Long idActividadGrupo,  @RequestBody AgregarInscripcionesDto dto){
        actividadGrupoService.agregarInscripcionesActividad(idActividadGrupo, dto.idsInscripciones()
        );
    }
    @GetMapping("/actividad/{idActividad}")
    public ResponseEntity<ActividadGrupoDashboardDto> obtenerPorId (@PathVariable Long idActividad){
       return ResponseEntity
               .status(HttpStatus.OK)
               .body(actividadGrupoService.obtenerActividadPorId(idActividad));
    }

    @GetMapping("actividad/{idActividad}/instrucciones")
    public ResponseEntity<Resource> verInstrucciones(@PathVariable Long idActividad) {

        Resource archivo = actividadGrupoService.visualizarInstrucciones(idActividad);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + archivo.getFilename() + "\"")
                .body(archivo);
    }

    @PutMapping ("/actividad/{idActividad}/actualizar")
    public ResponseEntity <ActividadGrupoDto> actualizarInstruccionesPorId (@PathVariable Long idActividad,
                                                                            @RequestParam(value =  "archivo", required =false) MultipartFile archivo){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(actividadGrupoService.actualizarInstruccionesPorId(idActividad,archivo));
    }

    @GetMapping("{idDocente}/actividad")
    public ResponseEntity<List<GrupoResumenDto>> obtenerGruposSinActividad (@RequestParam("titulo") String titulo, @PathVariable Long idDocente){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(actividadGrupoService.obtenerGruposSinActividad(titulo,idDocente));
    }

    @PostMapping("/actividades/catalogo/multiples")
    public  ResponseEntity <Void> asignarasignarDesdeCatalogoMultiple ( @RequestPart("dto") @Valid AsignarActividadCatalogoMultipleDto dto,
                                                                        @RequestPart(value = "archivo", required = false) MultipartFile archivo){
        actividadGrupoService.asignarDesdeCatalogoMultiple(dto,archivo);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @PostMapping("/actividades/extra/multiples")
    public  ResponseEntity <Void> asignarExtraMultiple (@RequestPart("dto") @Valid AsignarActividadExtraMultipleDto dto,
                                                         @RequestPart(value = "archivo") MultipartFile archivo){
        actividadGrupoService.crearExtraMultiple(dto,archivo);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/docente/{idDocente}/actividades")
    public ResponseEntity<Set<ActividadGrupoAgrupadaDto>> obtenerActividadesPorGrupos (@PathVariable Long idDocente){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(actividadGrupoService.obtenerActividadesPorGrupos(idDocente));
    }

    @PutMapping("actividades/instrucciones")
    public ResponseEntity<Void> actualizarMultiples (@RequestPart List<Long> idActividades,
                                                     @RequestPart MultipartFile archivo){
        actividadGrupoService.actualizarInstruccionesMultiple(idActividades, archivo);
        return ResponseEntity.ok().build();
    }

    }
