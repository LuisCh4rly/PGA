package com.app.pga.App.Models.Specification;

import com.app.pga.App.Models.Entities.Inscripcion;
import com.app.pga.App.Models.Entities.Usuario;
import com.app.pga.App.Models.Filtros.InscripcionFiltro;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;


public class InscripcionSpecification {

    //construir las rgelas de busqueda, recibe como parametros que nosotros determinamos para ser filtro y provienen del controller
    public static Specification <Inscripcion> filtrar (InscripcionFiltro filtro){
        //funcion lambda que utiliza tres cosas
        //root-< entidad Similar a decir From "inscripcion"
        //query -> en caso de hacer Disbtinct, group by u ordenamiento especial
        // constructor de construcciones
        return (root, query, criteriaBuilder) ->{
            //un predicate es una condicion por ejemplo, where estado = activo
            List<Predicate> predicates = new ArrayList<>();
            //primer filtro por estado
            if (filtro.getEstado() != null) {
                predicates.add(
                        criteriaBuilder.equal(root.get("estado"), filtro.getEstado())
                );
            }
            //segundo filtro por tipo de inscripcion
            if (filtro.getTipo() != null) {
                predicates.add(
                        criteriaBuilder.equal(root.get("tipo"), filtro.getTipo())
                );
            }
            //tercer tipo por nombre alumno
            if (filtro.getAlumno() != null) {
                //join de tablas usuario e inscripcion
                Join<Inscripcion, Usuario> usuarioJoin = root.join("usuario");
                predicates.add(
                        criteriaBuilder.like(
                                //ACCEDE A Usuario.NOMBRE
                                criteriaBuilder.concat(
                                criteriaBuilder.lower(usuarioJoin.get("nombre")),
                                        criteriaBuilder.concat(
                                                criteriaBuilder.lower(usuarioJoin.get("apellidoPaterno")),
                                                criteriaBuilder.lower(usuarioJoin.get("apellidoMaterno")))
                                        ),
                                //LO COMPARA CON EL NOMBRE DEL FILTRO. %% PARA BUSCAR COINCIDENCIAS PARCIALES
                                "%" + filtro.getAlumno().toLowerCase().trim().replaceAll("\\s+", "") + "%"
                        )
                );
            }
            //CUARTO FILTRO, POR GRUPO
            if (filtro.getIdGrupo() != null) {
                predicates.add(
                        criteriaBuilder.equal(
                                root.get("grupo").get("idGrupo"),
                                filtro.getIdGrupo()
                        )
                );
            }
            //juntar todas las condiciones con un "AND"r4t55
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

        };

    }
}
