package com.iben.gestiontaches.entities;

import java.util.List;

import org.springframework.boot.autoconfigure.web.WebProperties.Resources.Chain.Strategy;

import com.iben.gestiontaches.enums.deactivatedFlag;

import io.micrometer.common.lang.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity 
@AllArgsConstructor
@NoArgsConstructor 
@Data  
@Builder
public class User {
    @Id  
    private String id;
    @NotBlank
    private String  firstName;
    @NotBlank
    private String lastName;
    @Pattern(regexp = "^(male|female)$", message = "Sex must be either 'male' or 'female'")
    private String sex;
    @NotBlank
    private String phoneNumber;
    @Email
    private String email;
    @NotBlank 
    private String login;
    @NotBlank
    private String password;
    //@NotNull
    private deactivatedFlag status;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Service> services;

    @Transient
    @Nullable // For Spring Data validation
    private String confirmPassword;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<UserTaskAssignment> taskAssignments;

}
