package com.iben.gestiontaches.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.header.XFrameOptionsServerHttpHeadersWriter.Mode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.iben.gestiontaches.entities.Gravity;
import com.iben.gestiontaches.entities.Role;
import com.iben.gestiontaches.entities.Service;
import com.iben.gestiontaches.entities.Status;
import com.iben.gestiontaches.entities.User;
import com.iben.gestiontaches.enums.deactivatedFlag;
import com.iben.gestiontaches.repository.StatusRepository;
import com.iben.gestiontaches.services.AccountServiceImplementation;
import com.iben.gestiontaches.services.AdminServiceImplementation;
import com.iben.gestiontaches.services.GravityServiceImplementation;
import com.iben.gestiontaches.services.StatusServiceImplementation;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

/**
 * AdminCtroller
 */

@Controller
@AllArgsConstructor
public class AdminCtroller {

    private AdminServiceImplementation adminService;
    private AccountServiceImplementation accountService;
    private GravityServiceImplementation gravityService;
    private StatusServiceImplementation statusService;

    @GetMapping("/chef_projet/FormEmp")
    public String home(Model model) {
        List<Service> listeService = adminService.getAllServices();
        List<Role> listeRoles = adminService.getAllRoles();
        if (listeService.isEmpty()) throw new RuntimeException("No services in the liste");
        if(listeRoles.isEmpty()) throw new RuntimeException("No roles in the list");
        model.addAttribute("listeService", listeService);
        model.addAttribute("listeRoles", listeRoles);
        model.addAttribute("user", new User());
        return "FormEmp";

    }

    @GetMapping("/chef_projet/editEmp") 
    public String editEmp(Model model ,@RequestParam(name="login") String login){
        
        User user =accountService.editUser(login);
        if (user!=null) {
            model.addAttribute("userEdited" ,  user);
        }
        return "EditEmp";


    } 


    
  


    @GetMapping("/chef_projet/home")
    public String getUsersByChefProjetServices(Model model, Authentication authentication) {
        //String username = authentication.getName();
        //User user = userRepository.findUserBylogin(username);
        
           // List<Service> services = user.getServices();
          // List<Long> serviceIds = services.stream().map(Service::getId).collect(Collectors.toList());
            List<User> users = new ArrayList<>();
            
            users.addAll(accountService.getUsersByChefProjetService());
            System.out.println("You are here __________________________________________");
            model.addAttribute("listeUser", users);
            System.out.println("Users retrieved successfully.");
            return "chef_projet/home";
        
         
    }

    public String addGravity(@Valid Gravity gravity){

         gravityService.addNewGravity(gravity);
         return "redirect:/chef_projet/home";

    }

    public String addStatus(@Valid Status status){

        statusService.addNewStatus(status);
        return "redirect:/chef_projet/home";

   }

    


}