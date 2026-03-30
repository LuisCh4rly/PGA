package com.app.pga.App.Models.Filtros;

import com.app.pga.App.Models.Enum.Alcance;
import com.app.pga.App.Models.Enum.MomentoSesion;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SesionFiltro {
    private Long idUsuario;
    private MomentoSesion momentoSesion;
    private Alcance alcance;
}
