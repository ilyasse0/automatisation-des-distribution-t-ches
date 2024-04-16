package com.iben.gestiontaches.services;

import java.util.List;

import com.iben.gestiontaches.entities.Role;
import com.iben.gestiontaches.entities.Service;

public interface AdminService {
    
    public List<Service> getAllServices();
    public List<Role> getAllRoles();

}
