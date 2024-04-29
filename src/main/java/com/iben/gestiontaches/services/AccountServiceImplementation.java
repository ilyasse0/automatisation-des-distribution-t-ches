package com.iben.gestiontaches.services;

import java.beans.Encoder;
import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.iben.gestiontaches.entities.Equipe;
import com.iben.gestiontaches.entities.Role;
import com.iben.gestiontaches.entities.Service;
import com.iben.gestiontaches.entities.User;
import com.iben.gestiontaches.enums.deactivatedFlag;
import com.iben.gestiontaches.repository.RoleRepository;
import com.iben.gestiontaches.repository.ServiceRepository;
import com.iben.gestiontaches.repository.UserRepository;

import lombok.AllArgsConstructor;

@Transactional
@org.springframework.stereotype.Service
@AllArgsConstructor
public class AccountServiceImplementation implements AccountService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private ServiceRepository serviceRepository;

    @Override
    public User addUser(String firstName, String lastName, String sex, String phoneNumber, String email,
            String login, String password, String confirmPassword, List<Role> roles,
            List<Service> services) {

        // Check if user with provided login already exists
        User existingUser = userRepository.findUserBylogin(login);
        if (existingUser != null) {
            throw new RuntimeException("User with login '" + login + "' already exists!");
        }

        // Check if password matches the confirm password
        if (!password.equals(confirmPassword)) {
            throw new RuntimeException("Passwords do not match!");
        }

        // Create a new user with the provided attributes
        User newUser = User.builder()
                .id(UUID.randomUUID().toString())
                .firstName(firstName)
                .lastName(lastName)
                .sex(sex)
                .phoneNumber(phoneNumber)
                .email(email)
                .login(login)
                .password(passwordEncoder.encode(password))
                .status(deactivatedFlag.ACTIVE)
                .roles(roles)
                .services(services)
                .build();

        // Save the new user to the repository
        return userRepository.save(newUser);
    }

    @Override
    public Role addNewRole(String role) {
        // TODO Auto-generated method stub

        Role roleExiste = roleRepository.findRoleByname(role);
        if (roleExiste != null)
            throw new RuntimeException("Role already exists");
        Role newRole = Role.builder()
                .name(role)
                .build();

        return roleRepository.save(newRole);
    }

    @Override
    public void addRoletoUser(String idUser, Long idRole) {
        // TODO Auto-generated method stub
        User user = userRepository.findUserByid(idUser);
        Role role = roleRepository.findById(idRole).get();
        user.getRoles().add(role);
        // userRepository.save(user); methode is transactionnel!! noo need!
    }

    @Override
    public void romeveRoleFromUser(String idUser, Long idRole) {
        // TODO Auto-generated method stub
        User user = userRepository.findUserByid(idUser);
        Role role = roleRepository.findById(idRole).get();
        user.getRoles().remove(role);
    }

    @Override
    public User loadUserBylogin(String login) {
        // TODO Auto-generated method stub
        User user = userRepository.findUserBylogin(login);
        if (user == null)
            throw new RuntimeException("user " + login + " not found");

        return user;
    }

    @Override
    public Service addNewService(String ServiceName) {
        // TODO Auto-generated method stub
        Service serviceExiste = serviceRepository.findServiceByname(ServiceName);
        if (serviceExiste != null)
            throw new RuntimeException("Service already exists");
        Service newService = Service.builder()
                .name(ServiceName)
                .build();

        return serviceRepository.save(newService);
    }

    @Override
    public void addServiceToUser(String idUser, Long idService) {
        // TODO Auto-generated method stub
        User user = userRepository.findUserByid(idUser);
        Service service = serviceRepository.findById(idService).get();
        user.getServices().add(service);
    }

    @Override
    public void romeveServiceFromUser(String idUser, Long idService) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'romeveServiceFromUser'");
    }

    @Override
    public List<User> getUsersBySupervisorService(Long serviceId) {
        // TODO Auto-generated method stub
        // return userRepository.findUsersBySupervisorService(serviceId);
        Service service = serviceRepository.findById(serviceId).orElse(null);
        if (service == null) {
            return Collections.emptyList(); // Service not found, return an empty list
        }
        return userRepository.findAll().stream()
                .filter(user -> user.getServices().contains(service))
                .filter(user -> user.getRoles().stream().anyMatch(role -> role.getName().equals("OP")))
                .collect(Collectors.toList());

    }

    @Override
    public List<User> getUsersByChefProjetService() {
        // TODO Auto-generated method stub
        return userRepository.findAll().stream()
                .filter(user -> user.getRoles().stream().noneMatch(role -> role.getName().equals("CHEF_PROJET")))
                .collect(Collectors.toList());

    }

    @Override
    public User editUser(String login) {
        // TODO Auto-generated method stub
        return userRepository.findUserBylogin(login);
    }

    // @Override
    public List<User> getSupervisorsbyTeamCord(String cordId) {

        Long teamId = userRepository.findTeamIdByUserId(cordId);
        List liste = userRepository.findSupervisorsByTeamId(teamId);
        return liste;

    }
    

    @Override
    public User addSupervisor(String firstName, String lastName, String sex, String phoneNumber, String email,
            String login, String password, String confirmPassword, List<Service> services, List<Equipe> equipes) {
        // TODO Auto-generated method stub
        User existingUser = userRepository.findUserBylogin(login);
          if (existingUser != null) {
              throw new RuntimeException("User with login '" + login + "' already exists!");
          }

           // Check if password matches the confirm password
        if (!password.equals(confirmPassword)) {
            throw new RuntimeException("Passwords do not match!");
        }
        List<Role> role = new ArrayList<Role>();
        role.add(roleRepository.findRoleByname("SUP"));

        User newUser = User.builder()
        .id(UUID.randomUUID().toString())
        .firstName(firstName)
        .lastName(lastName)
        .sex(sex)
        .phoneNumber(phoneNumber)
        .email(email)
        .login(login)
        .password(passwordEncoder.encode(password))
        .status(deactivatedFlag.ACTIVE)
        .roles(role)
        .services(services)
        .equipes(equipes)
        .build();

        return userRepository.save(newUser);
    }

    @Override
    public User addEmployee(String firstName, String lastName, String sex, String phoneNumber, String email,
            String login, String password, String confirmPassword, List<Service> services, List<Equipe> equipes) {
        // TODO Auto-generated method stub
        User existingUser = userRepository.findUserBylogin(login);
          if (existingUser != null) {
              throw new RuntimeException("User with login '" + login + "' already exists!");
          }

           // Check if password matches the confirm password
        if (!password.equals(confirmPassword)) {
            throw new RuntimeException("Passwords do not match!");
        }
        List<Role> role = new ArrayList<Role>();
        role.add(roleRepository.findRoleByname("OP"));
        User newUser = User.builder()
        .id(UUID.randomUUID().toString())
        .firstName(firstName)
        .lastName(lastName)
        .sex(sex)
        .phoneNumber(phoneNumber)
        .email(email)
        .login(login)
        .password(passwordEncoder.encode(password))
        .status(deactivatedFlag.ACTIVE)
        .roles(role)
        .services(services)
        .equipes(equipes)
        .build();

        return userRepository.save(newUser);
    }

    @Override
    public List<User> getEmployeesbyTeamCord(String cordId) {
        // TODO Auto-generated method stub
        Long teamId = userRepository.findTeamIdByUserId(cordId);
        List liste = userRepository.findEmployeesByTeamId(teamId);
        return liste;    }

}
