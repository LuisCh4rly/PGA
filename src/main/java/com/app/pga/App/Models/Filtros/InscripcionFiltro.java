package com.app.pga.App.Models.Filtros;

import com.app.pga.App.Models.Enum.tipoInscripcion;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InscripcionFiltro {
    //colocar los campos por los cuales se desea filtrar
    private Boolean estado;
    private tipoInscripcion tipo;
    private String alumno;
    private Long idGrupo;


}
