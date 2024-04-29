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

    @Override
    public void deleteEquipe(Long idTeam) {
        // TODO Auto-generated method stub
      Equipe equipe =  equipeRepository.findById(idTeam).get();
      if(equipe ==null){
        throw new IllegalStateException("Team not found please cheeck the server");
      }else{
        equipeRepository.delete(equipe);
      }

    }
    
    
}
