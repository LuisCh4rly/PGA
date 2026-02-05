package com.app.pga.App.Models.Enum;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

public enum EstadoTarea {
    Sin_Iniciar ("SIN INICIAR"),
    En_Progreso("EN PROGRESO"),
    En_Espera ("EN ESPERA"),
    Completada("COMPLETADA"),
    Exenta ("EXENTA"),
    Aprobada ("APROBADA"),
    Incompleta ("INCOMPLETA");

    private final String Estado;

    EstadoTarea(String Estado){
        this.Estado=Estado;
    }

    @JsonCreator
    public static EstadoTarea fromEstado(String value) {
        return Arrays.stream(values())
                .filter(v -> v.Estado.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Tipo de Estado de Tarea inválido"));
    }
    public String getEstado(){return Estado;}
}
