package com.CampusGo.security.presentation.controller;

import com.CampusGo.commons.configs.api.routes.ApiRoutes;
import com.CampusGo.commons.helpers.payloads.ChangePasswordPayload;
import com.CampusGo.security.presentation.payload.AuthCreateUserRequest;
import com.CampusGo.security.presentation.payload.AuthLoginRequest;
import com.CampusGo.security.presentation.dto.AuthResponseDto;
import com.CampusGo.security.service.UserDetailServiceImpl;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.CampusGo.commons.configs.api.routes.ApiRoutes.*;

@RestController
@Tag(name = " Autenticación y Administración de Cuentas ")

public class AuthenticationController {

    @Autowired
    private UserDetailServiceImpl userDetailService;

    @Hidden
    @PostMapping("/sign-up")
    public ResponseEntity<AuthResponseDto> register(@RequestBody @Valid AuthCreateUserRequest userRequest) {
        return new ResponseEntity<>(this.userDetailService.createUser(userRequest), HttpStatus.CREATED);
    }

    @PostMapping(LOGIN)
    @Operation(
            summary = "Iniciar sesión",
            description = "Permite a un usuario autenticarse en la plataforma mediante credenciales válidas. Devuelve un token JWT si las credenciales son correctas.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Inicio de sesión exitoso. Devuelve el token JWT.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AuthResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Solicitud inválida (credenciales mal formadas)",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "No autorizado. Credenciales incorrectas.",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<AuthResponseDto> login(@RequestBody @Valid AuthLoginRequest userRequest) {
        return new ResponseEntity<>(this.userDetailService.loginUser(userRequest), HttpStatus.OK);
    }
    @PostMapping(USER_RECOVER_PASSWORD)
    @Operation(
            summary = "Recuperar contraseña",
            description = "Recupera la contraseña de un usuario enviando una nueva contraseña temporal a su correo.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Se ha enviado una nueva contraseña temporal al correo."),
                    @ApiResponse(responseCode = "404", description = "No se encontró el usuario con el correo proporcionado.")
            }
    )
    public ResponseEntity<String> recoverPassword(@RequestParam String email) {
        userDetailService.recoverPassword(email);
        return ResponseEntity.ok("Se ha enviado una nueva contraseña temporal a tu correo.");
    }


    @Operation(summary = "Actualizar logo del imagen de usuario autenticado")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Logo actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Archivo inválido"),
            @ApiResponse(responseCode = "404", description = "Profesor no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    @PutMapping(
            value    = USER_UPLOAD_IMAGE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Void> uploadLogo(
            @Parameter(
                    description = "Imagen a subir (JPEG/PNG)",
                    required    = true,
                    content     = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema    = @Schema(type = "string", format = "binary")
                    )
            )
            @RequestPart("file") MultipartFile file
    ) {
        userDetailService.uploadImageLogo(file);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Actualizar contraseña de usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Contraseña actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @ApiResponse(responseCode = "404", description = "Profesor no encontrado")
    })
    @PutMapping(USER_CHANGE_PASSWORD)
    public ResponseEntity<String> updatePassword(@Valid @RequestBody ChangePasswordPayload payload) {
        userDetailService.updatePassword(payload);
        return ResponseEntity.noContent().build();
    }
}