package com.iben.gestiontaches.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iben.gestiontaches.entities.Message;
import com.iben.gestiontaches.entities.Task;

public interface MessageRepository extends JpaRepository<Message , Long> {
    @Query("SELECT m FROM Message m WHERE m.task.id = :taskId")
    List<Message> findByTaskId(@Param("taskId") Long taskId);
    

    //  @Query("SELECT m.user.id FROM Message m WHERE m.task = :task")
    //  List<String> findUserIdsByTask(@Param("task") Long taskId);
    

}
