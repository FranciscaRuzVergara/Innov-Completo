package com.innovatech.repository;


import com.innovatech.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);

    boolean existsByName(String name);

    List<Role> findByHourlyRateLessThan(Double hourlyRate);

    List<Role> findByHourlyRateGreaterThan(Double hourlyRate);
}
