package com.app.pga.App.Models.Specification;

import com.app.pga.App.Models.Entities.Sesion;
import com.app.pga.App.Models.Enum.MomentoSesion;
import com.app.pga.App.Models.Filtros.SesionFiltro;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SesionSpecification {

    public static Specification<Sesion> filtrarSesiones (SesionFiltro filtro, Long idUsuario){
        return((root, query, cb) -> {
            List<Predicate> predicados = new ArrayList<>();
            LocalDateTime ahora = LocalDateTime.now();
            // --- NAVEGACIÓN DE RELACIONES ---
            // 1. Join de Sesion con Grupo
            // 2. Join de Grupo con Usuario (Docente)
            // 3. Filtrar por el ID del Usuario
            predicados.add(cb.equal(root.join("grupo").join("usuario").get("idUsuario"), idUsuario));


            //filtro por fecha
            if(filtro.getMomentoSesion()!=null){

                if (filtro.getMomentoSesion() == MomentoSesion.POR_INICIAR) {
                    //por iniciar es una fecha mayor a la ahora
                    predicados.add(cb.greaterThan(root.get("fecha"), ahora));
                }
                else if (filtro.getMomentoSesion() == MomentoSesion.FINALIZADAS) {
                    //finalizadas es una fecha menor a la ahora
                    predicados.add(cb.lessThan(root.get("fecha"), ahora));
                }

            }

            //filtro por alcance
            if(filtro.getAlcance()!=null){
                predicados.add(
                        cb.equal(root.get("alcance"), filtro.getAlcance())
                );
            }
            return cb.and(predicados.toArray(new Predicate[0]));
        });
    }
}
