package com.iben.gestiontaches.entities;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
public class Task {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String title;
    private String description;
    @Temporal(TemporalType.DATE)
    private LocalDate date_Creation;
    @OneToOne(fetch = FetchType.EAGER)
    private Calendar calendar;

    @ManyToOne(fetch = FetchType.EAGER)
    private Gravity gravity;

    @ManyToOne(fetch = FetchType.EAGER)
    private Status status;


@OneToMany(mappedBy = "task", fetch = FetchType.LAZY)
    private List<UserTaskAssignment> userAssignments;
}
