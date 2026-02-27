package com.app.pga.App.Models.Filtros;
import com.app.pga.App.Models.Enum.Estado;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GrupoFiltro {//clase simple que captura los datos que vienen del usuario
    private String docente;
    private String curso;
    private Estado estado;
}
