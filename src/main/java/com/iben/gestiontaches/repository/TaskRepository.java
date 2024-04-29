package com.iben.gestiontaches.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iben.gestiontaches.entities.Task;

/**
 * TaskRepository
 */
public interface TaskRepository extends JpaRepository<Task  , Long>{

   
   // @Query("SELECT t FROM Task t JOIN t.userAssignments a JOIN a.user u WHERE u.id = :userId")
    //List<Task> findTasksByUserId(@Param("userId") String userId);

    @Query("SELECT t FROM Task t JOIN t.userAssignments a WHERE a.corUserId = :corUserId")
List<Task> findTasksByCorUserId(@Param("corUserId") String corUserId);


@Query("SELECT t FROM Task t JOIN t.userAssignments a WHERE a.supUserId = :supUserId")
List<Task> findTasksBySupUserId(@Param("supUserId") String supUserId);


@Query("SELECT t FROM Task t JOIN t.userAssignments a WHERE a.opUserId = :opUserId")
List<Task> findTasksByOpId(@Param("opUserId") String opUserId);

    
}