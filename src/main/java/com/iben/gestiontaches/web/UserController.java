package com.iben.gestiontaches.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.iben.gestiontaches.entities.Service;
import com.iben.gestiontaches.entities.Task;
import com.iben.gestiontaches.entities.User;
import com.iben.gestiontaches.enums.deactivatedFlag;
import com.iben.gestiontaches.repository.UserRepository;
import com.iben.gestiontaches.services.AccountServiceImplementation;
import com.iben.gestiontaches.services.TaskService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class UserController {

    private UserRepository userRepository;
    private AccountServiceImplementation accountService;
    private TaskService taskService;

    // @GetMapping("/employee/home")
    // public String home() {
    // return "home";
    // }



    @PostMapping("/addEmployee")
    public String addUser(@Valid  User user) {

        try {

            if (user.getStatus() == null) {
                user.setStatus(deactivatedFlag.ACTIVE);
            }
            // Call the addUser method from the accountService
            accountService.addUser(
                     user.getFirstName(),
                     user.getLastName(),
                     user.getSex(),
                     user.getPhoneNumber(),
                     user.getEmail(),
                     user.getLogin(),
                     user.getPassword(),
                     //user.getStatus(user.setStatus(deactivatedFlag.ACTIVE)),
                     user.getConfirmPassword(), // Confirm password is not used in this context
                     user.getRoles(),
                     user.getServices());

            System.out.println("user added successfully!");
            return "redirect:/chef_projet/home";
        } catch (RuntimeException e) {
            // Handle any runtime exceptions thrown by accountService.addUser()
            System.out.println("Error: " + e.getMessage());
            return "error";
        }

    }

    public String addRole(String nameRole) {
        try {
            accountService.addNewRole(nameRole);

            return "Role added successfully!";
        } catch (RuntimeException e) {
            // Handle any runtime exceptions thrown by accountService.addNewRole()
            System.out.println("Error: " + e.getMessage());
            return "error";
        }

    }

    public String addRoleToUser(String userId, Long idRole) {
        try {
            accountService.addRoletoUser(userId, idRole);
            return "Role added to User successfully!";
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return "error";
            // TODO: handle exception
        }

    }

    public String removeRoleToUser(String userId, Long idRole) {
        try {
            accountService.romeveRoleFromUser(userId, idRole);
            return "Role removed to User successfully!";
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return "error";
            // TODO: handle exception
        }

    }

    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_EMP"))) {
            return "redirect:/employee/home";
        } else if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_SUP"))) {
            return "redirect:/supervisor/home";
        } else if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_CHEF_PROJET"))) {
            return "redirect:/chef_projet/home";
        } else {
            throw new IllegalStateException("Unknown user this error role");
        }
    }

    @GetMapping("/supervisor/home")
    public String getUsersBySupervisorServices(Model model, Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findUserBylogin(username);
        if (user != null) {
            List<Service> services = user.getServices();
            List<Long> serviceIds = services.stream().map(Service::getId).collect(Collectors.toList());
            List<User> users = new ArrayList<>();
            for (Long serviceId : serviceIds) {
                users.addAll(accountService.getUsersBySupervisorService(serviceId));
            }

            System.out.println("u here __________________________________________");
            model.addAttribute("listeUser", users);
            System.out.println("Users retrieved successfully.");
            return "supervisor/home";

        } else {
            // Handle the case where the user is not found
            return "redirect:/login"; // Redirect to login page or handle appropriately
        }

        // List<User> userListe = userRepository.findAll();
        // System.out.println(userListe);

        // System.out.println( model.getAttribute(null));

    }

    @GetMapping("/employee/home")
    public String getTasksForEmployee(Model model, Authentication authentication) {
        String login = authentication.getName();
       String id= userRepository.findIdByLogin(login);
         List<Task> tasks = taskService.getTasks(id);
         model.addAttribute("tasks", tasks);
         //System.out.println("--------------------------------");
        // System.out.println(tasks);
        return "employee/home";
    }



}
