package com.app.pga.App.Controllers;

import com.app.pga.App.Models.Dtos.RequestDto.CampoRequestDto;
import com.app.pga.App.Models.Dtos.ResponseDto.CampoResponseDto;
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
    public ResponseEntity<CampoResponseDto> crearCampo (@RequestBody @Valid CampoRequestDto campoRequestDto){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(campoFormativoService.crearCampo(campoRequestDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CampoResponseDto > obtenerCampo (@PathVariable Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(campoFormativoService.obtenerCampo(id));
    }
    @GetMapping()
    public ResponseEntity< List <CampoResponseDto > > obtenerCampos (){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(campoFormativoService.obtenerCampos());
    }
    @GetMapping("/activos")
    public ResponseEntity< List <CampoResponseDto > > obtenerCamposActivos (){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(campoFormativoService.obtenerCamposActivos());
    }
    @PutMapping("/{id}")
    public ResponseEntity< CampoResponseDto  > actualizarCampo (@PathVariable Long id , @RequestBody @Valid CampoRequestDto campoRequestDto){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(campoFormativoService.actualizarCampo(campoRequestDto, id));
    }

    @PutMapping("habilitarDeshabilitar/{id}")
    public ResponseEntity< CampoResponseDto  > habilitarDeshabilitar (@PathVariable Long id ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(campoFormativoService.habitarDeshabilitar(id));
    }








}
