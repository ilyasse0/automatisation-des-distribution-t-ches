package com.iben.gestiontaches.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iben.gestiontaches.entities.Message;

public interface MessageRepository extends JpaRepository<Message , Long> {
    @Query("SELECT m FROM Message m WHERE m.task.id = :taskId")
    List<Message> findByTaskId(@Param("taskId") Long taskId);
    

}
