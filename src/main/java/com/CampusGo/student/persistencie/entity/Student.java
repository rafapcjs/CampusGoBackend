package com.CampusGo.student.persistencie.entity;

import com.CampusGo.commons.entity.BaseEntity;
import com.CampusGo.enroll.persistencie.entity.Enroll;
import com.CampusGo.grade.persistencie.entity.Grade;
import com.CampusGo.security.persistence.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Getter
@Setter
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Student  extends BaseEntity {

    private String studentCode;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id_user")
    private UserEntity user;

    @OneToMany(mappedBy = "student")
    private List<Grade> grades;

    @OneToMany(mappedBy = "student")
    private List<Enroll> enrolls;
}
