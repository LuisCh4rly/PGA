package com.app.pga.App.Models.Entities;

import com.app.pga.App.Models.Enum.Alcance;
import com.app.pga.App.Models.Enum.Origen;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(
        name = "actividades_grupos",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"titulo", "id_grupo"})
        })
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ActividadGrupo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idActividadGrupo;

    @Column(unique = false)
    private String titulo;

    private String descripcion;
    @JsonFormat (pattern = "dd/MM/yyyy")
    private LocalDate fechaAsignacion;
    private Boolean reqEntrega;

    @Enumerated(EnumType.STRING)
    private Alcance alcance;

    @Enumerated(EnumType.STRING)
    private Origen origen;

    String campo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_grupo", referencedColumnName = "idGrupo")
    private Grupo grupo;

}
