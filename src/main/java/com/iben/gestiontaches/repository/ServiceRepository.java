package com.iben.gestiontaches.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.iben.gestiontaches.entities.Service;

public interface ServiceRepository extends JpaRepository<Service , Long> {
    Service findServiceByname(String service);
    Service findServiceByid(Long id);


    
}
