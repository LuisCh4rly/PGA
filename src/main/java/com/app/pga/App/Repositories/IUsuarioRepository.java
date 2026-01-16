package com.app.pga.App.Repositories;

import com.app.pga.App.Models.Dtos.UsuarioDto;
import com.app.pga.App.Models.Entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IUsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByNombreAndApellidoPaternoAndApellidoMaterno(String Nombre, String ApellidoPaterno, String ApellidoMaterno);
    List<Usuario> findByActivoTrue();

}
