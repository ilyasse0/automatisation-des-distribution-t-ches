package com.iben.gestiontaches.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iben.gestiontaches.entities.UserTaskAssignment;

public interface UserTaskAssignmentRepository extends JpaRepository<UserTaskAssignment , Long > {
    
}
