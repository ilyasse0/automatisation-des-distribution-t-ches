package com.iben.gestiontaches.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iben.gestiontaches.entities.Equipe;
import com.iben.gestiontaches.repository.EquipeRepository;

import lombok.AllArgsConstructor;


@Transactional
@Service
@AllArgsConstructor
public class EquipeServiceImplementation implements EquipeService {
    private EquipeRepository equipeRepository;

    @Override
    public List<Equipe> getAllEquipes() {
        // TODO Auto-generated method stub
        return equipeRepository.findAll();
    }
    
}
