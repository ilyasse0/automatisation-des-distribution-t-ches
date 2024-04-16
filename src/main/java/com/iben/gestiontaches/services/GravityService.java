package com.iben.gestiontaches.services;

import java.util.List;

import com.iben.gestiontaches.entities.Gravity;

/**
 * GravityService
 */
public interface GravityService {

    public Gravity addNewGravity(Gravity gravity);
  //  public void addGravitytoTask(Long  gravityId , Long taskId);
  public List<Gravity> getAllGravity();
    
}