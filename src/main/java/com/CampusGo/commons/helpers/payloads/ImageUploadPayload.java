package com.CampusGo.commons.helpers.payloads;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Builder
public class ImageUploadPayload {

    @NotBlank(message = "La imagen no puede estar nula es obligatoria")

    private String imageUrl;
}
