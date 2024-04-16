package com.iben.gestiontaches.entities;

import java.util.List;

import com.iben.gestiontaches.enums.Priority;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Gravity {
    @Id  @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Priority priority ;
    private String description;
    
    @OneToMany(mappedBy = "gravity")
    private List<Task> tasks;
}
