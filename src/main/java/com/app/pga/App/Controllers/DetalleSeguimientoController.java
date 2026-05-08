package com.app.pga.App.Controllers;

import com.app.pga.App.Models.Dtos.RequestDto.DetalleSeguimientoAgrupadoDto;
import com.app.pga.App.Models.Dtos.RequestDto.DetalleSeguimientoRequestDto;
import com.app.pga.App.Models.Dtos.ResponseDto.DetalleDashboardDto;
import com.app.pga.App.Services.Interfaces.IDetalleSeguimientoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/detalles")
@RequiredArgsConstructor
@Transactional
class DetalleSeguimientoController {
    private final IDetalleSeguimientoService detalleSeguimientoService;

    @PostMapping()
    public ResponseEntity<List<DetalleDashboardDto>> crearDetalle (@RequestBody DetalleSeguimientoAgrupadoDto dto){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(detalleSeguimientoService.crearDetalleSeguimiento(dto));
    }



}
