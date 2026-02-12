package com.app.pga.Auth.Repositories;

import com.app.pga.Auth.Models.Entities.Role;
import com.app.pga.Auth.Models.Enum.ERole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IRoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}