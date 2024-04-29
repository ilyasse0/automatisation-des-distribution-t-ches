package com.iben.gestiontaches.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.iben.gestiontaches.entities.Message;
import com.iben.gestiontaches.entities.Task;
import com.iben.gestiontaches.entities.User;
import com.iben.gestiontaches.repository.MessageRepository;
import com.iben.gestiontaches.repository.TaskRepository;
import com.iben.gestiontaches.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
@Transactional
public class MessageServiceImplementation implements MessageService {
    private MessageRepository messageRepository;
    private TaskRepository taskRepository;
    private UserRepository userRepository;

    @Override
    public Message addMessageBySup(String message, LocalDate dateCreation, Long task_Id, String user_Id) {
        // TODO Auto-generated method stub
        Task task = taskRepository.findById(task_Id).get();
        User user = userRepository.findById(user_Id).get();

        Message msg = Message.builder()
                .Content(message)
                .date_Creation(dateCreation)
                .task(task)
                .user(user)
                .build();
        System.out.println("Message created");
        return messageRepository.save(msg);

    }

    @Override
    public Message addMessageByOp(Message message) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addMessageByOp'");
    }

    @Override
    public List<Message> fetchMessagesForSup(String user_Id, Long taskId) {
        List<Message> messages = messageRepository.findByTaskId(taskId);
        List<Message> messageSup = new ArrayList<>();
        for (Message message : messages) {
          
            if (message.getUser().equals(user_Id)) {
                messageSup.add(message);
                System.out.println("the secinde test "+messageSup);
            }
        }
        return messageSup;

    }

    @Override
    public List<Message> fetchMessagesForOp(String user_Id, Long taskId) {
        // TODO Auto-generated method stub
        List<Message> messages = messageRepository.findByTaskId(taskId);
        List<Message> messageOp = new ArrayList<>();
        for (Message message : messages) {
            if (!message.getUser().equals(user_Id)) {
                messageOp.add(message);
            }
        }
        return messageOp;
    }

}
