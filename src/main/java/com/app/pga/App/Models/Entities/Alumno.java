package com.app.pga.App.Models.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "alumnos")
public class Alumno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAlumno;
    private String universidad;
    private String carrera;
    private LocalDate fechaTermino;
    private LocalDate fechaAlta;
    private LocalDate fechaBaja;
    private Boolean activo;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUsuario")
    private Usuario usuario;
}
