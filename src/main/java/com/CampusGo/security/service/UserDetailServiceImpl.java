package com.CampusGo.security.service;

import com.CampusGo.commons.configs.error.exceptions.AccessDeniedException;
import com.CampusGo.commons.configs.error.exceptions.ConflictException;
import com.CampusGo.commons.configs.error.exceptions.ResourceNotFoundException;
import com.CampusGo.commons.configs.utils.cloudinary.CloudinaryService;
import com.CampusGo.commons.configs.utils.emails.IEmailService;
import com.CampusGo.commons.helpers.payloads.ChangePasswordPayload;
import com.CampusGo.security.presentation.payload.AuthCreateUserRequest;
import com.CampusGo.security.presentation.payload.AuthLoginRequest;
import com.CampusGo.security.presentation.dto.AuthResponseDto;
import com.CampusGo.security.persistence.entity.RoleEntity;
import com.CampusGo.security.persistence.entity.UserEntity;
import com.CampusGo.security.persistence.repository.RoleRepository;
import com.CampusGo.security.persistence.repository.UserRepository;
import com.CampusGo.security.util.JwtUtils;
import com.CampusGo.security.util.SecurityUtils;
import com.CampusGo.teacher.persistencie.entity.Teacher;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
 public class UserDetailServiceImpl implements UserDetailsService {


    private JwtUtils jwtUtils;

    private PasswordEncoder passwordEncoder;


    private UserRepository userRepository;
private  final CloudinaryService cloudinaryService;

    private RoleRepository roleRepository;
    private final IEmailService iEmailService;
    public UserDetailServiceImpl(JwtUtils jwtUtils,
                                 PasswordEncoder passwordEncoder,
                                 UserRepository userRepository,
                                 RoleRepository roleRepository,
                                 IEmailService iEmailService
    , CloudinaryService cloudinaryService) {
        this.jwtUtils        = jwtUtils;
        this.passwordEncoder = passwordEncoder;
        this.userRepository  = userRepository;
        this.roleRepository  = roleRepository;
        this.iEmailService   = iEmailService;
    this.cloudinaryService = cloudinaryService;
    }


    @Override
    public UserDetails loadUserByUsername(String username) {

        UserEntity userEntity = userRepository.findUserEntityByUsername(username).orElseThrow(() -> new UsernameNotFoundException("El usuario " + username + " no existe."));

        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        userEntity.getRoles().forEach(role -> authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name()))));


        return new User(userEntity.getUsername(), userEntity.getPassword(), userEntity.isEnabled(), userEntity.isAccountNoExpired(), userEntity.isCredentialNoExpired(), userEntity.isAccountNoLocked(), authorityList);
    }

    public AuthResponseDto createUser(AuthCreateUserRequest createRoleRequest) {

        String username = createRoleRequest.username();
        String password = createRoleRequest.password();
        List<String> rolesRequest = createRoleRequest.roleRequest().roleListName();

        Set<RoleEntity> roleEntityList = roleRepository.findRoleEntitiesByRoleEnumIn(rolesRequest).stream().collect(Collectors.toSet());

        if (roleEntityList.isEmpty()) {
            throw new IllegalArgumentException("The roles specified does not exist.");
        }

        UserEntity userEntity = UserEntity.builder().username(username).password(passwordEncoder.encode(password)).roles(roleEntityList).isEnabled(true).accountNoLocked(true).accountNoExpired(true).credentialNoExpired(true).build();

        UserEntity userSaved = userRepository.save(userEntity);

        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();

        userSaved.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleEnum().name()))));


        SecurityContext securityContextHolder = SecurityContextHolder.getContext();
        Authentication authentication = new UsernamePasswordAuthenticationToken(userSaved, null, authorities);

        String accessToken = jwtUtils.createToken(authentication);

        AuthResponseDto authResponse = new AuthResponseDto(username, "User created successfully", accessToken, true);
        return authResponse;
    }

    public AuthResponseDto loginUser(AuthLoginRequest authLoginRequest) {

        String username = authLoginRequest.username();
        String password = authLoginRequest.password();

        Authentication authentication = this.authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtUtils.createToken(authentication);
        AuthResponseDto authResponse = new AuthResponseDto(username, "User loged succesfully", accessToken, true);
        return authResponse;
    }

    public Authentication authenticate(String username, String password) {
        UserDetails userDetails = this.loadUserByUsername(username);

        if (userDetails == null) {
            throw new BadCredentialsException(String.format("Invalid username or password"));
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Incorrect Password");
        }

        return new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
    }

    public void recoverPassword(String email) {
        UserEntity userEntity = userRepository.findUserEntityByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "El usuario con el correo " + email + " no existe"));

        String newPassword = UUID.randomUUID().toString().substring(0, 10);
        userEntity.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(userEntity);

        String subject = "Recuperación de contraseña - Campus Go";

        String message = """
                <!DOCTYPE html>
                <html lang="es">
                <head>
                  <meta charset="UTF-8">
                  <title>Recuperación de Contraseña</title>
                </head>
                <body style="margin:0;padding:0;background-color:#f4f6f8;
                             font-family:Arial,sans-serif;color:#333;">
                  <table width="100%%" cellpadding="0" cellspacing="0" role="presentation">
                    <tr>
                      <td align="center">
                        <table width="600" cellpadding="0" cellspacing="0" role="presentation"
                               style="margin:40px 0;background:#ffffff;border-radius:8px;
                                      overflow:hidden;box-shadow:0 2px 8px rgba(0,0,0,0.1);">
                
                          <!-- Header con logo de Campus Go -->
                          <tr>
                            <td style="background:#004080;padding:20px;text-align:center;">
                              <img src="https://res.cloudinary.com/diaxo8ovb/image/upload/v1745967630/ChatGPT_Image_29_abr_2025_17_59_49_rl2wlo.png"
                                   alt="Campus Go" width="150" style="display:block;margin:0 auto;">
                            </td>
                          </tr>
                
                          <!-- Body -->
                          <tr>
                            <td style="padding:30px;">
                              <h2 style="margin-top:0;color:#004080;">
                                Hola %s,
                              </h2>
                              <p style="line-height:1.6;">
                                Hemos recibido una solicitud para restablecer la contraseña de tu cuenta en Campus Go.
                                Usa el siguiente código de verificación:
                              </p>
                
                              <!-- Código destacado -->
                              <div style="text-align:center;margin:30px 0;">
                                <span style="display:inline-block;padding:15px 30px;font-size:24px;
                                             letter-spacing:4px;font-weight:bold;color:#004080;
                                             background:#e6f0ff;border-radius:4px;">
                                  %s
                                </span>
                              </div>
                
                              <p style="line-height:1.6;">
                                Este código expirará en 15 minutos. Si no solicitaste este cambio,
                                ignora este correo o contacta a soporte.
                              </p>
                
                              <p style="line-height:1.6;">
                                Gracias por usar Campus Go,<br>
                                <strong>Equipo de Soporte Campus Go</strong>
                              </p>
                            </td>
                          </tr>
                
                          <!-- Footer -->
                          <tr>
                            <td style="background:#f0f3f6;padding:20px;text-align:center;
                                       font-size:12px;color:#777;">
                              <p style="margin:0 0 8px 0;">
                                © 2025 Campus Go. Todos los derechos reservados.
                              </p>
                              <p style="margin:0;">
                                <a href="https://campusgo.com/terminos"
                                   style="color:#004080;text-decoration:none;">
                                  Términos y Condiciones
                                </a> |
                                <a href="https://campusgo.com/privacidad"
                                   style="color:#004080;text-decoration:none;">
                                  Política de Privacidad
                                </a>
                              </p>
                            </td>
                          </tr>
                
                        </table>
                      </td>
                    </tr>
                  </table>
                </body>
                </html>
                """.formatted(
                userEntity.getName(),
                newPassword
        );

        iEmailService.sendEmail(new String[]{email}, subject, message);
    }


    @Transactional
    public void uploadImageLogo(MultipartFile file) {
        // 1. Usuario actual
        String username = SecurityUtils.getCurrentUsername();
        UserEntity user = userRepository
                .findUserEntityByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        // 2. Docente asociado

        try {
            // 3. Lógica encapsulada en CloudinaryService
            String secureUrl = cloudinaryService.upload(file, "logos_teachers");

            // 4. Actualizar entidad y guardar
            user.setImageUrl(secureUrl);
            userRepository.save(user);


        } catch (IOException e) {
            throw new RuntimeException("Error al procesar la imagen de logo", e);
        }
    }


    public void updatePassword(ChangePasswordPayload payload) {
        String currentUsername = SecurityUtils.getCurrentUsername();

        // Buscar el usuario autenticado
        UserEntity user = userRepository.findUserEntityByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado."));



        // Validar contraseña actual
        if (!passwordEncoder.matches(payload.getOldPassword(), user.getPassword())) {
            throw new AccessDeniedException("La contraseña actual es incorrecta.");
        }

        // Validar que la nueva contraseña y la confirmación coincidan
        if (!payload.getNewPassword().equals(payload.getConfirmNewPassword())) {
            throw new ConflictException("La nueva contraseña y su confirmación no coinciden.");
        }

        // Validar que la nueva sea diferente
        if (passwordEncoder.matches(payload.getNewPassword(), user.getPassword())) {
            throw new ConflictException("La nueva contraseña no puede ser igual a la actual.");
        }

        // Cambiar contraseña
        user.setPassword(passwordEncoder.encode(payload.getNewPassword()));
        userRepository.save(user);
    }


}