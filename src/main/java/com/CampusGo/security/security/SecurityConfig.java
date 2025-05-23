package com.CampusGo.security.security;

import com.CampusGo.commons.configs.api.routes.ApiRoutes;
import com.CampusGo.security.security.filter.JwtTokenValidator;
import com.CampusGo.security.service.UserDetailServiceImpl;
import com.CampusGo.security.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import static com.CampusGo.commons.configs.api.routes.ApiRoutes.*;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtUtils jwtUtils;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, AuthenticationProvider authenticationProvider, CorsConfigurationSource corsConfigurationSource) throws Exception {
        return httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource))

                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(http -> {
                    // EndPoints publicos
                 http.requestMatchers(HttpMethod.POST,LOGIN).permitAll();
                 http.requestMatchers(HttpMethod.POST, TEACHER_REGISTER).permitAll();
                 http.requestMatchers(HttpMethod.POST,STUDENT_REGISTER).permitAll();
                    http
                            .requestMatchers(HttpMethod.POST, USER_RECOVER_PASSWORD)
                            .permitAll();
                    http.requestMatchers(
                            "/swagger-ui/**",
                            "/swagger-ui.html",
                            "/v3/api-docs/**",
                            "/v3/api-docs.yaml",
                            "/v3/api-docs.json"
                            ,"/"
                    ).permitAll();
                    // EndPoints Privados
// metodos comunes

                    http.requestMatchers(HttpMethod.PUT, USER_UPLOAD_IMAGE)
                            .hasAnyRole("TEACHER", "STUDENT");
                    http.requestMatchers(HttpMethod.PUT, USER_CHANGE_PASSWORD)
                            .hasAnyRole("TEACHER", "STUDENT");
// permisos de student
// permisos de student
      http.requestMatchers(HttpMethod.PUT, STUDENT_UPDATE).hasRole("STUDENT");
      http.requestMatchers(HttpMethod.GET, STUDENT_ME).hasRole("STUDENT");


      // permisos de student en scheluder
      http.requestMatchers(HttpMethod.GET,SCHELUDE_LIST_BY_STUDENT ).hasRole("STUDENT");


      // permiso de teeacher
      http.requestMatchers(HttpMethod.PUT, TEACHER_UPDATE).hasRole("TEACHER");

      http.requestMatchers(HttpMethod.GET, TEACHER_ME).hasRole("TEACHER");

      // permisos de teacher en la clase Academic
      http.requestMatchers(HttpMethod.GET,ACADEMIC_LIST ).hasRole("TEACHER");
      http.requestMatchers(HttpMethod.POST,ACADEMIC_REGISTER ).hasRole("TEACHER");
      http.requestMatchers(HttpMethod.PUT,ACADEMIC_UPDATE ).hasRole("TEACHER");

      // permisos de teacher en la clase Schelude
      http.requestMatchers(HttpMethod.POST,SCHELUDE_REGISTER ).hasRole("TEACHER");
      http.requestMatchers(HttpMethod.GET,SCHELUDE_LIST_ORDER ).hasRole("TEACHER");
      http.requestMatchers(HttpMethod.PUT,SCHELUDE_UPDATE ).hasRole("TEACHER");
      http.requestMatchers(HttpMethod.DELETE,SCHELUDE_DELETE ).hasRole("TEACHER");

      // permisos de teacher en la clase Enroll
      http.requestMatchers(HttpMethod.POST,ENROLL_REGISTER ).hasRole("TEACHER");
      http.requestMatchers(HttpMethod.POST,ENROLL_MULTI_REGISTER ).hasRole("TEACHER");
      http.requestMatchers(HttpMethod.DELETE,ENROLL_DELETE ).hasRole("TEACHER");
      http.requestMatchers(HttpMethod.GET,ENROLL_LIST_ALL ).hasRole("TEACHER");
      http.requestMatchers(HttpMethod.GET,ENROLL_LIST_BY_ID_STUDENT ).hasRole("TEACHER");
      http.requestMatchers(HttpMethod.GET,ENROLL_LIST_BY_CODE_ASIGNATURE ).hasRole("TEACHER");
      http.requestMatchers(HttpMethod.GET,ENROLL_LIST_BY_DATE ).hasRole("TEACHER");


      // permisos para materias teacher
                    http.requestMatchers(HttpMethod.GET, SUBJECT_SEARCH_NAME + "/**").hasRole("TEACHER");
                    http.requestMatchers(HttpMethod.GET,SUBJECT_LIST_BY_ORDER_NAME ).hasRole("TEACHER");
                    http.requestMatchers(HttpMethod.GET,SUBJECT_LIST_BY_ORDER_CODE ).hasRole("TEACHER");
                    http.requestMatchers(HttpMethod.GET,SUBJECT_MY_TEACHER ).hasRole("TEACHER");
                    http.requestMatchers(HttpMethod.POST,SUBJECT_REGISTER ).hasRole("TEACHER");
                    http.requestMatchers(HttpMethod.GET, SUBJECT_SEARCH_NAME + "/**").hasRole("TEACHER");
                    http.requestMatchers(HttpMethod.GET, SUBJECT_SEARCH_CODE + "/**").hasRole("TEACHER");
                    http.requestMatchers(HttpMethod.PUT, SUBJECT_UPDATE + "/**").hasRole("TEACHER");

// permisos de subject para student
                    http.requestMatchers(HttpMethod.GET, SUBJECT_MY_STUDENT)
                            .hasRole("STUDENT");

 // permiso de nota para profesor
                    http   .requestMatchers(HttpMethod.GET,  GRADE_LIST_BY_SUBJECT_TEACHER + "/++")
                            .hasRole("TEACHER");

                    http  .requestMatchers(HttpMethod.PUT,  GRADE_REGISTER                + "/++")
                            .hasRole("TEACHER");

               // permiso de student en notas
                    // permiso para student sobre notas
                    http .requestMatchers(HttpMethod.GET, GRADE_MY_GRADES)
                            .hasRole("STUDENT");


                    http.anyRequest().denyAll();
                })
                .addFilterBefore(new JwtTokenValidator(jwtUtils), BasicAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailServiceImpl userDetailService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailService);
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
