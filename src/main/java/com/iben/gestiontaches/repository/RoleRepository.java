package com.iben.gestiontaches.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iben.gestiontaches.entities.Role;

public interface RoleRepository extends JpaRepository<Role , Long> {
    
    Role findRoleByname(String name);

}
