package com.app.pga.App.Models.Entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "sesion_alumno")
public class SesionAlumno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSesionAlumno;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idInscripcion", referencedColumnName = "idInscripcion")
    private Inscripcion inscripcion;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idSesion", referencedColumnName = "idSesion")
    private Sesion sesion;
    @OneToOne(mappedBy = "sesionAlumno", cascade = CascadeType.ALL)
    private Asistencia asistencia;
}
