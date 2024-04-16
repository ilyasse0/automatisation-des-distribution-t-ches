package com.iben.gestiontaches.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iben.gestiontaches.entities.Calendar;
import com.iben.gestiontaches.entities.Gravity;
import com.iben.gestiontaches.entities.Status;
import com.iben.gestiontaches.entities.Task;
import com.iben.gestiontaches.entities.User;
import com.iben.gestiontaches.entities.UserTaskAssignment;
import com.iben.gestiontaches.repository.CalendarRepository;
import com.iben.gestiontaches.repository.StatusRepository;
import com.iben.gestiontaches.repository.TaskRepository;
import com.iben.gestiontaches.repository.UserRepository;
import com.iben.gestiontaches.repository.UserTaskAssignmentRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
@Transactional
public class TaskServiceImplementation implements TaskService {
    private TaskRepository taskRepository;
    private CalendarRepository calendarRepository;
    private UserTaskAssignmentRepository assignmentRepository;
    private StatusRepository statusRepository;

    @Override
    @Transactional
public Task addTask(String title, String description, LocalDate startDate, LocalDate latestStartDate,
         int duration, Gravity gravity, User user) {
    try {
      //  String taskId = UUID.randomUUID().toString();

        // Create a new calendar
        Calendar calendar = Calendar.builder()
                .duration(duration)
                .startDate(startDate)
                .latestStartDate(latestStartDate)
                .build();
                calendarRepository.save(calendar);
        
        
        Status status = statusRepository.findById(1L).get();

        // Create a new task
        Task task = Task.builder()
                .calendar(calendar)
                .title(title)
                .description(description)
                .date_Creation(LocalDate.now())
                .gravity(gravity)
                .status(status)
                .build();

        // Save the calendar and task
        Task savedTask = taskRepository.save(task);

        // Assign the task to the user
        UserTaskAssignment userTaskAssignment = new UserTaskAssignment();
        userTaskAssignment.setUser(user);
        userTaskAssignment.setTask(savedTask);
        assignmentRepository.save(userTaskAssignment);

        return savedTask;
    } catch (Exception e) {
        // Handle exceptions
        throw new RuntimeException("Error while adding task: " + e.getMessage(), e);
    }
}

    @Override
    public UserTaskAssignment AffecteTaskToUser(Task task, User user) {

        UserTaskAssignment userTaskAssignment = new UserTaskAssignment();
        userTaskAssignment.setUser(user);
        userTaskAssignment.setTask(task);
        return assignmentRepository.save(userTaskAssignment);

    }


    public  List<Task> getTasks(String userId){
        List<Task> tasks = new ArrayList<>();
        taskRepository.findTasksByUserId(userId).forEach(tasks::add);
       // taskRepository.findAll().forEach(tasks::add);
        return tasks;
    }



}
