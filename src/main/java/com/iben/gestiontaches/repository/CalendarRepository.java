package com.iben.gestiontaches.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iben.gestiontaches.entities.Calendar;

public interface CalendarRepository extends JpaRepository<Calendar , Long>{
    
}
