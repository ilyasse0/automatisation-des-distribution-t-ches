package com.iben.gestiontaches.web;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.iben.gestiontaches.entities.Calendar;
import com.iben.gestiontaches.entities.Gravity;
import com.iben.gestiontaches.entities.Task;
import com.iben.gestiontaches.entities.User;
import com.iben.gestiontaches.repository.UserRepository;
import com.iben.gestiontaches.services.GravityService;
import com.iben.gestiontaches.services.TaskService;

import jakarta.persistence.EntityExistsException;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class supervisorController {
    private TaskService taskService;
    private UserRepository userRepository;
    private GravityService gravityService;

    @PostMapping("/addTask")
    public String addTask(Task task, Calendar calendar,  @RequestParam(name = "userId") String userId) {
        try {
            System.out.println("''''-------------------------");
            System.out.println(userId);
            User user = userRepository.findById(userId).orElseThrow(
                    () -> new Exception("User not found"));

            // Assign task to user first
            System.out.println("here si far");

            // Save the task after it's assigned to the user
            if (taskService.addTask(
                    task.getTitle(),
                    task.getDescription(),
                    calendar.getStartDate(),
                    calendar.getLatestStartDate(),
                    //task.getDate_Creation(),
                    calendar.getDuration(),
                   // task.getStatus(),
                    task.getGravity(),
                    user)

                    != null) {

                // this is duplicated!
                // taskService.AffecteTaskToUser(task, user);
                System.out.println("cheeck here!");

            }

            System.out.println("Task added successfully!");
            System.out.println("Task affected to " + user.getLogin() + " successfully!");

            return "redirect:/supervisor/home";
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return "error";
        }
    }

    @GetMapping("/supervisor/FormTask")
    public String home( @RequestParam(name = "id") String userId , Model model) {
        List<Gravity> listeGravity = gravityService.getAllGravity();
        if (listeGravity.isEmpty())
            throw new EntityExistsException("liste gravity is empty");
        model.addAttribute("listeGravity", listeGravity);
        model.addAttribute("task", new Task());
        model.addAttribute("calender", new Calendar());
        model.addAttribute("userId", userId);
        return "FormTask";

    }

}
