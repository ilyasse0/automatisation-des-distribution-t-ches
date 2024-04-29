package com.iben.gestiontaches.services;

import java.time.LocalDate;
import java.util.List;

import com.iben.gestiontaches.entities.Calendar;
import com.iben.gestiontaches.entities.Gravity;
import com.iben.gestiontaches.entities.Status;
import com.iben.gestiontaches.entities.Task;
import com.iben.gestiontaches.entities.User;
import com.iben.gestiontaches.entities.UserTaskAssignment;

public interface TaskService {
    public Task addTask(String title, String description, LocalDate startDate, LocalDate latestStartDate,
            int duration, Gravity gravity, User user);

    public UserTaskAssignment AffecteTaskToUser(Task task, User user);

    public List<Task> getTasksbyCord(String userId);
    public List<Task> getTasksbySup(String userId);
    public List<Task> getTasksbyOp(String userId);



    public Task addTaskByCor(String title, String description, Gravity gravity,String cordId,String supId);
    public Task addTaskBySup(Long idTask, String opId , int duration , LocalDate startDate );
    public List<Task> getTasks();

}
