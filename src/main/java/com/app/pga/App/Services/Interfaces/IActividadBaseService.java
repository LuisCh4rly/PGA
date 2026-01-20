package com.app.pga.App.Services.Interfaces;

import com.app.pga.App.Models.Dtos.ActividadBaseDto;
import com.app.pga.App.Models.Dtos.CampoFormativoDto;

import java.util.List;

public interface IActividadBaseService {

    ActividadBaseDto crearActividadBase (ActividadBaseDto actividadBaseDto);
    ActividadBaseDto obtenerActividadBase (Long idActividadBase);
    List<ActividadBaseDto> obtenerActividadesBaseGeneral ();

    List<ActividadBaseDto> obtenerActividadesBaseActivas ();
    List<ActividadBaseDto> obtenerActividadesBasePorCampo (Long idCampo);
    ActividadBaseDto actualizarActividadBase( ActividadBaseDto actividadBaseDto, Long idActividadBase);

    ActividadBaseDto habitarDeshabilitar(Long idActividadBase);
    List<ActividadBaseDto> habitarDeshabilitarPorCampo (Long idCampo,boolean estado);
}
