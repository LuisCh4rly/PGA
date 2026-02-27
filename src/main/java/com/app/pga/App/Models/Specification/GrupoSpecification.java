package com.app.pga.App.Models.Specification;

import com.app.pga.App.Models.Entities.Grupo;
import com.app.pga.App.Models.Entities.Usuario;
import com.app.pga.App.Models.Filtros.GrupoFiltro;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;


public class GrupoSpecification {
    /*parametros a considerar - son como piezas de un rompecabezas de SQL
    * ROOT<T>root: es tu tabla (entidad), se usa para elegir que columna quierer ver, es decir, ej. root.get("nombre"), where nombre...
    * CriteriaQuery<?> query: es la estructura completa del SELECT. se toca cuando se hacen consultas avanzadas que ocupen DISTINT o GROUP BY
    * CriteriaBuilder cb: son las condiciones, aqui se utilizan metodos para comparar: equal, like, greaterThan, etc
    *
    * NOTA: en lugar de crear metodos pequeños, se puede crear una unica especificiacion que recorra la clase de filtro
    *
    * PREDICADO: condición que puede ser verdadera o falsa
    *   en sql: el predicado se pone despues del where, ej, nombre='Maria'
    *   con criteria: un objeto Predicate es la misma pieza de código pero representada como un objeto
    *   ---Al ser una consulta dinámica, se va creando una "lista de condiciones" (predicados) y al final se le dice a hibernate "juna todos los predicados con un AND" */

    public static Specification<Grupo> filtrarGrupos (GrupoFiltro filtro){
        return ((root, query, cb) ->{
            List<Predicate> predicados = new ArrayList<>();

            //1. Filtro por curso (join)
            //root.join("curso") entra ala tabla relacionada
            if(filtro.getCurso() !=null && !filtro.getCurso().isEmpty()){
                predicados.add(cb.like(
                        root.join("curso").get("nombre"),"%" + filtro.getCurso() + "%"));
            }

            //2. Filtro de docente (CONCATENAR nombre y apellidos)
            if (filtro.getDocente() != null && !filtro.getDocente().isBlank()) {

                Join<Grupo, Usuario> usuarioJoin = root.join("usuario");

                Expression<String> nombreCompleto = cb.concat(
                        cb.concat( cb.concat( cb.concat( cb.lower(usuarioJoin.get("nombre")), ""),
                                        cb.lower(usuarioJoin.get("apellidoPaterno"))),
                                            cb.lower(usuarioJoin.get("apellidoMaterno"))), "");

                String filtroNormalizado = filtro.getDocente()
                        .toLowerCase()
                        .trim()
                        .replaceAll("\\s+", "");

                predicados.add(
                        cb.like(nombreCompleto, "%" + filtroNormalizado + "%")
                );
            }

            // 3. Filtro por Estado (ENUM)
            if (filtro.getEstado() != null) {
                // Con Enums usamos equal directamente
                predicados.add(cb.equal(root.get("estado"), filtro.getEstado()));
            }

            // Retornamos todas las condiciones unidas por AND
            return cb.and(predicados.toArray(new Predicate[0]));
        });
    }
}
