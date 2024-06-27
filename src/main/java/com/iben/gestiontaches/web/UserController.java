package com.iben.gestiontaches.web;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.iben.gestiontaches.entities.Equipe;
import com.iben.gestiontaches.entities.Message;
import com.iben.gestiontaches.entities.Service;
import com.iben.gestiontaches.entities.Task;
import com.iben.gestiontaches.entities.User;
import com.iben.gestiontaches.entities.UserTaskAssignment;
import com.iben.gestiontaches.enums.deactivatedFlag;
import com.iben.gestiontaches.repository.EquipeRepository;
import com.iben.gestiontaches.repository.TaskRepository;
import com.iben.gestiontaches.repository.UserRepository;
import com.iben.gestiontaches.repository.UserTaskAssignmentRepository;
import com.iben.gestiontaches.services.AccountServiceImplementation;
import com.iben.gestiontaches.services.MessageServiceImplementation;
import com.iben.gestiontaches.services.TaskService;
import com.iben.gestiontaches.services.UserServices;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class UserController {

    private UserRepository userRepository;
    private AccountServiceImplementation accountService;
    private TaskService taskService;
    private TaskRepository taskRepository;
    private UserTaskAssignmentRepository userTaskRepo;
    private MessageServiceImplementation messageServiceImplementation;
    private UserServices userServices;

    private EquipeRepository equipeRepository;
    private PasswordEncoder passwordEncoder;

    // @GetMapping("/employee/home")
    // public String home() {
    // return "home";
    // }

    @PostMapping("/addEmployee")
    public String addUser(@Valid User user) {

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
                    // user.getStatus(user.setStatus(deactivatedFlag.ACTIVE)),
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
        if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_OP"))) {
            return "redirect:/employee/home";
        } else if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_SUP"))) {
            return "redirect:/supervisor/home";
        } else if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_CHEF_PROJET"))) {
            return "redirect:/chef_projet/home";
        } else if ((authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_COR")))) {
            return "redirect:/coordinator/home";
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
        String id = userRepository.findIdByLogin(login);
        List<Task> tasks = taskService.getTasksbyOp(id);
        model.addAttribute("tasks", tasks);
        model.addAttribute("taskUtility", new UserController(userRepository, accountService, taskService,
                taskRepository, userTaskRepo, messageServiceImplementation, userServices, equipeRepository, passwordEncoder));
        return "employee/home";
    }

    @GetMapping("/employee/profile")
    public String profile(Model model, Authentication auth) {
        User userForEdit = userRepository.findUserBylogin(auth.getName());
        List<Equipe> teams = equipeRepository.findEquipeByUserId(userForEdit.getId());
        model.addAttribute("team", teams);
        model.addAttribute("userForEdit", userForEdit);
        return "employee/profile";
    }

    @GetMapping("/employee/detailTask")
    public String detailTask(@RequestParam(name = "id") Long taskId, Model model, Authentication authentication) {
        Task task = taskService.findbyId(taskId);
        UserTaskAssignment userTask = userTaskRepo.findByTaskId(taskId);
        User sup = userRepository.findById(userTask.getSupUserId()).get();
        model.addAttribute("task", task);
        model.addAttribute("userTask", userTask);
        model.addAttribute("sup", sup);

        return "employee/detailTask";
    }

    @PostMapping("/employee/taskDone")
    public String changeTaskStatus(@RequestParam("id") Long idTask) {
        // TODO: process POST request using idTask

        taskService.upadteTaskStatus(idTask);
        return "redirect:/employee/home";
    }

    @GetMapping("/employee/conversation")
    public String getConvesasion(@RequestParam(name = "id") Long taskId, Model model, Authentication authentication) {
        String userId = userRepository.findIdByLogin(authentication.getName());

        List<Message> messagesSup = messageServiceImplementation.fetchMessagesForSup(userId, taskId);
        List<Message> messagesOp = messageServiceImplementation.fetchMessagesForOp(userId, taskId);

        //
        // Combine messages from supervisors and employees into a single list
        List<Message> combinedMessages = Stream.concat(messagesSup.stream(), messagesOp.stream())
                .collect(Collectors.toList());

        // Sort the combined list based on date_Creation attribute (timestamp)
        combinedMessages.sort(Comparator.comparing(Message::getDate_Creation));

        Task task = taskRepository.findById(taskId).get();

        model.addAttribute("combinedMessages", combinedMessages);
        model.addAttribute("task", task);
        model.addAttribute("mesg", new Message());
        return "employee/conversasion";
    }

    @PostMapping("/employee/addMessage")
    public String addMessage(@RequestParam("taskId") Long taskId, Authentication authentication, Message message) {

        User user = userRepository.findUserBylogin(authentication.getName());

        messageServiceImplementation.addMessageBySup(message.getContent(), LocalDate.now(), taskId, user.getId());

        return "redirect:/employee/conversation?id=" + taskId;
    }

    @GetMapping("/employee/editInfo")
    public String editInfo() {
        return "employee/editInfo";
    }

    @PostMapping("/employee/updatePassword")
    public String updatePassword(@RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmNewPassword") String confirmNewPassword,
            Authentication authentication,
            Model model) {
        String username = authentication.getName();
        User user = userRepository.findUserBylogin(username);

        // Verify old password
        if (!accountService.verifyPassword(user, oldPassword)) {
            // Old password doesn't match, return with an error message
            model.addAttribute("error", "Old password is incorrect.");
            return "employee/editInfo";
        }

        // Confirm new password
        if (!newPassword.equals(confirmNewPassword)) {
            model.addAttribute("error", "New passwords do not match.");
            return "employee/editInfo";
        }

        // Update password
        System.out.println("Password Upadated!");
        accountService.updatePassword(user, newPassword);

        return "redirect:/employee/profile";
    }

    @GetMapping("/forgot_password")
    public String forgot_password(Model model) {
        model.addAttribute("userP", new User());
        return "forgot_password";
    }

    @PostMapping("/reset_password")
    public String forgotPassword(@RequestParam("email") String email, @RequestParam("UserName") String userName,
            Model model) {
        User user = userRepository.findUserBylogin(userName);
        if (user == null) {
            System.out.println("Error: User not found!");
            model.addAttribute("error", "User not found");
            return "forgot_password";
        } else {
            if (user.getEmail().equals(email)) {
                System.out.println("Email Matched");
                // Generate a new random password
                String newPassword = userServices.generateRandomPassword();
                System.out.println(newPassword);
                // Update the user's password in the database
                user.setPassword(passwordEncoder.encode(newPassword)); // Assuming user.setPassword updates the password
                                                                       // in the database
                userRepository.save(user); // Save the updated user object
                // Send the new password to the user via email or provide it on the reset
                // password page

                userServices.sendNewPasswordByEmail(user.getEmail(), newPassword); // Implement this method to send
                                                                                   // email
                return "login"; // Redirect to the reset password page
            } else {
                System.out.println("Email Not Matched");
                model.addAttribute("error", "Email not matched");
                return "login";
            }
        }
    }

    public String getDeadline(Task task) {
        if (task == null || task.getCalendar() == null || task.getCalendar().getStartDate() == null) {
            return "Empty"; // Return "Empty" if task or calendar or startDate is null
        }
        LocalDate deadline = task.getCalendar().getStartDate().plusDays(task.getCalendar().getDuration());
        return deadline.toString(); // Convert LocalDate to String
    }

}
