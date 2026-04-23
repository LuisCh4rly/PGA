package com.app.pga.App.Controllers;

import com.app.pga.App.Models.Dtos.RequestDto.ActividadBaseRequestDto;
import com.app.pga.App.Models.Dtos.ResponseDto.ActividadBaseResponseDto;
import com.app.pga.App.Models.Filtros.ActividadBaseFiltro;
import com.app.pga.App.Services.Interfaces.IActividadBaseService;
import jakarta.mail.Multipart;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/actividadBase")
@RequiredArgsConstructor
class ActividadBaseController {
    private final IActividadBaseService actividadBaseService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ActividadBaseResponseDto> crearActividadBase (@RequestPart("actividadBaseRequestDto") @Valid ActividadBaseRequestDto actividadBaseRequestDto,
                                                                        @RequestPart MultipartFile archivo){

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(actividadBaseService.crearActividadBase(actividadBaseRequestDto, archivo));
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
    public ResponseEntity< ActividadBaseResponseDto > actualizarActividadBase (
            @PathVariable Long id ,
            @RequestPart("actividadBaseRequestDto") @Valid ActividadBaseRequestDto actividadBaseRequestDto,
            @RequestPart(value = "archivo", required = false) MultipartFile archivo){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(actividadBaseService.actualizarActividadBase(actividadBaseRequestDto, id, archivo));
    }
    @PutMapping("habilitarDeshabilitar/{id}")
    public ResponseEntity< ActividadBaseResponseDto > habilitarDeshabilitarActividadBase (@PathVariable @Min(1) Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(actividadBaseService.habitarDeshabilitar(id));
    }

    @GetMapping("/{idActividad}/instrucciones")
    public ResponseEntity<Resource> verInstrucciones(@PathVariable Long idActividad) {

        Resource archivo = actividadBaseService.visualizarInstrucciones(idActividad);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + archivo.getFilename() + "\"")
                .body(archivo);
    }
}


