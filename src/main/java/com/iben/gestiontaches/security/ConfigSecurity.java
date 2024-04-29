package com.iben.gestiontaches.security;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.iben.gestiontaches.services.UserDeatailServiceImpl;

import lombok.AllArgsConstructor;

@EnableWebSecurity
@Configuration
@AllArgsConstructor
public class ConfigSecurity {
    
    private UserDeatailServiceImpl userDeatailServiceimp;


    //@Bean
    public JdbcUserDetailsManager jdbcUserDetailsManager (DataSource dataSource){
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        return jdbcUserDetailsManager;
    }



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.formLogin();
        http.authorizeHttpRequests().requestMatchers("/supervisor/**").hasRole("SUP");
        http.authorizeHttpRequests().requestMatchers("/employee/**").hasRole("OP");
        http.authorizeHttpRequests().requestMatchers("/chef_projet/**").hasRole("CHEF_PROJET");
        http.authorizeHttpRequests().requestMatchers("/coordinator/**").hasRole("COR");


        http.authorizeHttpRequests().requestMatchers("https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css").permitAll();
        http.authorizeHttpRequests().requestMatchers("../css/style.css").permitAll();
        http.authorizeHttpRequests().requestMatchers("/images/**").permitAll();





        http.authorizeHttpRequests().anyRequest().authenticated();
        http.exceptionHandling().accessDeniedPage("/403");
        http.userDetailsService(userDeatailServiceimp);
        return http.build();
    }
}
