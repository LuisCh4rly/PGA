package com.app.pga.App.Models.Entities;

import com.app.pga.App.Models.Enum.AlcanceEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name= "actividades_grupo")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ActividadGrupo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idActividadGrupo;
    @Column(unique = true)
    private String titulo;
    private String descripcion;
    private LocalDate fechaEntrega;


    private Boolean reqEntrega = true;

    @Enumerated (EnumType.STRING)
    private AlcanceEnum alcance;

    @ManyToOne()
    @JoinColumn(name = "id_grupo", referencedColumnName = "idGrupo")
    private  Grupo grupo;




}


