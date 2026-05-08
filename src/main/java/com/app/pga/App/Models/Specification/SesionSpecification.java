package com.app.pga.App.Models.Specification;

import com.app.pga.App.Models.Entities.Grupo;
import com.app.pga.App.Models.Entities.Sesion;
import com.app.pga.App.Models.Entities.SesionAlumno;
import com.app.pga.App.Models.Enum.MomentoSesion;
import com.app.pga.App.Models.Filtros.SesionFiltro;
import jakarta.persistence.criteria.Join;
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
            Join<Sesion, Grupo> grupoJoin = root.join("grupo");
            predicados.add(cb.equal(grupoJoin.join("usuario").get("idUsuario"), idUsuario));

            if (filtro.getIdGrupo() != null) {
                // Navegación: Sesion -> Grupo -> idGrupo (o el nombre de tu PK en Grupo, ej: "id")
                predicados.add(cb.equal(grupoJoin.get("idGrupo"), filtro.getIdGrupo()));
            }

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

    public static Specification<Sesion> filtrarSesionesAlumno (SesionFiltro filtro, Long idInscripcion){
        return((root, query, cb) -> {
            List<Predicate> predicados = new ArrayList<>();
            LocalDateTime ahora = LocalDateTime.now();
            // --- NAVEGACIÓN DE RELACIONES ---
            // 1. Join de Sesion con SesionAlumno (el atributo de la sesion es sesionAlumno)
            // 2. Join de la SesionALumno con Inscripcion (el atributo de la sesionAlumno es inscripcion)
            // 3. Filtrar por el ID de la inscripcion
            Join<Sesion, SesionAlumno> sesionAlumnoJoin = root.join("sesionAlumnos");
            predicados.add(cb.equal(sesionAlumnoJoin.get("inscripcion").get("idInscripcion"), idInscripcion));

            if (filtro.getIdGrupo() != null) {
                // Navegación: Sesion -> Grupo -> idGrupo (o el nombre de tu PK en Grupo, ej: "id")
                Join<Sesion, Grupo> grupoJoin = root.join("grupo");
                predicados.add(cb.equal(grupoJoin.get("idGrupo"), filtro.getIdGrupo()));
            }

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
            query.distinct(true);//por si existen duplicados

            return cb.and(predicados.toArray(new Predicate[0]));
        });
    }
}
