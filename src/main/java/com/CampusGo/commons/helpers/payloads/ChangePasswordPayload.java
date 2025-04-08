package com.CampusGo.commons.helpers.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Builder
public class ChangePasswordPayload {
    @NotBlank(message = "La contraseña actual es obligatoria")
    private String oldPassword;

    /** Nueva contraseña deseada */
    @NotBlank(message = "La nueva contraseña es obligatoria")
    @Size(min = 8, max = 100, message = "La nueva contraseña debe tener entre 8 y 100 caracteres")
    private String newPassword;

    /** Confirmación de la nueva contraseña */
    @NotBlank(message = "La confirmación de la nueva contraseña es obligatoria")
    private String confirmNewPassword;

}
