package com.iben.gestiontaches.services;

import java.time.LocalDate;
import java.util.List;

import com.iben.gestiontaches.entities.Message;
import com.iben.gestiontaches.entities.Task;
import com.iben.gestiontaches.entities.User;

public interface MessageService {
    Message addMessageBySup(String message, LocalDate dateCreation, Long task_Id, String user_Id);
    Message addMessageByOp(Message message);

    public List<Message> fetchMessagesForSup(String user_Id , Long taskId);
    public List<Message> fetchMessagesForOp(String user_Id , Long taskId);


    public boolean isSupervisor(String userId);

}
