package com.app.pga.App.Models.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;


@Entity
@Table(name= "cursos")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCurso;
    private String nombre;
    private String descripcion;
    private LocalDate fechaAlta;
    private LocalDate fechaBaja;
    private Boolean activo;

    @OneToMany(
            mappedBy = "curso",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Curso_ActividadBase> actividades;


}

