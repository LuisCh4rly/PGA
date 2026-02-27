package com.app.pga.App.Controllers;

import com.app.pga.App.Models.Dtos.RequestDto.GrupoRequestDto;
import com.app.pga.App.Models.Dtos.ResponseDto.GrupoResponseDto;
import com.app.pga.App.Models.Filtros.GrupoFiltro;
import com.app.pga.App.Services.Interfaces.IGrupoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<GrupoResponseDto> crearGrupo (@RequestBody @Valid GrupoRequestDto dto){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(grupoService.crear(dto));
    }
    @GetMapping("/{id}")
    public ResponseEntity <GrupoResponseDto> obtenerGrupo(@PathVariable Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(grupoService.obtenerPorId(id));
    }
    @GetMapping()
    public ResponseEntity<List<GrupoResponseDto>> obtenerGruposGeneral(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(grupoService.obtenerGrupoGeneral());
    }
    @GetMapping("/porCurso/{id}")
    public ResponseEntity<List<GrupoResponseDto>> obtenerGruposPorCurso(@PathVariable Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(grupoService.obtenerPorCurso(id));
    }

    @GetMapping("/porDocente/{id}")
    public ResponseEntity<List<GrupoResponseDto>> obtenerGruposPorDocente(@PathVariable Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(grupoService.obtenerPorDocente(id));
    }

    @PutMapping("habilitarDeshabilitar/{id}")
    public ResponseEntity< GrupoResponseDto > habilitarDeshabilitarGrupo (@PathVariable Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(grupoService.cambiarEstado(id));
    }

    @PutMapping("/{idGrupo}/cambiarDocente/{idDocente}")
    public ResponseEntity< GrupoResponseDto > cambiarDocente (@PathVariable Long idGrupo, @PathVariable Long idDocente ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(grupoService.cambiarDocente(idGrupo, idDocente));
    }

    //consulta general con paginacion
    @GetMapping("/listPage")
    public ResponseEntity<Page<GrupoResponseDto>> consultaGeneralPage(@ModelAttribute GrupoFiltro filtro, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "6") int size){
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(grupoService.findAll(filtro, pageable));
    }

    //consulta de grupos activos
    @GetMapping("/activos")
    public ResponseEntity<List<GrupoResponseDto>> obtenerGruposActivos(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(grupoService.obtenerGruposActivos());
    }



}
