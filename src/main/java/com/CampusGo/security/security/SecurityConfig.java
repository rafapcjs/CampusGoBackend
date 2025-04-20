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

import static com.CampusGo.commons.configs.api.routes.ApiRoutes.*;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtUtils jwtUtils;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, AuthenticationProvider authenticationProvider) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(http -> {
                    // EndPoints publicos
                 http.requestMatchers(HttpMethod.POST,LOGIN).permitAll();
                 http.requestMatchers(HttpMethod.POST, TEACHER_REGISTER).permitAll();
                 http.requestMatchers(HttpMethod.POST,STUDENT_REGISTER).permitAll();
                    http.requestMatchers(
                            "/swagger-ui/**",
                            "/swagger-ui.html",
                            "/v3/api-docs/**",
                            "/v3/api-docs.yaml",
                            "/v3/api-docs.json"
                            ,"/"
                    ).permitAll();
                    // EndPoints Privados

// permisos de student
      http.requestMatchers(HttpMethod.PUT, "/api/v1/campus-go/students/update").hasRole("STUDENT");
      http.requestMatchers(HttpMethod.GET, "/api/v1/campus-go/students/me").hasRole("STUDENT");
      http.requestMatchers(HttpMethod.PUT,STUDENT_CHANGE_PASSWORD ).hasRole("STUDENT");

      // permiso de teeacher
      http.requestMatchers(HttpMethod.PUT, "api/v1/campus-go/teachers/update").hasRole("TEACHER");
      http.requestMatchers(HttpMethod.GET, "api/v1/campus-go/teachers/me").hasRole("TEACHER");
      http.requestMatchers(HttpMethod.PUT,TEACHER_CHANGE_PASSWORD ).hasRole("TEACHER");

      // permisos de teacher en la clase Academic
      http.requestMatchers(HttpMethod.GET,ACADEMIC_LIST ).hasRole("TEACHER");
      http.requestMatchers(HttpMethod.POST,ACADEMIC_REGISTER ).hasRole("TEACHER");
      http.requestMatchers(HttpMethod.PUT,ACADEMIC_UPDATE ).hasRole("TEACHER");

      // permisos de teacher en la clase Schelude
      http.requestMatchers(HttpMethod.POST,SCHELUDE_REGISTER ).hasRole("TEACHER");



      // permisos para materias teacher
                    http.requestMatchers(HttpMethod.GET, SUBJECT_SEARCH_NAME + "/**").hasRole("TEACHER");
                    http.requestMatchers(HttpMethod.GET,SUBJECT_LIST_BY_ORDER_NAME ).hasRole("TEACHER");
                    http.requestMatchers(HttpMethod.GET,SUBJECT_LIST_BY_ORDER_CODE ).hasRole("TEACHER");

                    http.requestMatchers(HttpMethod.POST,SUBJECT_REGISTER ).hasRole("TEACHER");
                    http.requestMatchers(HttpMethod.GET, SUBJECT_SEARCH_NAME + "/**").hasRole("TEACHER");
                    http.requestMatchers(HttpMethod.GET, SUBJECT_SEARCH_CODE + "/**").hasRole("TEACHER");
                    http.requestMatchers(HttpMethod.PUT, SUBJECT_UPDATE + "/**").hasRole("TEACHER");



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
