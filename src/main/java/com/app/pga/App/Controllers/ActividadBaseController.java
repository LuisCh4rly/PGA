package com.app.pga.App.Controllers;

import com.app.pga.App.Models.Dtos.RequestDto.ActividadBaseRequestDto;
import com.app.pga.App.Models.Dtos.ResponseDto.ActividadBaseResponseDto;
import com.app.pga.App.Models.Filtros.ActividadBaseFiltro;
import com.app.pga.App.Services.Interfaces.IActividadBaseService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/actividadBase")
@RequiredArgsConstructor
class ActividadBaseController {
    private final IActividadBaseService actividadBaseService;

    @PostMapping
    public ResponseEntity<ActividadBaseResponseDto> crearActividadBase (@RequestBody @Valid ActividadBaseRequestDto actividadBaseRequestDto){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(actividadBaseService.crearActividadBase(actividadBaseRequestDto));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ActividadBaseResponseDto> obtenerActividadBase (@PathVariable @Min(1) Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(actividadBaseService.obtenerActividadBase(id));
    }
    @GetMapping()
    public ResponseEntity<Page<ActividadBaseResponseDto>> obtenerActividades (ActividadBaseFiltro filtro, Pageable pageable){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(actividadBaseService.obtenerActividadesBaseGeneral(filtro, pageable));
    }
    @GetMapping ("/activas")
    public ResponseEntity< List <ActividadBaseResponseDto> > obtenerActividadesBaseActivas (){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(actividadBaseService.obtenerActividadesBaseActivas());
    }
    @GetMapping ("campo/{idCampo}")
    public ResponseEntity< List <ActividadBaseResponseDto> > obtenerActividadesBasePorCampo(@PathVariable Long idCampo ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(actividadBaseService.obtenerActividadesBasePorCampo(idCampo));
    }
    @PutMapping("/{id}")
    public ResponseEntity< ActividadBaseResponseDto > actualizarActividadBase (@PathVariable Long id , @RequestBody @Valid ActividadBaseRequestDto  actividadBaseRequestDto){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(actividadBaseService.actualizarActividadBase(actividadBaseRequestDto, id));
    }
    @PutMapping("habilitarDeshabilitar/{id}")
    public ResponseEntity< ActividadBaseResponseDto > habilitarDeshabilitarActividadBase (@PathVariable @Min(1) Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(actividadBaseService.habitarDeshabilitar(id));
    }


}
