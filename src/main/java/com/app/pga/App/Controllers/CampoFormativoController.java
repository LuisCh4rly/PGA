package com.app.pga.App.Controllers;

import com.app.pga.App.Models.Dtos.RequestDto.CampoRequestDto;
import com.app.pga.App.Models.Dtos.ResponseDto.CampoResponseDto;
import com.app.pga.App.Models.Filtros.CampoFiltro;
import com.app.pga.App.Services.Interfaces.ICampoFormativoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Campos Formativos", description = "Endpoints para la gestión de campos formativos")
@RequiredArgsConstructor
@RequestMapping("/api/campo")
public class CampoFormativoController {

    private final ICampoFormativoService campoFormativoService;

    @Operation(summary = "Crear campo formativo", description = "Registra un nuevo campo formativo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Campo formativo creado correctamente"),
            @ApiResponse(responseCode = "409", description = "El campo formativo ya existe")
    })
    @PostMapping
    public ResponseEntity<CampoResponseDto> crearCampo (@RequestBody @Valid CampoRequestDto campoRequestDto){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(campoFormativoService.crearCampo(campoRequestDto));
    }

    @Operation(summary = "Consultar campo formativo por ID", description = "Obtiene la información de un campo formativo específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Campo formativo no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CampoResponseDto > obtenerCampo (
            @Parameter(description = "Id campo formativo", example = "1") @PathVariable Long id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(campoFormativoService.obtenerCampo(id));
    }

    @Operation(summary = "Consultar campos formativos", description = "Obtiene una lista paginada de campos formativos aplicando filtros.")
    @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente")
    @GetMapping()
    public ResponseEntity<Page<CampoResponseDto >> obtenerCampos (CampoFiltro filtro, Pageable pageable){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(campoFormativoService.obtenerCampos(filtro, pageable));
    }
    @Operation(summary = "Consultar campos formativos activos", description = "Obtiene todos los campos formativos activos.")
    @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente")
    @GetMapping("/activos")
    public ResponseEntity< List <CampoResponseDto > > obtenerCamposActivos (){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(campoFormativoService.obtenerCamposActivos());
    }
    @Operation(summary = "Actualizar campo formativo", description = "Actualiza la información de un campo formativo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Campo formativo actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Campo formativo no encontrado"),
            @ApiResponse(responseCode = "409", description = "El campo formativo está deshabilitado")
    })
    @PutMapping("/{id}")
    public ResponseEntity< CampoResponseDto  > actualizarCampo (
            @Parameter(description = "Id campo formativo", example = "1") @PathVariable Long id ,
            @RequestBody @Valid CampoRequestDto campoRequestDto){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(campoFormativoService.actualizarCampo(campoRequestDto, id));
    }

    @Operation(summary = "Habilitar o deshabilitar campo formativo", description = "Cambia el estado de un campo formativo y actualiza las actividades asociadas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estado actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Campo formativo no encontrado")
    })
    @PutMapping("habilitarDeshabilitar/{id}")
    public ResponseEntity< CampoResponseDto  > habilitarDeshabilitar (
            @Parameter(description = "Id campo formativo", example = "1") @PathVariable Long id ){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(campoFormativoService.habitarDeshabilitar(id));
    }








}
