package com.app.pga.App.Controllers;

import com.app.pga.App.Models.Dtos.ActividadBaseDto;
import com.app.pga.App.Models.Dtos.CampoFormativoDto;
import com.app.pga.App.Services.Interfaces.IActividadBaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<ActividadBaseDto> crearActividadBase (@RequestBody @Valid ActividadBaseDto dto){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(actividadBaseService.crearActividadBase(dto));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ActividadBaseDto> obtenerActividadBase (@PathVariable Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(actividadBaseService.obtenerActividadBase(id));
    }
    @GetMapping()
    public ResponseEntity<List<ActividadBaseDto>> obtenerActividades (){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(actividadBaseService.obtenerActividadesBaseGeneral());
    }
    @GetMapping ("/activas")
    public ResponseEntity< List <ActividadBaseDto> > obtenerActividadesBaseActivas (){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(actividadBaseService.obtenerActividadesBaseActivas());
    }
    @GetMapping ("campo/{idCampo}")
    public ResponseEntity< List <ActividadBaseDto> > obtenerActividadesBasePorCampo(@PathVariable Long idCampo ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(actividadBaseService.obtenerActividadesBasePorCampo(idCampo));
    }
    @PutMapping("/{id}")
    public ResponseEntity< ActividadBaseDto > actualizarActividadBase (@PathVariable Long id ,
                                                                        @RequestBody @Valid ActividadBaseDto actividadBaseDto){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(actividadBaseService.actualizarActividadBase(actividadBaseDto, id));
    }
    @PutMapping("habilitarDeshabilitar/{id}")
    public ResponseEntity< ActividadBaseDto > habilitarDeshabilitarActividadBase (@PathVariable Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(actividadBaseService.habitarDeshabilitar(id));
    }

}
