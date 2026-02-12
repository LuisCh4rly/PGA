package com.app.pga.Auth.Service;

import com.app.pga.Auth.Models.Dtos.Request.AuthRegisterDto;
import com.app.pga.Auth.Models.Dtos.Response.CuentaResponseDto;
import com.app.pga.Auth.Models.Entities.Cuenta;

import java.util.List;

public interface ICuentaService {
    CuentaResponseDto register(AuthRegisterDto dto);

    List<CuentaResponseDto> listarCuentas();
    CuentaResponseDto cuentaById (Long id);
}
