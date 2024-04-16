package com.iben.gestiontaches.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iben.gestiontaches.entities.Gravity;


public interface GravityRepository extends  JpaRepository<Gravity , Long> {
    
}
