package com.CampusGo.enroll.persistencie.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ENROLL")
public class Enroll {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(unique = true, nullable = false)
    private Integer code;

    @Column(nullable = false)
    private LocalDateTime fechaRegistra;

    @Column(nullable = false)
    private Integer codEstudianteFk;

    @Column(nullable = false)
    private Integer codAsignatureFk;
}
