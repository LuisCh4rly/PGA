package com.app.pga.App.Models.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Entity
@Table (name= "actividades_base")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ActividadBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idActividad;
    @Column(unique = true)
    private String titulo;
    private String descripcion;
    @Column(nullable = false)
    private Boolean activo = true;

    @ManyToOne ( fetch = FetchType.LAZY)
    @JoinColumn(name = "id_CampoFormativo", referencedColumnName = "idCampo")
    private CampoFormativo campoFormativo;

    @OneToMany(mappedBy = "actividadBase")
    private List< Curso_ActividadBase > cursos;

    private String urlInstrucciones;



}
