package com.iben.gestiontaches.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iben.gestiontaches.entities.Gravity;
import com.iben.gestiontaches.entities.Task;
import com.iben.gestiontaches.repository.GravityRepository;
import com.iben.gestiontaches.repository.TaskRepository;

import lombok.AllArgsConstructor;

@Transactional
@Service
@AllArgsConstructor
public class GravityServiceImplementation implements GravityService {
    private GravityRepository gravityRepository;
    private TaskRepository taskRepository;

    @Override
    public Gravity addNewGravity(Gravity gravity) {
        // TODO Auto-generated method stub
        return gravityRepository.save(gravity);

    }

    // @Override
    // public void addGravitytoTask(Long gravityId, Long taskId) {
    //     // TODO Auto-generated method stub
    //     Gravity gravity = gravityRepository.findById(taskId).get();
    //     Task task = taskRepository.findById(taskId).get();
    //   //  task.setGravity(gravity);

    // }

    public List<Gravity> getAllGravity(){
        return gravityRepository.findAll();
    }

}
