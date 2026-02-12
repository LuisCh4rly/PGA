package com.app.pga.Auth.Models.Entities;

import com.app.pga.Auth.Models.Enum.ERole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRole;


    @Enumerated(EnumType.STRING)
    private ERole name;

}

