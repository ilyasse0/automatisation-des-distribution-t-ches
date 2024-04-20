package com.iben.gestiontaches.entities;

import java.time.LocalDateTime;

import groovy.transform.builder.Builder;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor 
@NoArgsConstructor
@Builder 
@Data 
@Entity
public class UserTaskAssignment  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Fields for storing user IDs
    @Column(name = "cor_user_id")
    private Long corUserId;

    @Column(name = "sup_user_id")
    private Long supUserId;

    @Column(name = "op_user_id")
    private Long opUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;

    private LocalDateTime dateLastModification;

    // Constructors, getters, and setters omitted for brevity
}
