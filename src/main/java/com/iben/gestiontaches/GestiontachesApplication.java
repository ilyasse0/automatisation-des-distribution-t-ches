package com.iben.gestiontaches;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;

import com.iben.gestiontaches.entities.Calendar;
import com.iben.gestiontaches.entities.Equipe;
import com.iben.gestiontaches.entities.Gravity;
import com.iben.gestiontaches.entities.Service;
import com.iben.gestiontaches.entities.Status;
import com.iben.gestiontaches.entities.Task;
import com.iben.gestiontaches.entities.User;
import com.iben.gestiontaches.enums.Priority;
import com.iben.gestiontaches.enums.deactivatedFlag;
import com.iben.gestiontaches.enums.statusCode;
import com.iben.gestiontaches.repository.GravityRepository;
import com.iben.gestiontaches.repository.RoleRepository;
import com.iben.gestiontaches.repository.ServiceRepository;
import com.iben.gestiontaches.repository.StatusRepository;
import com.iben.gestiontaches.repository.TaskRepository;
import com.iben.gestiontaches.repository.UserRepository;
import com.iben.gestiontaches.services.AccountService;
import com.iben.gestiontaches.services.AdminServiceImplementation;
import com.iben.gestiontaches.services.GravityService;
import com.iben.gestiontaches.services.StatusService;
import com.iben.gestiontaches.services.TaskService;
import com.iben.gestiontaches.web.AdminCtroller;
import com.iben.gestiontaches.web.UserController;
import com.iben.gestiontaches.web.supervisorController;

import lombok.AllArgsConstructor;

@SpringBootApplication
@AllArgsConstructor
public class GestiontachesApplication {

	// private UserRepository userRepository;
	private RoleRepository roleRepository;
	private ServiceRepository serviceRepository;
	// private GravityService gravityService;
	// private GravityRepository gravityRepository;
	// private StatusRepository statusRepository;
	// private StatusService statusService;
	// private TaskRepository taskRepository;
	// private TaskService taskService;
	// private supervisorController supervisorController;
	//private AdminServiceImplementation adminServiceImplementation;
	//private AdminCtroller adminCtroller;


	public static void main(String[] args) {
		SpringApplication.run(GestiontachesApplication.class, args);

	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	//@Bean
	CommandLineRunner commandLineRunnerUserDetails(AccountService accountService) {
		return args -> {
			// accountService.addNewRole("OP");
			// accountService.addNewRole("SUP");
			// accountService.addNewRole("COR");
			// accountService.addNewRole("CHEF_PROJET");


			//  accountService.addNewService("CONTROLLE");
			//  accountService.addNewService("SAISIE");
			//  accountService.addNewService("SCANNE");
			//  accountService.addNewService("TECHNIQUE");

			// accountService.addUser("John", "Doe", "female", "0606060606",
			// "joe@exemple.com", "doe",
			// "123", deactivatedFlag.ACTIVE,
			// "123",
			// List.of(roleRepository.findRoleByname("EMP")),
			// List.of(serviceRepository.findServiceByname("SAISIE")));
		};
	}

		@Bean
	CommandLineRunner commandeLineRunnerController(UserController controller) {
		User userForTest = new User();
	    userForTest.setFirstName("admin");
		userForTest.setLastName("admin");
		userForTest.setSex("male");
		userForTest.setPhoneNumber("0606060606");
		userForTest.setEmail("admin@exemple.com");
		userForTest.setLogin("admin");
		userForTest.setPassword("123321");
		userForTest.setStatus(deactivatedFlag.ACTIVE);
		userForTest.setConfirmPassword("123321");
		userForTest.setRoles(List.of(roleRepository.findRoleByname("CHEF_PROJET")));
		userForTest.setServices(List.of(serviceRepository.findServiceByname("SAISIE"),
		serviceRepository.findServiceByname("SCANNE"),
		serviceRepository.findServiceByname("TECHNIQUE"),
		serviceRepository.findServiceByname("CONTROLLE")));



		User userCordFortest = new User();
		userCordFortest.setFirstName("cordTest");
		userCordFortest.setLastName("cordTest");
		userCordFortest.setSex("male");
		userCordFortest.setPhoneNumber("0606060606");
		userCordFortest.setEmail("cordTest@exemple.com");
		userCordFortest.setLogin("cord");
		userCordFortest.setPassword("123");
		userCordFortest.setStatus(deactivatedFlag.ACTIVE);
		userCordFortest.setConfirmPassword("123");
		userCordFortest.setServices(List.of(serviceRepository.findServiceByname("SAISIE"),
        serviceRepository.findServiceByname("SCANNE"),
        serviceRepository.findServiceByname("TECHNIQUE"),
        serviceRepository.findServiceByname("CONTROLLE")));


	    //  Gravity gravity = new Gravity();
		//  gravity.setPriority(Priority.MEDIUM);
		//  gravity.setDescription("this is a test gravity to see that the backend works!");

		// Status status = new Status();
		//  status.setDescription("this a test status to see that the backend works!");
		//  status.setUseConstraint("just a test no constraint");
		//  status.setStatus(statusCode.IN_PROGRESS);

		// Calendar calendar = new Calendar();
		// Task task = new Task();
		// calendar.setDuration(3);
		// LocalDate startDate = LocalDate.of(2020, 2, 22);
		// calendar.setStartDate(startDate);

		// task.setId(1L);
		// task.setCalendar(calendar);
		// task.setDescription("this is a test task with affectation");
		// task.setGravity(gravityRepository.findById(1L).get());
		// task.setStatus(statusRepository.findById(1L).get());
		// task.setDate_Creation(LocalDate.now());
		// task.setTitle("TEST Task");

		return args -> {
			//controller.addUser(userForTest);
			// Long idRole = roleRepository.findRoleByname("CHEF_PROJET").getId();
			// String idUser = userRepository.findIdByLogin(userForTest.getLogin());
			// controller.addRoleToUser(idUser,idRole);
			// controller.removeRoleToUser(idUser, idRole);
			// List<Service> services = userForTest.getServices();
			// List<Long> listeIdServices = new ArrayList<>();
			// for (Service service : services) {
			// listeIdServices.add(service.getId());
			// }
			// //List<Long> listeIdServices = userForTest.getServices();
			// System.out.println("__________________________________________________________");

			// System.out.println( controller.getUsersBySupervisorServices());

			 //gravityService.addNewGravity(gravity);
			 //statusService.addNewStatus(status);
			// taskService.addTask(task, calendar);
			// supervisorController.addTask(task, calendar,"8c10d257-1c3a-48aa-9952-2f4ae2fa5fb7");
			// taskService.AffecteTaskToUser(taskRepository.findById("20d852b9-ebde-4dea-9e2b-5fd6223eb004").get()
			// ,userRepository.findUserByid("75504a63-610e-4ffe-a0c8-195ade312ebe") ) ;

			Equipe equipe = new Equipe();
			equipe.setNom("equipe1");
			equipe.setDescription("equipe1 for the test only");
			equipe.setStatus(deactivatedFlag.ACTIVE);
		   // adminCtroller.addEquipe(equipe);
		   //adminCtroller.addCordinateur(userCordFortest);
		   
		

		
			

		};
	}

}
