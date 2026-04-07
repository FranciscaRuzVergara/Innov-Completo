package com.innovatech.controller;


import com.innovatech.dto.RoleDTO;
import com.innovatech.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDTO> getRoleById(@PathVariable Long id) {
        return roleService.getRoleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<RoleDTO> getRoleByName(@PathVariable String name) {
        return roleService.getRoleByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET roles by hourly rate less than
    @GetMapping("/hourlyRate/less/{hourlyRate}")
    public ResponseEntity<List<RoleDTO>> getRolesByHourlyRateLessThan(@PathVariable Double hourlyRate) {
        return ResponseEntity.ok(roleService.getRolesByHourlyRateLessThan(hourlyRate));
    }

    // GET roles by hourly rate greater than
    @GetMapping("/hourlyRate/greater/{hourlyRate}")
    public ResponseEntity<List<RoleDTO>> getRolesByHourlyRateGreaterThan(@PathVariable Double hourlyRate) {
        return ResponseEntity.ok(roleService.getRolesByHourlyRateGreaterThan(hourlyRate));
    }

    @PostMapping
    public ResponseEntity<RoleDTO> createRole(@RequestBody RoleDTO dto) {
        try {
            RoleDTO created = roleService.createRole(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleDTO> updateRole(@PathVariable Long id,
                                           @RequestBody RoleDTO dto) {
        try {
            RoleDTO updated = roleService.updateRole(id, dto);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        try {
            roleService.deleteRole(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
