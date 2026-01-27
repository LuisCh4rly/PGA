package com.app.pga.App.Controllers;

import com.app.pga.App.Models.Dtos.ActividadBaseDto;
import com.app.pga.App.Models.Dtos.GrupoDto;
import com.app.pga.App.Repositories.IGrupoRepository;
import com.app.pga.App.Services.Interfaces.IGrupoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grupo")
@RequiredArgsConstructor
class GrupoController {
    private final IGrupoService grupoService;

    @PostMapping
    public ResponseEntity<GrupoDto> crearActividadBase (@RequestBody @Valid GrupoDto dto){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(grupoService.crear(dto));
    }
    @GetMapping("/{id}")
    public ResponseEntity <GrupoDto> obtenerGrupo(@PathVariable Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(grupoService.obtenerPorId(id));
    }
    @GetMapping()
    public ResponseEntity<List<GrupoDto>> obtenerGruposGeneral(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(grupoService.obtenerGrupoGeneral());
    }
    @GetMapping("/porCurso/{id}")
    public ResponseEntity<List<GrupoDto>> obtenerGruposPorCurso(@PathVariable Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(grupoService.obtenerPorCurso(id));
    }

    @GetMapping("/porDocente/{id}")
    public ResponseEntity<List<GrupoDto>> obtenerGruposPorDocente(@PathVariable Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(grupoService.obtenerPorDocente(id));
    }

    @PutMapping("habilitarDeshabilitar/{id}")
    public ResponseEntity< GrupoDto > habilitarDeshabilitarActividadBase (@PathVariable Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(grupoService.cambiarEstado(id));
    }

    @PutMapping("cambiarDocente")
    public ResponseEntity< GrupoDto > cambiarDocente (@RequestBody @Valid GrupoDto dto){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(grupoService.cambiarDocente(dto));
    }



}
