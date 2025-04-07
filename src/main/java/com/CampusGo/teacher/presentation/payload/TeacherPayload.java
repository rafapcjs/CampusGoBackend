package com.CampusGo.teacher.presentation.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class TeacherPayload {

        @NotBlank(message = "El nombre de usuario no puede estar vacío")
        private String username;

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
        private String password;

        @NotBlank(message = "El DNI es obligatorio")
        @Pattern(regexp = "\\d{9}", message = "El DNI debe tener 9 dígitos")
        private String dni;

        @NotBlank(message = "El correo electrónico es obligatorio")
        private String email;

        @NotBlank(message = "El teléfono es obligatorio")
        @Pattern(regexp = "\\d{7,15}", message = "El teléfono debe tener entre 7 y 15 dígitos")
        private String phone;

        @NotBlank(message = "El código del profesor es obligatorio")
        private String teacherCode;
}
