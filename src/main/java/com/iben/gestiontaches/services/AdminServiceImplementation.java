package com.iben.gestiontaches.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.iben.gestiontaches.entities.Equipe;
import com.iben.gestiontaches.entities.Role;
import com.iben.gestiontaches.entities.Service;
import com.iben.gestiontaches.entities.User;
import com.iben.gestiontaches.enums.deactivatedFlag;
import com.iben.gestiontaches.repository.EquipeRepository;
import com.iben.gestiontaches.repository.RoleRepository;
import com.iben.gestiontaches.repository.ServiceRepository;
import com.iben.gestiontaches.repository.UserRepository;

import lombok.AllArgsConstructor;

@Transactional
@AllArgsConstructor
@org.springframework.stereotype.Service
public class AdminServiceImplementation implements AdminService {
    private ServiceRepository serviceRepository;
    private RoleRepository roleRepository;
    private EquipeRepository equipeRepository;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public List<Service> getAllServices() {
        // TODO Auto-generated method stub

        return serviceRepository.findAll();

    }

    @Override
    public List<Role> getAllRoles() {
        List<Role> listeRoles = roleRepository.findAll();
        List<Role> filteredRoles = listeRoles.stream()
            .filter(role -> !"CHEF_PROJET".equals(role.getName()))
            .collect(Collectors.toList());
            return filteredRoles;
      
    }

    @Override
    public Equipe addEquipe(String nomEquipe, String description, deactivatedFlag status) {
        // TODO Auto-generated method stub
        Equipe equipe = Equipe.builder()
        .nom(nomEquipe)
        .description(description)
        .status(status)
        .build();
        return equipeRepository.save(equipe);
        

    }
    public List<Equipe> getAllEquipes(){
        return equipeRepository.findAll();
    }

    @Override
    public User addCordinateur(String firstName, String lastName, String sex, String phoneNumber, String email,
            String login, String password, String confirmPassword,  List<Service> services , List<Equipe> equipes) {
        // TODO Auto-generated method stub
          // Check if user with provided login already exists
          User existingUser = userRepository.findUserBylogin(login);
          if (existingUser != null) {
              throw new RuntimeException("User with login '" + login + "' already exists!");
          }

           // Check if password matches the confirm password
        if (!password.equals(confirmPassword)) {
            throw new RuntimeException("Passwords do not match!");
        }
        List<Role> role = new ArrayList<Role>();
        role.add(roleRepository.findRoleByname("COR"));

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

    public List<User> getCordinateur(){
        //List<User> listeCord =  userRepository.findByRolesName("COR");
        List<User> listeCord =  userRepository.findByRolesName("COR");

        System.out.println("((-----------------------------------))");
        System.out.println(listeCord);
        return listeCord;

    }


    

    

}
