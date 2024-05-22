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

import com.iben.gestiontaches.entities.Equipe;
import com.iben.gestiontaches.entities.Gravity;
import com.iben.gestiontaches.entities.Role;
import com.iben.gestiontaches.entities.Service;
import com.iben.gestiontaches.entities.Status;
import com.iben.gestiontaches.entities.Task;
import com.iben.gestiontaches.entities.User;
import com.iben.gestiontaches.entities.UserTaskAssignment;
import com.iben.gestiontaches.enums.deactivatedFlag;
import com.iben.gestiontaches.enums.statusCode;
import com.iben.gestiontaches.repository.RoleRepository;
import com.iben.gestiontaches.repository.StatusRepository;
import com.iben.gestiontaches.repository.UserRepository;
import com.iben.gestiontaches.repository.UserTaskAssignmentRepository;
import com.iben.gestiontaches.services.AccountServiceImplementation;
import com.iben.gestiontaches.services.AdminServiceImplementation;
import com.iben.gestiontaches.services.EquipeServiceImplementation;
import com.iben.gestiontaches.services.GravityServiceImplementation;
import com.iben.gestiontaches.services.StatusServiceImplementation;
import com.iben.gestiontaches.services.TaskServiceImplementation;

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
    private EquipeServiceImplementation equipeService;
    private TaskServiceImplementation taskServiceImplementationFactory;
    private UserTaskAssignmentRepository assignmentRepository;
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @GetMapping("/chef_projet/FormEmp")
    public String home(Model model) {
        List<Service> listeService = adminService.getAllServices();
        List<Role> listeRoles = adminService.getAllRoles();
        if (listeService.isEmpty())
            throw new RuntimeException("No services in the liste");
        if (listeRoles.isEmpty())
            throw new RuntimeException("No roles in the list");
        model.addAttribute("listeService", listeService);
        model.addAttribute("listeRoles", listeRoles);
        model.addAttribute("user", new User());
        return "FormEmp";

    }

    @GetMapping("/chef_projet/editEmp")
    public String editEmp(Model model, @RequestParam(name = "login") String login) {
        User user = accountService.editUser(login);
        if (user != null) {
            model.addAttribute("userEdited", user);
        }
        return "EditEmp";
    }

    @GetMapping("/chef_projet/home")
    public String getUsersByChefProjetServices(Model model, Authentication authentication) {
        List<Task> listeTasks = taskServiceImplementationFactory.getTasks();
        List<Task> ComptedTask = new ArrayList<Task>();
        List<Task> UnComptedTask = new ArrayList<Task>();
        for (Task task : listeTasks) {
            if (task.getStatus_task_temp().equals(statusCode.DONE)) {
                // System.out.println("Task " + task.getId() + " is completed.");

                ComptedTask.add(task);
            } else if (!(task.getStatus_task_temp().equals(statusCode.DONE))) {
                // System.out.println("Task " + task.getId() + " is not completed.");

                UnComptedTask.add(task);
            }
        }

        // model.addAttribute("listTasks", listeTasks);
        model.addAttribute("listTasksCompleted", ComptedTask);
        model.addAttribute("listTasksUncompeted", UnComptedTask);

        model.addAttribute("taskUtility", new AdminCtroller(adminService, accountService, gravityService, statusService,
                equipeService, taskServiceImplementationFactory, assignmentRepository, userRepository, roleRepository));

        return "chef_projet/home";

    }

    @GetMapping("/chef_projet/teams")
    public String homeTeam(Model model) {
        List<Equipe> equipe = adminService.getAllEquipes();
        List<User> cordinateurListe = adminService.getCordinateur();
        if (equipe.isEmpty())
            throw new RuntimeException("No equipe in the liste");
        model.addAttribute("equipe", equipe);
        model.addAttribute("Cordinateur", cordinateurListe);
        return "chef_projet/teams";
    }

    @GetMapping("/chef_projet/employee")
    public String homeEmployee(Model model) {
        List<User> operateurs = adminService.getEmployees("OP");
        List<User> superviseurs = adminService.getEmployees("SUP");
        model.addAttribute("supervisorList", superviseurs);
        model.addAttribute("operatorList", operateurs);
        return "chef_projet/employee";
    }

    @GetMapping("chef_projet/FormEquipe")
    public String formTeam(Model model) {
        model.addAttribute("NewEquipe", new Equipe());
        return "chef_projet/FormEquipe";
    }

    public String addGravity(@Valid Gravity gravity) {
        gravityService.addNewGravity(gravity);
        return "redirect:/chef_projet/home";

    }

    public String addStatus(@Valid Status status) {
        statusService.addNewStatus(status);
        return "redirect:/chef_projet/home";

    }

    @PostMapping("/addEquipe")
    public String addEquipe(Equipe equipe) {
        adminService.addEquipe(equipe.getNom(), equipe.getDescription(), equipe.getStatus());
        System.out.println("Team added successfully");
        return "redirect:/chef_projet/teams";
    }

    @PostMapping("/addCord")
    public String addCordinateur(User user) {
        adminService.addCordinateur(user.getFirstName(), user.getLastName(), user.getSex(), user.getPhoneNumber(),
                user.getEmail(), user.getLogin(), user.getPassword(), user.getConfirmPassword(), user.getServices(),
                user.getEquipes());
        System.out.println("coedinateur  added successfully");
        return "redirect:/chef_projet/teams";

    }

    @GetMapping("chef_projet/FormCord")
    public String formCord(Model model) {
        List<Service> listeService = adminService.getAllServices();
        if (listeService.isEmpty())
            throw new RuntimeException("No services in the liste");
        List<Equipe> listeEquipe = equipeService.getAllEquipes();
        if (listeEquipe.size() == 0)
            throw new RuntimeException("No equipe in the liste");
        model.addAttribute("listeService", listeService);
        model.addAttribute("listeEquipe", listeEquipe);
        model.addAttribute("NewCor", new User());
        return "chef_projet/FormCordinateur";
    }

    @GetMapping("chef_projet/EditCord")
    public String editCord(Model model,String id) {

        User user = userRepository.findById(id).get();
        List<Service> listeService = adminService.getAllServices();
        if (listeService.isEmpty())
            throw new RuntimeException("No services in the liste");
        List<Equipe> listeEquipe = equipeService.getAllEquipes();
        if (listeEquipe.size() == 0)
            throw new RuntimeException("No equipe in the liste");

        List<Role> roles = roleRepository.findAll();
        model.addAttribute("listeService", listeService);
        model.addAttribute("listeEquipe", listeEquipe);
            model.addAttribute("userEdited", user);
            model.addAttribute("roles", roles);
            return "chef_projet/EditCordinateur";
    }

    @GetMapping("/deleteEquipe")
    public String delete(Long id) {
        equipeService.deleteEquipe(id);
        return "redirect:/chef_projet/teams";
    }

    @GetMapping("/chef_projet/editEquipe")
    public String editTeam(Long id , Model model){
       Equipe equipe =  equipeService.findTeam(id);
       model.addAttribute("equipe", equipe);
       return "chef_projet/editEquipe";

    }
    @PostMapping("/chef_projet/updateEquipe")
    public String updateEquipe( @RequestParam("id") Long id , Equipe team) {
        Equipe equipe =  equipeService.findTeam(id);
        equipe.setNom(team.getNom());
        equipe.setDescription(team.getDescription());
        equipe.setStatus(team.getStatus());
        adminService.saveEquipe(equipe);
        return "redirect:/chef_projet/teams";
    }

    @GetMapping("/deleteUser")
    public String delete(String id) {
        accountService.deleteUser(id);

        return "redirect:/chef_projet/teams";
    }

    public String getCorOrSupForTask(Long taskId, String role) {
        UserTaskAssignment assignment = assignmentRepository.findByTaskId(taskId);
        if (assignment != null) {
            String userId = null;
            if (role.equalsIgnoreCase("coordinator")) {
                userId = assignment.getCorUserId();
            } else if (role.equalsIgnoreCase("supervisor")) {
                userId = assignment.getSupUserId();
            } else if (role.equalsIgnoreCase("operateur")) {
                userId = assignment.getOpUserId();
            }
            if (userId != null) {
                User user = userRepository.findById(userId).orElse(null);
                if (user != null) {
                    return user.getLastName();
                }
            }
        }
        return "EMPTY"; // or any default value indicating no coordinator or supervisor found for the
                        // task
    }



    @PostMapping("/chef_projet/updateCord")
    public String updateCord( @RequestParam("id") String id , User user) {
        // List<Role> roles = new ArrayList<Role>();
        // Role role = roleRepository.findById(3L).get();
        //roles.add(role);
        User usr =  userRepository.findById(id).get();
        usr.setFirstName(user.getFirstName());
        usr.setLastName(user.getLastName());
        usr.setSex(user.getSex());
        usr.setStatus(user.getStatus());
        usr.setPhoneNumber(user.getPhoneNumber());
        usr.setEmail(user.getEmail());
        usr.setPassword(user.getPassword());
        usr.setLogin(user.getLogin());
        usr.setEquipes(user.getEquipes());
        usr.setServices(user.getServices());
        usr.setRoles(user.getRoles());
        System.out.println(user.getServices());
        System.out.println(user.getRoles());
        adminService.saveCord(user);
       
        return "redirect:/chef_projet/teams";
    }


    @GetMapping("/chef_projet/profile")
    public String profile(Model model , Authentication aut) {
        User user = userRepository.findUserBylogin(aut.getName());
        model.addAttribute("userProfile", user);
        return "chef_projet/profile";
    }
    

}