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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grupo")
@RequiredArgsConstructor
public class ActividadGrupoController {

    private final IActividadGrupoService actividadGrupoService;
    @PostMapping("/{idGrupo}/actividades/catalogo")
    public ResponseEntity<ActividadGrupoDto> asignarDedeCatalogo (@PathVariable Long idGrupo, @RequestBody @Valid AsignarActividadCatalogoDto dto){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(actividadGrupoService.asignarDesdeCatalogo(idGrupo, dto));
    }

    @PostMapping("/{idGrupo}/actividades/extra")
    public ResponseEntity<ActividadGrupoDto> asignarExtra (@PathVariable Long idGrupo, @RequestBody @Valid AsignarActividadExtraDto dto){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(actividadGrupoService.crearExtra(idGrupo, dto));
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




}
