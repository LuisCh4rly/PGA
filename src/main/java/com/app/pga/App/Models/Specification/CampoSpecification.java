package com.app.pga.App.Models.Specification;

import com.app.pga.App.Models.Entities.CampoFormativo;
import com.app.pga.App.Models.Filtros.CampoFiltro;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CampoSpecification {

    public static Specification<CampoFormativo> filtrar (CampoFiltro filtro){

        return (root, query, criteriaBuilder) ->{
            List<Predicate> predicates = new ArrayList<>();

            if (filtro.getActivo() != null) {
                predicates.add(
                        criteriaBuilder.equal(root.get("activo"), filtro.getActivo())
                );
            }

            if (filtro.getNombre() != null) {

                predicates.add(
                        criteriaBuilder.like(  criteriaBuilder.lower(root.get("nombre")), "%" + filtro.getNombre().toLowerCase().trim().replaceAll("\\s+", "") + "%")
                );
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

        };
    }
}
