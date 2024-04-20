package com.iben.gestiontaches.services;

import java.util.List;

import com.iben.gestiontaches.entities.Equipe;
import com.iben.gestiontaches.entities.Role;
import com.iben.gestiontaches.entities.Service;
import com.iben.gestiontaches.entities.User;
import com.iben.gestiontaches.enums.deactivatedFlag;

public interface AccountService {
     User addUser( String firstName, String lastName, String sex,
                               String phoneNumber, String email, String login,
                               String password, 
                               String confirmPassword,
                               List<Role> roles, List<Service> services);

      
      
                               
                               
    Role addNewRole(String Role);
    void addRoletoUser(String idUser , Long idRole); 
    void romeveRoleFromUser(String idUser , Long idRole);
    User loadUserBylogin (String login);
    Service addNewService(String ServiceName);
    void addServiceToUser(String idUser , Long idService);
    void romeveServiceFromUser(String idUser , Long idService);
   // String loginUser(String login , String password);
   List<User>getUsersBySupervisorService(Long serviceId);
   List<User>getUsersByChefProjetService();
   User editUser(String login);
   List<User> getSupervisorsbyTeamCord(String cordId);
   List<User> getEmployeesbyTeamCord(String cordId);

   User addSupervisor(String firstName, String lastName, String sex,
   String phoneNumber, String email, String login,
   String password,
   String confirmPassword,
   List<Service> services,
   List<Equipe> equipes);
   
   User addEmployee(String firstName, String lastName, String sex,
   String phoneNumber, String email, String login,
   String password,
   String confirmPassword,
   List<Service> services,
   List<Equipe> equipes);


}
