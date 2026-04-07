package com.innovatech.service;


import com.innovatech.dto.RoleDTO;
import com.innovatech.model.Role;
import com.innovatech.repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    private RoleDTO toDTO(Role role) {
        return new RoleDTO(
                role.getId(),
                role.getName(),
                role.getHourlyRate()
        );
    }

    // Mapper: RoleDTO -> Role
    private Role toEntity(RoleDTO dto) {
        Role role = new Role();
        role.setId(dto.getId());
        role.setName(dto.getName());
        role.setHourlyRate(dto.getHourlyRate());
        return role;
    }

    // Get all roles
    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Get role by ID
    public Optional<RoleDTO> getRoleById(Long id) {
        return roleRepository.findById(id)
                .map(this::toDTO);
    }

    // Get role by name
    public Optional<RoleDTO> getRoleByName(String name) {
        return roleRepository.findByName(name)
                .map(this::toDTO);
    }

    // Get roles by hourly rate less than
    public List<RoleDTO> getRolesByHourlyRateLessThan(Double hourlyRate) {
        return roleRepository.findByHourlyRateLessThan(hourlyRate)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Get roles by hourly rate greater than
    public List<RoleDTO> getRolesByHourlyRateGreaterThan(Double hourlyRate) {
        return roleRepository.findByHourlyRateGreaterThan(hourlyRate)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Create role
    @Transactional
    public RoleDTO createRole(RoleDTO dto) {
        if (roleRepository.existsByName(dto.getName())) {
            throw new RuntimeException("Role already exists with name: " + dto.getName());
        }
        return toDTO(roleRepository.save(toEntity(dto)));
    }

    // Update role
    @Transactional
    public RoleDTO updateRole(Long id, RoleDTO dto) {
        Role existing = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with ID: " + id));

        existing.setName(dto.getName());
        existing.setHourlyRate(dto.getHourlyRate());

        return toDTO(roleRepository.save(existing));
    }

    // Delete role
    @Transactional
    public void deleteRole(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new RuntimeException("Role not found with ID: " + id);
        }
        roleRepository.deleteById(id);
    }
}