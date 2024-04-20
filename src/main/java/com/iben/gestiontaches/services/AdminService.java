package com.iben.gestiontaches.services;

import java.util.List;

import com.iben.gestiontaches.entities.Equipe;
import com.iben.gestiontaches.entities.Role;
import com.iben.gestiontaches.entities.Service;
import com.iben.gestiontaches.entities.User;
import com.iben.gestiontaches.enums.deactivatedFlag;

public interface AdminService {

    public List<Service> getAllServices();

    public List<Role> getAllRoles();

    public Equipe addEquipe(String nomEquipe, String description, deactivatedFlag status);

    User addCordinateur(String firstName, String lastName, String sex,
            String phoneNumber, String email, String login,
            String password,
            String confirmPassword,
            List<Service> services,
            List<Equipe> equipes);

}
