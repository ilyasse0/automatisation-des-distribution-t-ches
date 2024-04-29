package com.iben.gestiontaches.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iben.gestiontaches.entities.UserTaskAssignment;

public interface UserTaskAssignmentRepository extends JpaRepository<UserTaskAssignment , Long > {
 
    @Query("SELECT uta FROM UserTaskAssignment uta WHERE uta.task.id = :taskId")
    UserTaskAssignment findByTaskId(@Param("taskId") Long taskId);
}
