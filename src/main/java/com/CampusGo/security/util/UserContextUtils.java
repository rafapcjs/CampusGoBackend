package com.CampusGo.security.util;

import com.CampusGo.commons.configs.error.exceptions.ResourceNotFoundException;
import com.CampusGo.security.persistence.entity.UserEntity;
import com.CampusGo.security.persistence.repository.UserRepository;
import com.CampusGo.student.persistencie.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserContextUtils {

    @Autowired
    private UserRepository userRepository;

    public Student getCurrentStudent() {
        String username = SecurityUtils.getCurrentUsername();
        UserEntity user = userRepository.findUserEntityByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Student student = user.getStudent();
        if (student == null) {
            throw new ResourceNotFoundException("Este usuario no está asociado a un estudiante");
        }

        return student;
    }

}
