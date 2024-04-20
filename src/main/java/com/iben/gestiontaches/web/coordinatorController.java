package com.iben.gestiontaches.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.iben.gestiontaches.entities.Equipe;
import com.iben.gestiontaches.entities.Service;
import com.iben.gestiontaches.entities.Task;
import com.iben.gestiontaches.entities.User;
import com.iben.gestiontaches.repository.EquipeRepository;
import com.iben.gestiontaches.repository.ServiceRepository;
import com.iben.gestiontaches.repository.UserRepository;
import com.iben.gestiontaches.services.AccountServiceImplementation;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class coordinatorController {
    AccountServiceImplementation accountServiceImplementation;
    UserRepository userRepository;
    private EquipeRepository equipeRepository;
    private ServiceRepository serviceRepository;

    @GetMapping("/coordinator/home")
    public String getUsersByChefProjetServices(Model model, Authentication auth) {
        String idCord = userRepository.findIdByLogin(auth.getName());
        List<User> supervisors = new ArrayList<>();
        supervisors.addAll(accountServiceImplementation.getSupervisorsbyTeamCord(idCord));
        System.out.println("You are here __________________________________________");
        model.addAttribute("listesup", supervisors);
        System.out.println("Supervisors retrieved successfully.");
        return "coordinator/home";
    }

    @GetMapping("/coordinator/FormSup")
    public String formSup(Model model) {
        List<Service> listeService = serviceRepository.findAll();
        if (listeService.isEmpty())
            throw new RuntimeException("No services in the liste");
        // List<Equipe> listeEquipe = equipeService.getAllEquipes();
        // if(listeEquipe.size() == 0) throw new RuntimeException("No equipe in the
        // liste");
        model.addAttribute("listeService", listeService);
        // model.addAttribute("listeEquipe", listeEquipe);
        model.addAttribute("NewSup", new User());
        return "coordinator/FormSupervisor";
    }

    @PostMapping("/addSup")
    public String addSupervisor(User user, Authentication auth) {
        String idCord = userRepository.findIdByLogin(auth.getName());
        List<Equipe> equipe = equipeRepository.findEquipeByUserId(idCord);
        accountServiceImplementation.addSupervisor(user.getFirstName(), user.getLastName(), user.getSex(),
                user.getPhoneNumber(),
                user.getEmail(), user.getLogin(), user.getPassword(), user.getConfirmPassword(), user.getServices(),
                equipe);
        System.out.println("coedinateur  added successfully");
        return "redirect:/coordinator/teams";

    }

    @GetMapping("/coordinateur/employee")
    public String getEmployee(Model model, Authentication auth) {
        String idCord = userRepository.findIdByLogin(auth.getName());
        List<User> employees = new ArrayList<>();
        employees.addAll(accountServiceImplementation.getEmployeesbyTeamCord(idCord));
        System.out.println(employees);
        System.out.println("You are here __________________________________________");
         model.addAttribute("listeEmp", employees);
        System.out.println("empployee retrieved successfully.");
        return "coordinator/employee";
    }

    @GetMapping("/coordinator/FormEmp")
    public String formEmp(Model model) {
        List<Service> listeService = serviceRepository.findAll();
        if (listeService.isEmpty())
            throw new RuntimeException("No services in the liste");
        // List<Equipe> listeEquipe = equipeService.getAllEquipes();
        // if(listeEquipe.size() == 0) throw new RuntimeException("No equipe in the
        // liste");
        model.addAttribute("listeService", listeService);
        // model.addAttribute("listeEquipe", listeEquipe);
        model.addAttribute("NewEmp", new User());
        return "coordinator/FormEmployee";
    }

    @PostMapping("/addEmp")
    public String addEmployee(User user, Authentication auth) {
        String idCord = userRepository.findIdByLogin(auth.getName());
        List<Equipe> equipe = equipeRepository.findEquipeByUserId(idCord);
        accountServiceImplementation.addEmployee(user.getFirstName(), user.getLastName(), user.getSex(),
                user.getPhoneNumber(),
                user.getEmail(), user.getLogin(), user.getPassword(), user.getConfirmPassword(), user.getServices(),
                equipe);
        System.out.println("employee  added successfully");
        return "redirect:/coordinateur/employee";

    }
    // @PostMapping("/addTask")
    // public String addTask(Task task,  @RequestParam(name = "userId") String userId) {
    //     try {
    //         System.out.println("''''-------------------------");
    //         System.out.println(userId);
    //         User user = userRepository.findById(userId).orElseThrow(
    //                 () -> new Exception("User not found"));

    //         // Assign task to user first
    //         System.out.println("here si far");

    //         // Save the task after it's assigned to the user
    //         if (taskService.addTask(
    //                 task.getTitle(),
    //                 task.getDescription(),
                   
                   
    //                 //task.getDate_Creation(),
                  
    //                // task.getStatus(),
    //                 task.getGravity(),
    //                 user)

    //                 != null) {
    //         }

    //         System.out.println("Task added successfully!");
    //         System.out.println("Task affected to " + user.getLogin() + " successfully!");

    //         return "redirect:/supervisor/home";
    //     } catch (Exception e) {
    //         System.out.println("Error: " + e.getMessage());
    //         return "error";
    //     }
    // }


}
