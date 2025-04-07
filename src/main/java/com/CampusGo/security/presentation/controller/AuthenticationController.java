package com.CampusGo.security.presentation.controller;

import com.CampusGo.commons.configs.api.routes.ApiRoutes;
import com.CampusGo.security.presentation.payload.AuthCreateUserRequest;
import com.CampusGo.security.presentation.payload.AuthLoginRequest;
import com.CampusGo.security.presentation.dto.AuthResponseDto;
import com.CampusGo.security.service.UserDetailServiceImpl;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.CampusGo.commons.configs.api.routes.ApiRoutes.LOGIN;

@RestController
@Tag(name = "Inicio de session")

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
}