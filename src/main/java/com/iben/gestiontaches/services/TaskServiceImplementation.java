package com.iben.gestiontaches.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iben.gestiontaches.entities.Calendar;
import com.iben.gestiontaches.entities.Gravity;
import com.iben.gestiontaches.entities.Message;
import com.iben.gestiontaches.entities.Status;
import com.iben.gestiontaches.entities.Task;
import com.iben.gestiontaches.entities.User;
import com.iben.gestiontaches.entities.UserTaskAssignment;
import com.iben.gestiontaches.enums.statusCode;
import com.iben.gestiontaches.repository.CalendarRepository;
import com.iben.gestiontaches.repository.MessageRepository;
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
    private MessageRepository messageRepository;
    

    @Override
    @Transactional
    public Task addTask(String title, String description, LocalDate startDate, LocalDate latestStartDate,
            int duration, Gravity gravity, User user) {
        // try {
        // // String taskId = UUID.randomUUID().toString();

        // // Create a new calendar
        // Calendar calendar = Calendar.builder()
        // .duration(duration)
        // .startDate(startDate)
        // .latestStartDate(latestStartDate)
        // .build();
        // calendarRepository.save(calendar);

        // Status status = statusRepository.findById(1L).get();

        // // Create a new task
        // Task task = Task.builder()
        // .calendar(calendar)
        // .title(title)
        // .description(description)
        // .date_Creation(LocalDate.now())
        // .gravity(gravity)
        // .status(status)
        // .build();

        // // Save the calendar and task
        // Task savedTask = taskRepository.save(task);

        // // Assign the task to the user
        // UserTaskAssignment userTaskAssignment = new UserTaskAssignment();
        // userTaskAssignment.setUser(user);
        // userTaskAssignment.setTask(savedTask);
        // assignmentRepository.save(userTaskAssignment);

        // return savedTask;
        // } catch (Exception e) {
        // // Handle exceptions
        // throw new RuntimeException("Error while adding task: " + e.getMessage(), e);
        // }

        return null;
    }

    @Override
    public UserTaskAssignment AffecteTaskToUser(Task task, User user) {

        // UserTaskAssignment userTaskAssignment = new UserTaskAssignment();
        // userTaskAssignment.setUser(user);
        // userTaskAssignment.setTask(task);
        // return assignmentRepository.save(userTaskAssignment);

        return null;
    }

    public List<Task> getTasksbyCord(String userId) {
        List<Task> tasks = new ArrayList<>();
        taskRepository.findTasksByCorUserId(userId).forEach(tasks::add);
        return tasks;

    }

    public List<Task> getTasksbySup(String userId) {
        List<Task> tasks = new ArrayList<>();
        taskRepository.findTasksBySupUserId(userId).forEach(tasks::add);
        return tasks;

    }

    public List<Task> getTasksbyOp(String userId) {
        List<Task> tasks = new ArrayList<>();
        taskRepository.findTasksByOpId(userId).forEach(tasks::add);
        return tasks;

    }

    public Task addTaskByCor(String title, String description, Gravity gravity, String cordId, String supId) {
        Status status = statusRepository.findById(2L).get();
        Task task = Task.builder()
                .title(title)
                .description(description)
                .date_Creation(LocalDate.now())
                .gravity(gravity)
                .status(status)
                .status_task_temp(statusCode.NOTASSIGNED)
                .priority_temp(gravity.getPriority())
                .build();

        taskRepository.save(task);

        UserTaskAssignment userTaskAssignment = new UserTaskAssignment();
        userTaskAssignment.setTask(task);
        userTaskAssignment.setCorUserId(cordId);
        userTaskAssignment.setSupUserId(supId);
        assignmentRepository.save(userTaskAssignment);

        return task;

    }

    public Task addTaskBySup(Long idTask, String opId, int duration, LocalDate startDate) {
        Optional<Task> taskOptional = taskRepository.findById(idTask);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            Status status = statusRepository.findById(2L).get();
            UserTaskAssignment usrTask = assignmentRepository.findByTaskId(idTask);
            // Create a new calendar
            Calendar calendar = Calendar.builder()
                    .duration(duration)
                    .startDate(startDate)
                    .build();
            calendarRepository.save(calendar);
            usrTask.setOpUserId(opId);
    
            task.setCalendar(calendar);
            task.setStatus(status);
            task.setStatus_task_temp(statusCode.TODO);
            taskRepository.save(task);
            return task;
        } else {
            // Handle the case where the Task with the given idTask is not found
            // For example:
            // throw new TaskNotFoundException("Task with id " + idTask + " not found");
            return null; // Or whatever is appropriate in your case
        }
    }
    public List<Task> getTasks(){
        List<Task> listeTasks = taskRepository.findAll();
        return listeTasks;
    }

    @Override
    public void removeTask(Long idTask) {
        // TODO Auto-generated method stub
        assignmentRepository.deleteByTaskId(idTask);
        taskRepository.deleteById(idTask);
        System.out.println("task removed successfully!");
    }

    @Override
    public Task findbyId(Long idTask) {
        // TODO Auto-generated method stub
        return taskRepository.findById(idTask).get();
    }

    @Override
    public Task upadteTaskStatus(Long idTask) {
        Task task = findbyId(idTask);
        if(task.getStatus_task_temp()==statusCode.TODO){
            task.setStatus_task_temp(statusCode.VALIDATIONIn_PROGRESS);
        }else{
           throw new  RuntimeException("Invalid Operation! task already done");
                }
          
            return task;
       
    }

    @Override
    public Task ValideTaskStatus(Long idTask) {
        // TODO Auto-generated method stub
        Task task = findbyId(idTask);
        if(task.getStatus_task_temp()==statusCode.VALIDATIONIn_PROGRESS){
            task.setStatus_task_temp(statusCode.DONE);
        }else{
           throw new  RuntimeException("Invalid Operation! task already done");
                }
          
            return task;
           }

    @Override
    public void removeAllTasksByCoordinator(String coordinatorId) {
        // TODO Auto-generated method stub
        List<Task> tasks = taskRepository.findTasksByCoordinatorId(coordinatorId);
        for (Task task : tasks) {
            //this part os for deleting the messages
            List<Message> messages = task.getMessages();
            if (messages != null) {
                for (Message message : messages) {
                    messageRepository.delete(message);
                    System.out.println("message has been deleted");
                }
            }
            //this part os for deleting the userAssignenment
            //this is darori to ba able to drop safly the tasks!!!!

            List<UserTaskAssignment> userTaskAssignments = task.getUserAssignments();
            if (userTaskAssignments != null) {
                for (UserTaskAssignment assignment : userTaskAssignments) {
                    assignmentRepository.delete(assignment);
                    System.out.println("UserTaskAssignment has been deleted");
                }
            }


            taskRepository.delete(task);
            System.out.println("tasks has been initialized!");
        }
    }

    @Override
    public LocalDate calculateDeadline(Task task) {
        if (task == null || task.getCalendar() == null || task.getCalendar().getStartDate() == null) {
            return null; // Return null if task or calendar or startDate is null
        }
        return task.getCalendar().getStartDate().plusDays(task.getCalendar().getDuration());
    }



}
