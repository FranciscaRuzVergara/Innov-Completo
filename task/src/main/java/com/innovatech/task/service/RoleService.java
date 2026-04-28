package com.innovatech.task.service;

import com.innovatech.task.model.Role;
import com.innovatech.task.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Role findById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + id));
    }

    @Transactional
    public Role save(Role role) {
        // Opcional: Validar si el nombre del rol ya existe para no duplicar
        roleRepository.findByName(role.getName()).ifPresent(r -> {
            throw new RuntimeException("El nombre del rol ya existe");
        });
        return roleRepository.save(role);
    }

    @Transactional
    public void delete(Long id) {
        roleRepository.deleteById(id);
    }
}