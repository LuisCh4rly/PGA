package com.app.pga.App.Models.Specification;

import com.app.pga.App.Models.Entities.Documento;
import com.app.pga.App.Models.Filtros.DocumentoFiltro;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class DocumentoSpecification {
    public static Specification<Documento>filtrarDocumentos(DocumentoFiltro filtro){
        return((root, query, cb) -> {
            List<Predicate>predicados = new ArrayList<>();

            //filtro por nombre
            if(filtro.getNombre() !=null && !filtro.getNombre().isEmpty()){
                predicados.add(
                        cb.like(cb.lower(root.get("nombre")),
                                "%"+filtro.getNombre().toLowerCase().trim()+"%")
                );
            }
            //filtro por activo
            if(filtro.getActivo()!=null){
                predicados.add(
                        cb.equal(root.get("activo"), filtro.getActivo())
                );
            }
            return cb.and(predicados.toArray(new Predicate[0]));
        });
    }
}
