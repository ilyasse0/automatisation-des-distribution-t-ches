package com.iben.gestiontaches.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

import com.iben.gestiontaches.entities.Role;
import com.iben.gestiontaches.entities.Service;
import com.iben.gestiontaches.repository.RoleRepository;
import com.iben.gestiontaches.repository.ServiceRepository;

import lombok.AllArgsConstructor;

@Transactional
@AllArgsConstructor
@org.springframework.stereotype.Service
public class AdminServiceImplementation implements AdminService {
    private ServiceRepository serviceRepository;
    private RoleRepository roleRepository;

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

    

    

}
