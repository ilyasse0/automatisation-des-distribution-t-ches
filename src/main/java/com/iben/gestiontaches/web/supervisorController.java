package com.iben.gestiontaches.web;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.iben.gestiontaches.entities.Calendar;
import com.iben.gestiontaches.entities.Gravity;
import com.iben.gestiontaches.entities.Message;
import com.iben.gestiontaches.entities.Service;
import com.iben.gestiontaches.entities.Task;
import com.iben.gestiontaches.entities.User;
import com.iben.gestiontaches.entities.UserTaskAssignment;
import com.iben.gestiontaches.repository.TaskRepository;
import com.iben.gestiontaches.repository.UserRepository;
import com.iben.gestiontaches.services.AccountService;
import com.iben.gestiontaches.services.GravityService;
import com.iben.gestiontaches.services.MessageServiceImplementation;
import com.iben.gestiontaches.services.TaskService;

import jakarta.persistence.EntityExistsException;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class supervisorController {
    private TaskService taskService;
    private TaskRepository taskRepository;
    private UserRepository userRepository;
    private GravityService gravityService;
    private AccountService accountService;
    private MessageServiceImplementation messageServiceImplementation;

    // @PostMapping("/addTask")
    // public String addTask(Task task, Calendar calendar, @RequestParam(name =
    // "userId") String userId) {
    // try {
    // System.out.println("''''-------------------------");
    // System.out.println(userId);
    // User user = userRepository.findById(userId).orElseThrow(
    // () -> new Exception("User not found"));

    // // Assign task to user first
    // System.out.println("here si far");

    // // Save the task after it's assigned to the user
    // if (taskService.addTask(
    // task.getTitle(),
    // task.getDescription(),
    // calendar.getStartDate(),
    // calendar.getLatestStartDate(),
    // //task.getDate_Creation(),
    // calendar.getDuration(),
    // // task.getStatus(),
    // task.getGravity(),
    // user)

    // != null) {

    // // this is duplicated!
    // // taskService.AffecteTaskToUser(task, user);
    // System.out.println("cheeck here!");

    // }

    // System.out.println("Task added successfully!");
    // System.out.println("Task affected to " + user.getLogin() + " successfully!");

    // return "redirect:/supervisor/home";
    // } catch (Exception e) {
    // System.out.println("Error: " + e.getMessage());
    // return "error";
    // }
    // }

    @GetMapping("/supervisor/FormTask")
    public String home(@RequestParam(name = "id") String userId, Model model) {
        List<Gravity> listeGravity = gravityService.getAllGravity();
        if (listeGravity.isEmpty())
            throw new EntityExistsException("liste gravity is empty");
        model.addAttribute("listeGravity", listeGravity);
        model.addAttribute("task", new Task());
        model.addAttribute("calender", new Calendar());
        model.addAttribute("userId", userId);
        return "FormTask";

    }

    @GetMapping("/supervisor/tasks")
    public String gettasks(Model model, Authentication auth) {
        String idSup = userRepository.findIdByLogin(auth.getName());
        List<Task> listeTask = taskService.getTasksbySup(idSup);
        model.addAttribute("listeTask", listeTask);
        User user = userRepository.findById(idSup).get();

        List<Service> services = user.getServices();
        List<Long> serviceIds = services.stream().map(Service::getId).collect(Collectors.toList());
        List<User> users = new ArrayList<>();
        for (Long serviceId : serviceIds) {
            users.addAll(accountService.getUsersBySupervisorService(serviceId));
        }
        model.addAttribute("task", new Task());
        model.addAttribute("calender", new Calendar());
        model.addAttribute("userAssign", new UserTaskAssignment());

        model.addAttribute("listeEmp", users);
        return "supervisor/tasks";
    }

    @PostMapping("/supervisor/addTask")
    public String addTask(@RequestParam("taskId") Long taskId, Calendar calendar, Model model,
            UserTaskAssignment opId) {
        Optional<Task> oldTask = taskRepository.findById(taskId);
        model.addAttribute("task", oldTask);

        taskService.addTaskBySup(taskId, opId.getOpUserId(), calendar.getDuration(), calendar.getStartDate());
        return "redirect:/supervisor/tasks";
    }


    @PostMapping("/addMessage")
    public String addMessage(@RequestParam("id") Long taskId, Authentication authentication, Message message) {

        User user = userRepository.findUserByid(authentication.getName());
        messageServiceImplementation.addMessageBySup(message.getContent(), LocalDate.now(), taskId, "c5a1d3d4-476a-44c7-830f-084afe033a52");

        return "redirect:/supervisor/conversation";
    }

    @GetMapping("/supervisor/conversation")
    public String conversation( @RequestParam(name = "id") Long taskId ,Model model , Authentication authentication ) {
        String userId = userRepository.findIdByLogin(authentication.getName());
         List<Message> messagesSup = messageServiceImplementation.fetchMessagesForSup(userId, taskId);
         List<Message> messagesOp = messageServiceImplementation.fetchMessagesForOp(userId, taskId);

         System.out.println("THE MESSAGE LISTE!");
         for (Message message : messagesSup) {
            message.getContent();
         }
        
         model.addAttribute("messagesSup", messagesSup);
         model.addAttribute("messagesOp", messagesOp);
         model.addAttribute("mesg", new Message());

        return "supervisor/Conversation";
    }

}
