package com.iben.gestiontaches.entities;

import java.util.List;

import com.iben.gestiontaches.enums.statusCode;

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
public class Status {
    @Id  @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private statusCode status;
    private String description;
    private String useConstraint;
    @OneToMany(mappedBy = "status")
    private List<Task> tasks;

}
