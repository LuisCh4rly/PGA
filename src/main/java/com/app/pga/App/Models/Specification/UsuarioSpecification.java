package com.app.pga.App.Models.Specification;

import com.app.pga.App.Models.Entities.Inscripcion;
import com.app.pga.App.Models.Entities.Usuario;
import com.app.pga.App.Models.Filtros.InscripcionFiltro;
import com.app.pga.App.Models.Filtros.UsuarioFiltro;
import com.app.pga.Auth.Models.Entities.Cuenta;
import com.app.pga.Auth.Models.Entities.Role;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UsuarioSpecification {
    public static Specification<Usuario> filtrar(UsuarioFiltro filtro) {
        //funcion lambda que utiliza tres cosas
        //root-< entidad Similar a decir From "inscripcion"
        //query -> en caso de hacer Disbtinct, group by u ordenamiento especial
        // constructor de construcciones
        return (root, query, criteriaBuilder) -> {
            //un predicate es una condicion por ejemplo, where estado = activo
            List<Predicate> predicates = new ArrayList<>();
            //primer filtro por estado
            if (filtro.getEstado() != null) {
                predicates.add(
                        criteriaBuilder.equal(root.get("activo"), filtro.getEstado())
                );
            }
            //tercer tipo por nombre
            if (filtro.getNombre() != null) {
                predicates.add(
                        criteriaBuilder.like(
                                //ACCEDE A Usuario.NOMBRE
                                criteriaBuilder.concat(
                                        criteriaBuilder.lower(root.get("nombre")),
                                        criteriaBuilder.concat(
                                                criteriaBuilder.lower(root.get("apellidoPaterno")),
                                                criteriaBuilder.lower(root.get("apellidoMaterno")))
                                ),
                                //LO COMPARA CON EL NOMBRE DEL FILTRO. %% PARA BUSCAR COINCIDENCIAS PARCIALES
                                "%" + filtro.getNombre().toLowerCase().trim().replaceAll("\\s+", "") + "%"
                        )
                );
            }
            if (filtro.getRole() != null) {
                Join< Usuario, Cuenta> cuentaJoin = root.join("cuenta");
                Join <Cuenta, Role> roleJoin = cuentaJoin.join("role");
                predicates.add(
                        criteriaBuilder.equal(roleJoin.get("name"), filtro.getRole())
                );
            }
            //juntar todas las condiciones con un "AND"
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            };
        }
    }


