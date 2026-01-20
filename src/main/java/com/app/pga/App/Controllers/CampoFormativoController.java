package com.app.pga.App.Controllers;

import com.app.pga.App.Models.Dtos.CampoFormativoDto;
import com.app.pga.App.Services.Interfaces.ICampoFormativoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/campo")
public class CampoFormativoController {

    private final ICampoFormativoService campoFormativoService;


    @PostMapping
    public ResponseEntity<CampoFormativoDto> crearCampo (@RequestBody @Valid CampoFormativoDto dto){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(campoFormativoService.crearCampo(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CampoFormativoDto> obtenerCampo (@PathVariable Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(campoFormativoService.obtenerCampo(id));
    }
    @GetMapping()
    public ResponseEntity< List <CampoFormativoDto> > obtenerCampos (){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(campoFormativoService.obtenerCampos());
    }
    @GetMapping("/activos")
    public ResponseEntity< List <CampoFormativoDto> > obtenerCamposActivos (){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(campoFormativoService.obtenerCamposActivos());
    }
    @PutMapping("/{id}")
    public ResponseEntity< CampoFormativoDto > actualizarCampo (@PathVariable Long id , @RequestBody @Valid CampoFormativoDto campoFormativoDto){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(campoFormativoService.actualizarCampo(campoFormativoDto, id));
    }

    @PutMapping("habilitarDeshabilitar/{id}")
    public ResponseEntity< CampoFormativoDto > habilitarDeshabilitar (@PathVariable Long id ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(campoFormativoService.habitarDeshabilitar(id));
    }








}
