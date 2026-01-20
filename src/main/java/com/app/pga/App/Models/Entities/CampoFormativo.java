package com.app.pga.App.Models.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;


@Entity
@Table (name= "campos_formativos")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class CampoFormativo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCampo;
    @Column(unique = true)
    private String nombre;
    private String descripcion;
    @Column(nullable = false)
    private Boolean activo = true;



}
