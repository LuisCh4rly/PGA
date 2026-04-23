package com.app.pga.App.Services.Interfaces;

import com.app.pga.App.Models.Dtos.RequestDto.ActividadBaseRequestDto;
import com.app.pga.App.Models.Dtos.ResponseDto.ActividadBaseResponseDto;
import com.app.pga.App.Models.Filtros.ActividadBaseFiltro;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IActividadBaseService {

    ActividadBaseResponseDto crearActividadBase (ActividadBaseRequestDto actividadBaseRequestDto, MultipartFile archivo);
    ActividadBaseResponseDto obtenerActividadBase (Long idActividadBase);


    List<ActividadBaseResponseDto> obtenerActividadesBaseActivas ();
    List<ActividadBaseResponseDto> obtenerActividadesBasePorCampo (Long idCampo);

    ActividadBaseResponseDto actualizarActividadBase( ActividadBaseRequestDto actividadBaseRequestDto, Long idActividadBase, MultipartFile archivo);

    ActividadBaseResponseDto habitarDeshabilitar(Long idActividadBase);

    List<ActividadBaseResponseDto> habitarDeshabilitarPorCampo (Long idCampo,boolean estado);


    Page<ActividadBaseResponseDto> obtenerActividadesBaseGeneral (ActividadBaseFiltro filtro, Pageable pageable);

    Resource visualizarInstrucciones(Long idActividadBase);
}
