package com.app.pga.App.Controllers;


import com.app.pga.App.Models.Dtos.RequestDto.DocenteRequestDto;
import com.app.pga.App.Models.Dtos.ResponseDto.DocenteResponseDto;
import com.app.pga.App.Services.Implements.DocenteService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/docente")
@Validated
public class DocenteController {
    private final DocenteService docenteService;

    public DocenteController(DocenteService docenteService){this.docenteService=docenteService;}

    //--------crear-------
  /*  @PostMapping
    public ResponseEntity<DocenteResponseDto>createDocente(@Valid @RequestBody DocenteRequestDto docenteRequestDto, UriComponentsBuilder uriComponentsBuilder){
        DocenteResponseDto createDocente = docenteService.createDocente(docenteRequestDto);
        URI location = uriComponentsBuilder.path("/api/docente/{id}")
                .buildAndExpand(createDocente.getClass()).toUri();

        return ResponseEntity.created(location).body(createDocente);
    }*/

    //------consulta por id----
    @GetMapping("/{id}")
    public ResponseEntity<DocenteResponseDto>consultaID(@PathVariable @Min(1) Long id){
        return ResponseEntity.ok(docenteService.findById(id));
    }

    //------consulta para activos----
    @GetMapping("/activos")
    public ResponseEntity<List<DocenteResponseDto>>consultaActivos(){
        return ResponseEntity.ok(docenteService.findAllActivos());
    }


    //------consulta----
    @GetMapping
    public ResponseEntity<List<DocenteResponseDto>>consulta(){
        return ResponseEntity.ok(docenteService.findAll());
    }


    //------activar desactivar-------
    @PutMapping("/desactivar/{id}")
    public ResponseEntity<DocenteResponseDto>desactivarActivar(@PathVariable @Min(1) Long id){
        DocenteResponseDto docenteActDes = docenteService.desactivarActivarDocente(id);
        return ResponseEntity.ok(docenteActDes);
    }
}
