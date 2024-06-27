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

        
        http.formLogin().loginPage("/login").permitAll();
        http.authorizeHttpRequests().requestMatchers("../reset_password").permitAll();
        
    




        http.authorizeHttpRequests().requestMatchers("/supervisor/**").hasRole("SUP");
        http.authorizeHttpRequests().requestMatchers("/employee/**").hasRole("OP");
        http.authorizeHttpRequests().requestMatchers("/chef_projet/**").hasRole("CHEF_PROJET");
        http.authorizeHttpRequests().requestMatchers("/coordinator/**").hasRole("COR");
        http.authorizeHttpRequests().requestMatchers("/resources/**").permitAll();
        http.authorizeHttpRequests().requestMatchers("/forgot_password").permitAll();









    



     

        http.authorizeHttpRequests().requestMatchers("https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css").permitAll();
        http.authorizeHttpRequests().requestMatchers("https://cdn.jsdelivr.net/npm/sweetalert2@11").permitAll();


        http.authorizeHttpRequests().requestMatchers("https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js").permitAll();
        http.authorizeHttpRequests().requestMatchers("https://cdnjs.cloudflare.com/ajax/libs/jspdf/2.5.1/jspdf.umd.min.js").permitAll();
        http.authorizeHttpRequests().requestMatchers("https://cdnjs.cloudflare.com/ajax/libs/jspdf-autotable/3.5.25/jspdf.plugin.autotable.min.js\"").permitAll();
     





        http.authorizeHttpRequests().requestMatchers("../CSS/style.css").permitAll();
        http.authorizeHttpRequests().requestMatchers("../path/to/datatables.min.js").permitAll();

        http.authorizeHttpRequests().requestMatchers("/images/**").permitAll();
       // http.authorizeHttpRequests().requestMatchers("/**/bootstrap.min.js").permitAll();
       // http.authorizeHttpRequests().requestMatchers("/**/bootstrap.min.css").permitAll();
       http.authorizeHttpRequests().requestMatchers("../CSS/detailtaskstyle.css").permitAll();


        http.authorizeHttpRequests().anyRequest().authenticated();
        http.exceptionHandling().accessDeniedPage("/403");
       
        http.userDetailsService(userDeatailServiceimp);

             

        return http.build();
    }
}
