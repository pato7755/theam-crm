package com.theam.crm.repository;

import com.theam.crm.model.Role;
import com.theam.crm.model.RoleDescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleDescription name);

}
