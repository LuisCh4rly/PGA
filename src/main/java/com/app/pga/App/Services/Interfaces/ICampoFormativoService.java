package com.app.pga.App.Services.Interfaces;

import com.app.pga.App.Models.Dtos.CampoFormativoDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ICampoFormativoService {

    CampoFormativoDto crearCampo (CampoFormativoDto campoFormativoDto);
    CampoFormativoDto obtenerCampo (Long idCampo);
    List<CampoFormativoDto> obtenerCampos ();
    List<CampoFormativoDto> obtenerCamposActivos ();
    CampoFormativoDto actualizarCampo( CampoFormativoDto campoFormativoDto, Long id);
    CampoFormativoDto habitarDeshabilitar(Long id);

}
