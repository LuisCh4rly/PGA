package com.app.pga.App.Controllers;

import com.app.pga.App.Models.Dtos.DocenteDto;
import com.app.pga.App.Services.Implements.DocenteService;
import com.app.pga.App.Services.Implements.UsuarioService;
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

    public DocenteController(DocenteService docenteService, UsuarioService usuarioService){this.docenteService=docenteService;}

    //--------crear-------
    @PostMapping
    public ResponseEntity<DocenteDto>createDocente(@Valid @RequestBody DocenteDto docenteDto, UriComponentsBuilder uriComponentsBuilder){
        DocenteDto createDocente = docenteService.createDocente(docenteDto);
        URI location = uriComponentsBuilder.path("/api/docente/{id}").buildAndExpand(createDocente.getClass()).toUri();

        return ResponseEntity.created(location).body(createDocente);
    }

    //------consulta por id----
    @GetMapping("/{id}")
    public ResponseEntity<DocenteDto>consultaID(@PathVariable @Min(1) Long id){
        return ResponseEntity.ok(docenteService.findById(id));
    }

    //------consulta para activos----
    @GetMapping("/activos")
    public ResponseEntity<List<DocenteDto>>consultaActivos(){
        return ResponseEntity.ok(docenteService.findAllActivos());
    }


    //------consulta----
    @GetMapping
    public ResponseEntity<List<DocenteDto>>consulta(){
        return ResponseEntity.ok(docenteService.findAll());
    }


    //------activar desactivar-------
    @PutMapping("/desactivar/{id}")
    public ResponseEntity<DocenteDto>desactivarActivar(@PathVariable @Min(1) Long id){
        DocenteDto docenteActDes = docenteService.desactivarActivarDocente(id);
        return ResponseEntity.ok(docenteActDes);
    }
}
