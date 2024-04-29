package com.iben.gestiontaches.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iben.gestiontaches.entities.Gravity;
import com.iben.gestiontaches.enums.Priority;


public interface GravityRepository extends  JpaRepository<Gravity , Long> {
    
    public Gravity findGravityBypriority(Priority priority);
    
}
