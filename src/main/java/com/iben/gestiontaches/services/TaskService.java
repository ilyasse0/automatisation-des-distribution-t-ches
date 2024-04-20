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
             int duration ,  Gravity gravity , User user);

    public UserTaskAssignment AffecteTaskToUser(Task task, User user);
    public  List<Task> getTasks(String userId);
    public Task addTaskByCor(String title, String description);


}
