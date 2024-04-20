package com.iben.gestiontaches.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.iben.gestiontaches.entities.Service;
import com.iben.gestiontaches.entities.User;

public interface UserRepository extends JpaRepository<User , String> {
    User findUserBylogin(String login);
    User findUserByid(String userId);
    //Long findidBylogin(String login);
    @Query("SELECT u.id FROM User u WHERE u.login = :login")
    String findIdByLogin(@Param("login") String login);

   // @Query("SELECT u FROM User u JOIN u.services s WHERE s.id IN :serviceIds")
    //List<User> findUsersBySupervisorService(@Param("serviceIds") List<Service> serviceIds);


     @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :roleName")
     List<User> findByRolesName(@Param("roleName") String roleName);    


     @Query("SELECT u FROM User u JOIN u.roles r JOIN u.equipes e WHERE r.name = 'SUP' AND e.id = :teamId")
    List<User> findSupervisorsByTeamId(@Param("teamId") Long teamId);

    @Query("SELECT u FROM User u JOIN u.roles r JOIN u.equipes e WHERE r.name = 'OP' AND e.id = :teamId")
    List<User> findEmployeesByTeamId(@Param("teamId") Long teamId);

    @Query("SELECT e.id FROM User u JOIN u.equipes e WHERE u.id = :userId")
    Long findTeamIdByUserId(@Param("userId") String userId);
}
