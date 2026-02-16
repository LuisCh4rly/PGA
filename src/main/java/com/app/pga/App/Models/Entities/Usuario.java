package com.app.pga.App.Models.Entities;

import com.app.pga.Auth.Models.Entities.Cuenta;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private LocalDate created_At;
    private String telefono;
    private String direccion;
    private Boolean activo;
    @OneToOne(mappedBy ="usuario")
    private Cuenta cuenta;
    private LocalDate fechaAlta;
    private LocalDate fechaBaja;
}
