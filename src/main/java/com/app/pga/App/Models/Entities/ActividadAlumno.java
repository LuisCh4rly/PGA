package com.app.pga.App.Models.Entities;

import com.app.pga.App.Models.Enum.EstadoTarea;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name= "actividades_alumnos")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ActividadAlumno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idActividadAlumno;

    @Enumerated(EnumType.STRING)
    private EstadoTarea estadoTarea;

    private Boolean excento;
    private String motivoExencion;

    private String urlEntrega;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fechaEntrega;
    private String observaciones;
    private String comentarios;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_actividad_grupo", referencedColumnName = "idActividadGrupo")
    ActividadGrupo actividadGrupo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_inscripcion", referencedColumnName = "idInscripcion")
    Inscripcion inscripcion;



}
