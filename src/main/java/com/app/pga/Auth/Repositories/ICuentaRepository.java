package com.app.pga.Auth.Repositories;

import com.app.pga.Auth.Models.Entities.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ICuentaRepository extends JpaRepository<Cuenta, Long> {

   Optional<Cuenta> findByEmail(String email);

}