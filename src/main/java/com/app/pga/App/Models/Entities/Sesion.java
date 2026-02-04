package com.app.pga.App.Models.Entities;


import com.app.pga.App.Models.Enum.Alcance;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "sesiones")
public class Sesion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSesion;
    private LocalDateTime fecha;
    private String tema;
    @Column (name = "url_sesion")
    private String urlSesion;
    @Enumerated (EnumType.STRING)
    private Alcance alcance;
    @Builder.Default
    @OneToMany(mappedBy = "sesion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SesionAlumno> sesionAlumnos = new ArrayList<>();
    private String plataforma;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idGrupo", referencedColumnName = "idGrupo")
    private Grupo grupo;
}

