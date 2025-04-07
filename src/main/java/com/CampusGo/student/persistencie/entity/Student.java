package com.CampusGo.student.persistencie.entity;

import com.CampusGo.commons.entity.BaseEntity;
import com.CampusGo.security.persistence.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

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
}
