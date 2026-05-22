package com.app.pga.App.Controllers;

import com.app.pga.App.Models.Dtos.RequestDto.ObservacionesExpedienteDto;
import com.app.pga.App.Models.Dtos.ResponseDto.ExpedienteReporteDto;
import com.app.pga.App.Models.Dtos.ResponseDto.ExpedienteResponseDto;
import com.app.pga.App.Models.Dtos.ResponseDto.InscripcionReporteDto;
import com.app.pga.App.Services.Interfaces.IExpedienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expedientes")
@RequiredArgsConstructor
class ExpedienteController {

    private final IExpedienteService expedienteService;

    @GetMapping("alumnos/{idAlumno}")
    public ResponseEntity<ExpedienteResponseDto> obtenerExpedienteporAlumno(@PathVariable Long idAlumno) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(expedienteService.verExpediente(idAlumno));
    }

    @PutMapping("/{idExpediente}/observaciones")
    public ResponseEntity< ExpedienteResponseDto> agregarObservaciones (@PathVariable Long idExpediente ,
                                                                       @RequestBody @Valid ObservacionesExpedienteDto dto){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(expedienteService.agregarObservaciones(idExpediente, dto.observacion()));
    }


}
