package com.iben.gestiontaches.web;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.iben.gestiontaches.entities.Equipe;
import com.iben.gestiontaches.entities.Gravity;
import com.iben.gestiontaches.entities.Role;
import com.iben.gestiontaches.entities.Service;
import com.iben.gestiontaches.entities.Task;
import com.iben.gestiontaches.entities.User;
import com.iben.gestiontaches.entities.UserTaskAssignment;
import com.iben.gestiontaches.repository.EquipeRepository;
import com.iben.gestiontaches.repository.RoleRepository;
import com.iben.gestiontaches.repository.ServiceRepository;
import com.iben.gestiontaches.repository.StatusRepository;
import com.iben.gestiontaches.repository.TaskRepository;
import com.iben.gestiontaches.repository.UserRepository;
import com.iben.gestiontaches.repository.UserTaskAssignmentRepository;
import com.iben.gestiontaches.services.AccountServiceImplementation;
import com.iben.gestiontaches.services.GravityService;
import com.iben.gestiontaches.services.TaskService;

import jakarta.persistence.EntityExistsException;
import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@AllArgsConstructor
public class coordinatorController {
    AccountServiceImplementation accountServiceImplementation;
    UserRepository userRepository;
    private EquipeRepository equipeRepository;
    private ServiceRepository serviceRepository;
    private TaskService taskService;
    private TaskRepository taskRepository;
    private GravityService gravityService;
    private StatusRepository statusRepository;
    private RoleRepository roleRepository;
    private UserTaskAssignmentRepository assignmentRepository;

    @GetMapping("/coordinator/home")
    public String getUsersByChefProjetServices(Model model, Authentication auth) {
        String idCord = userRepository.findIdByLogin(auth.getName());
        List<User> supervisors = new ArrayList<>();
        supervisors.addAll(accountServiceImplementation.getSupervisorsbyTeamCord(idCord));
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
        System.out.println("Supervisor added successfully");
        return "redirect:/coordinator/home";

    }

    @GetMapping("/coordinateur/employee")
    public String getEmployee(Model model, Authentication auth) {
        String idCord = userRepository.findIdByLogin(auth.getName());
        List<User> employees = new ArrayList<>();
        employees.addAll(accountServiceImplementation.getEmployeesbyTeamCord(idCord));
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

    @PostMapping("/addTask")
    public String addTask(Task task, Authentication auth, @RequestParam(name = "userId") String supId) {
        try {
            User user = userRepository.findById(supId).orElseThrow(
                    () -> new Exception("User not found"));

            String corId = userRepository.findIdByLogin(auth.getName());
            taskService.addTaskByCor(task.getTitle(), task.getDescription(), task.getGravity(), corId, supId);
            System.out.println("Task added successfully!");
            System.out.println("Task affected to " + user.getLogin() + " successfully!");
            return "redirect:/coordinator/home";
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return "error";
        }
    }

    // public String addTaskForTestOnly(Task task, Gravity gravity, String cordId,
    // String supId) {
    // // Save the task after it's assigned to the user
    // taskService.addTaskByCor(task.getTitle(), task.getDescription(), gravity,
    // cordId, supId);
    // System.out.println("Task added successfully!");
    // return "done";
    // }

    @GetMapping("/coordinateur/FormTask")
    public String FormTask(@RequestParam(name = "id") String userId, Model model) {
        List<Gravity> listeGravity = gravityService.getAllGravity();
        if (listeGravity.isEmpty())
            throw new EntityExistsException("liste gravity is empty");
        model.addAttribute("listeGravity", listeGravity);
        model.addAttribute("task", new Task());
        model.addAttribute("userId", userId);
        return "coordinator/FormTask";

    }

    @GetMapping("/coordinateur/tasks")
    public String gettasks(Model model, Authentication auth) {
        String idCord = userRepository.findIdByLogin(auth.getName());
        List<Task> listeTask = taskService.getTasksbyCord(idCord);

        // Calculate deadlines for each task and store them in a list
   
        model.addAttribute("listeTask", listeTask);
    
        model.addAttribute("taskUtility",
                new coordinatorController(accountServiceImplementation, userRepository, equipeRepository,
                        serviceRepository, taskService, taskRepository, gravityService, statusRepository,
                        roleRepository, assignmentRepository));
        return "coordinator/tasks";
    }

    @GetMapping("/coordinateur/profile")
    public String profile(Model model, Authentication auth) {
        User userForEdit = userRepository.findUserBylogin(auth.getName());
        List<Equipe> teams = equipeRepository.findEquipeByUserId(userForEdit.getId());
        model.addAttribute("userForEdit", userForEdit);
        System.out.println(teams);
        model.addAttribute("team", teams);
        return "coordinator/profile";
    }

    @GetMapping("/coordinateur/editSupervispr")
    public String editSup(@RequestParam(name = "id") String userId, Model model) {
        List<Service> listeService = serviceRepository.findAll();
        User user = userRepository.findById(userId).get();
        List<Service> service = user.getServices();
        List<Role> roleDefault = user.getRoles();
        if (listeService.isEmpty())
            throw new RuntimeException("No services in the liste");
        List<Equipe> listeEquipe = equipeRepository.findAll();
        if (listeEquipe.size() == 0)
            throw new RuntimeException("No equipe in the liste");

        List<Role> roles = roleRepository.findAll();
        Role roleRemove = roleRepository.findRoleByname("CHEF_PROJET");
        roles.remove(roleRemove);
        model.addAttribute("listeService", listeService);
        model.addAttribute("listeEquipe", listeEquipe);
        model.addAttribute("userEdit", user);
        model.addAttribute("service", service);
        model.addAttribute("roles", roles);
        model.addAttribute("roleDefault", roleDefault);
        return "coordinator/editemployee";
    }

    @PostMapping("/coordinateur/updateSupervispr/{id}")
    public String postMethodName(User supervisor, @PathVariable("id") String id) {
        User user = userRepository.findById(id).get();
         user.setLastName(supervisor.getLastName());
        user.setEmail(supervisor.getEmail());
        user.setPhoneNumber(supervisor.getPhoneNumber());
        user.setStatus(supervisor.getStatus());

      
        // user.setFirstName(supervisor.getFirstName());
        user.setRoles(supervisor.getRoles());
        user.setServices(supervisor.getServices());
        // System.out.println(supervisor.getServices());
        // System.out.println(supervisor.getRoles());
        System.out.println(supervisor.getStatus());

        // List<Service> servc = user.getServices();
        // for (Service service : servc) {
        // System.out.println("Service: " + service.getName()); // Assuming Service
        // class has a getName() method
        // }

        userRepository.save(user);
        System.out.println("Updated done!");

        return "redirect:/coordinator/home";
    }

    @GetMapping("/coordinator/deleteTask")
    public String deleteTask(Long id) {
        taskService.removeTask(id);
        return "redirect:/coordinateur/tasks";
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
    public String getDeadline(Task task) {
        if (task == null || task.getCalendar() == null || task.getCalendar().getStartDate() == null) {
            return "Empty"; // Return "Empty" if task or calendar or startDate is null
        }
        LocalDate deadline = task.getCalendar().getStartDate().plusDays(task.getCalendar().getDuration());
       // System.out.println(deadline);
        return deadline.toString(); // Convert LocalDate to String
    }

    @GetMapping("/coordinator/deleteUser")
    public String deleteUser(String id) {
        userRepository.deleteById(id);
        return "redirect:/coordinator/home";
    }

    @GetMapping("/coordinator/editInfo")
    public String editInfo() {
        return "coordinator/editInfo";
    }

    @PostMapping("/coordinator/updatePassword")
    public String updatePassword(@RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmNewPassword") String confirmNewPassword,
            Authentication authentication,
            Model model) {
        String username = authentication.getName();
        User user = userRepository.findUserBylogin(username);

        // Verify old password
        if (!accountServiceImplementation.verifyPassword(user, oldPassword)) {
            // Old password doesn't match, return with an error message
            model.addAttribute("error", "Old password is incorrect.");
            return "coordinator/editInfo";
        }

        // Confirm new password
        if (!newPassword.equals(confirmNewPassword)) {
            model.addAttribute("error", "New passwords do not match.");
            return "coordinator/editInfo";
        }

        // Update password
        System.out.println("Password Upadated!");
        accountServiceImplementation.updatePassword(user, newPassword);

        return "redirect:/coordinateur/profile";
    }


    @GetMapping("/coordinator/initializeTasks")
    public String initializeTasks(Authentication auth) {
        try {
            String idCord = userRepository.findIdByLogin(auth.getName());
            taskService.removeAllTasksByCoordinator(idCord);
            System.out.println("All tasks initialized successfully.");
            return "redirect:/coordinateur/tasks";
        } catch (Exception e) {
            System.out.println("Error during task initialization: " + e.getMessage());
            return "error";
        }
    }


    @GetMapping("/coordinateur/editTask")
    public String editTask(@RequestParam(name = "id") Long taskId, Model model) {
        
       
       Task task = taskRepository.findById(taskId).get();
       model.addAttribute("task", task);
       List<Gravity> listeGravity = gravityService.getAllGravity();
       model.addAttribute("listeGravity", listeGravity);

       Gravity gravity = task.getGravity();
       model.addAttribute("gravity", gravity);
      // System.out.println(task.getGravity().getPriority());

       

     
        return "coordinator/editTask";
    }

    @PostMapping("/coordinateur/updateTask/{id}")
    public String updateTask(Task task, @PathVariable("id") Long id) {
        Task tasktoUpdate = taskRepository.findById(task.getId()).get();
        tasktoUpdate.setTitle(task.getTitle());
        tasktoUpdate.setDescription(task.getDescription());

       

      
      

        taskRepository.save(tasktoUpdate);
        System.out.println("Updated done!");

        return "redirect:/coordinateur/tasks";
    }


}
