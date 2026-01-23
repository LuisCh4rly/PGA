package com.app.pga.App.Models.Enum;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

public enum tipoInscripcion {
    Servicio_Social("SERVICIO SOCIAL"),
    Practicas_Profesionales("PRACTICAS PROFESIONALES"),
    Jovenes_Construyendo_El_Futuro("JOVENES CONSTRUYENDO EL FUTURO");

    private final String descripcion;

    tipoInscripcion(String descripcion){
        this.descripcion=descripcion;
    }

    @JsonCreator
    public static tipoInscripcion fromDescripcion(String value) {
        return Arrays.stream(values())
                .filter(v -> v.descripcion.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Tipo de inscripción inválido"));
    }
    public String getDescripcion(){return descripcion;}
}
