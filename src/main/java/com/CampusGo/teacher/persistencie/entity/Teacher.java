package com.CampusGo.teacher.persistencie.entity;

import com.CampusGo.commons.entity.BaseEntity;
import com.CampusGo.security.persistence.entity.UserEntity;
import com.CampusGo.subject.persistencie.entity.Subject;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Teacher  extends BaseEntity {

    private String teacherCode;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id_user")
    private UserEntity user;

    @OneToMany(mappedBy = "teacher")
    private List<Subject> subjects;
}
