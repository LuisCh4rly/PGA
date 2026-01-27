package com.app.pga.App.Models.Entities;

import com.app.pga.App.Models.Enum.EstadoEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name= "grupos")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class Grupo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idGrupo;
    private String nombre;

    @Enumerated(EnumType.STRING)
    private EstadoEnum estado;

    private LocalDate created_at;
    private String periodo;

    @ManyToOne()
    @JoinColumn(name="id_curso", referencedColumnName = "idCurso")
    private Curso curso;

    @ManyToOne()
    @JoinColumn(name="id_docente", referencedColumnName = "idDocente")
    private Docente docente;


}
