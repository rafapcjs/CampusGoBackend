package com.CampusGo.student.presentation.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Builder
public class StudentPayload {


        @NotBlank(message = "El nombre de usuario es obligatorio")
        @Size(min = 4, max = 20, message = "El nombre de usuario debe tener entre 4 y 20 caracteres")
        private String username;

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
        private String password;

        @NotBlank(message = "El DNI es obligatorio")
        @Pattern(regexp = "\\d{6,10}", message = "El DNI debe tener entre 6 y 10 dígitos numéricos")
        private String dni;

        @NotBlank(message = "El correo electrónico es obligatorio")
        @Email(message = "Debe proporcionar un correo electrónico válido")
        private String email;

        @NotBlank(message = "El teléfono es obligatorio")
        @Pattern(regexp = "\\d{7,15}", message = "El teléfono debe tener entre 7 y 15 dígitos")
        private String phone;

        @NotBlank(message = "El código del estudiante es obligatorio")
    @Size(min = 5, max = 12, message = "El código del estudiante debe tener entre 5 y 12 caracteres")
    private String studentCode;


}
