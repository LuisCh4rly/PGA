package com.app.pga.App.Models.Enum;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

public enum EstadoExpediente {

    APROBADO ("APROBADO"),
    NO_APROBADO ("NO APROBADO");

    private final String Estado;

    EstadoExpediente(String Estado){
        this.Estado=Estado;
    }

    @JsonCreator
    public static EstadoExpediente fromEstado(String value) {
        return Arrays.stream(values())
                .filter(v -> v.Estado.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Tipo de Estado de Expediente inválido"));
    }
    public String getEstado(){return Estado;}
}
