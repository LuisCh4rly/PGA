package com.app.pga.App.Models.Entities;

import com.app.pga.App.Models.Enum.tipoInscripcion;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "inscripciones")
public class Inscripcion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idInscripcion;
    private LocalDate fechaInscripcion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private LocalDate fechaBaja;
    private Boolean estado;
    private String escuela;
    private String nivelEstudio;
    private String carrera;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private tipoInscripcion tipo;
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name="idUsuario")
    private Usuario usuario;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_grupo", referencedColumnName = "idGrupo")
    private Grupo grupo;
}
