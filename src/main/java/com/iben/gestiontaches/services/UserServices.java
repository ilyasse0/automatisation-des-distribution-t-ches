package com.iben.gestiontaches.services;

import java.util.Random;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
@Transactional
public class UserServices {

    

    private JavaMailSender javaMailSender;


    public String generateRandomPassword() {
    // Implement your logic to generate a random password here
    // Example: Generate a random string of alphanumeric characters
    String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    StringBuilder sb = new StringBuilder();
    Random random = new Random();
    for (int i = 0; i < 7; i++) {
        int index = random.nextInt(chars.length());
        sb.append(chars.charAt(index));
    }
    return sb.toString();
}



public void sendNewPasswordByEmail(String toEmail, String newPassword) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom("enhtaskservice@gmail.com");
    message.setTo(toEmail);
    message.setSubject("Your New Password");
    message.setText("Your new password is: " + newPassword);
    message.setText("if youre still facing technical issues, please contact your administrator");
    System.out.println("Email has been sent ");

    javaMailSender.send(message);
}
}
