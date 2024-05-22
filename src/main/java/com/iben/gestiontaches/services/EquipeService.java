package com.iben.gestiontaches.services;

import java.util.List;

import com.iben.gestiontaches.entities.Equipe;

/**
 * EquipeService
 */
public interface EquipeService {

    List<Equipe> getAllEquipes();
    void deleteEquipe(Long idTeam);
    Equipe findTeam(Long idTeam);
  
}
