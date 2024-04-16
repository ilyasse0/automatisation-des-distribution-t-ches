package com.iben.gestiontaches.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iben.gestiontaches.entities.Service;

public interface ServiceRepository extends JpaRepository<Service , Long> {
    Service findServiceByname(String service);
    Service findServiceByid(Long id);
}
