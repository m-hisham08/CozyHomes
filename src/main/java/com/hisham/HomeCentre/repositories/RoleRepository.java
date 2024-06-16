package com.hisham.HomeCentre.repositories;

import com.hisham.HomeCentre.models.Role;
import com.hisham.HomeCentre.models.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleEnum name);
}
