package com.app.pga.App.Services.Interfaces;

import com.app.pga.App.Models.Dtos.RequestDto.ActividadBaseRequestDto;
import com.app.pga.App.Models.Dtos.ResponseDto.ActividadBaseResponseDto;

import java.util.List;

public interface IActividadBaseService {

    ActividadBaseResponseDto crearActividadBase (ActividadBaseRequestDto actividadBaseRequestDto);
    ActividadBaseResponseDto obtenerActividadBase (Long idActividadBase);
    List<ActividadBaseResponseDto> obtenerActividadesBaseGeneral ();

    List<ActividadBaseResponseDto> obtenerActividadesBaseActivas ();
    List<ActividadBaseResponseDto> obtenerActividadesBasePorCampo (Long idCampo);
    ActividadBaseResponseDto actualizarActividadBase( ActividadBaseRequestDto actividadBaseRequestDto, Long idActividadBase);

    ActividadBaseResponseDto habitarDeshabilitar(Long idActividadBase);
    List<ActividadBaseResponseDto> habitarDeshabilitarPorCampo (Long idCampo,boolean estado);
}
