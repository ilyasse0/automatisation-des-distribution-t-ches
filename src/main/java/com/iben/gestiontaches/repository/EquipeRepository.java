package com.iben.gestiontaches.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iben.gestiontaches.entities.Equipe;

public interface EquipeRepository extends JpaRepository<Equipe , Long> {
    
    @Query("SELECT u.equipes FROM User u WHERE u.id = :userId")
   List <Equipe> findEquipeByUserId(@Param("userId") String userId);
}
