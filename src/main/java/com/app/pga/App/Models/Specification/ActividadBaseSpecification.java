package com.app.pga.App.Models.Specification;

import com.app.pga.App.Models.Entities.ActividadBase;
import com.app.pga.App.Models.Entities.CampoFormativo;
import com.app.pga.App.Models.Entities.Inscripcion;
import com.app.pga.App.Models.Entities.Usuario;
import com.app.pga.App.Models.Filtros.ActividadBaseFiltro;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ActividadBaseSpecification {

public static Specification <ActividadBase> filtrar (ActividadBaseFiltro filtro){

    return (root, query, criteriaBuilder) ->{
        List<Predicate> predicates = new ArrayList<>();

        if (filtro.getActivo() != null) {
            predicates.add(
                    criteriaBuilder.equal(root.get("activo"), filtro.getActivo())
            );
        }
        if (filtro.getNombre() != null) {
            predicates.add(
                    criteriaBuilder.like( criteriaBuilder.lower( root.get("titulo")), "%" + filtro.getNombre().toLowerCase().trim().replaceAll("\\s+", "") + "%")
            );
        }
        if (filtro.getIdCampo() != null){
            predicates.add(
                    criteriaBuilder.equal(
                            root.get("campoFormativo").get("idCampo"),
                            filtro.getIdCampo()
                    )
            );        }


        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

    };
}


}
