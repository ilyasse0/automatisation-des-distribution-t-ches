package com.iben.gestiontaches.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.iben.gestiontaches.entities.User;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserDeatailServiceImpl implements UserDetailsService {


    private AccountService accountService;



    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        // TODO Auto-generated method stub
       User user =  accountService.loadUserBylogin(login);
       if(user == null)  throw new UsernameNotFoundException(String.format("login %s  not found", login));
       String[] roles = user.getRoles().stream().map(u -> u.getName()).toArray(String[]::new);
       UserDetails userDetails = org.springframework.security.core.userdetails.User
       .withUsername(login)
       .password(user.getPassword())
       .roles(roles).build();
       return userDetails;
      }
    
}
