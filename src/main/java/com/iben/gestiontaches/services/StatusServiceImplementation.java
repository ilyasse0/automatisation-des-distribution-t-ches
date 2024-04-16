package com.iben.gestiontaches.services;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iben.gestiontaches.entities.Status;
import com.iben.gestiontaches.repository.StatusRepository;

import lombok.AllArgsConstructor;

@Transactional
@Service
@AllArgsConstructor
public class StatusServiceImplementation implements StatusService {
    private StatusRepository statusRepository;

    @Override
    public Status addNewStatus(Status status) {
        // TODO Auto-generated method stub

        return statusRepository.save(status);
    }
    
}
