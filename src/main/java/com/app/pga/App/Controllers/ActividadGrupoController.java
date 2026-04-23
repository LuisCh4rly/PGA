package com.app.pga.App.Controllers;

import com.app.pga.App.Models.Dtos.*;
import com.app.pga.App.Models.Dtos.RequestDto.AgregarInscripcionesDto;
import com.app.pga.App.Models.Dtos.RequestDto.AsignarActividadCatalogoDto;
import com.app.pga.App.Models.Dtos.RequestDto.AsignarActividadExtraDto;
import com.app.pga.App.Models.Dtos.ResponseDto.ActividadGrupoDashboardDto;
import com.app.pga.App.Models.Entities.ActividadGrupo;
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





}
