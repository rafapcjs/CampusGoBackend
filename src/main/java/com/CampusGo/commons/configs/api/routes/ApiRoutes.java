package com.CampusGo.commons.configs.api.routes;


public interface ApiRoutes {
    // 🌐 Ruta base de la API versionada
    String BASE_API = "/api/v1/campus-go";

    // 🔐 Rutas de autenticación
    String AUTH_BASE = BASE_API + "/auth";
    String LOGIN = AUTH_BASE + "/log-in";
    String REGISTER = AUTH_BASE + "/register";

    // 👤 Rutas para usuarios
    String USER_BASE = BASE_API + "/users";

    // 🎓 Rutas para estudiantes
    String STUDENT_BASE = BASE_API + "/students";
    String STUDENT_REGISTER = STUDENT_BASE + "/register";

    // 👨‍🏫 Rutas para profesores
    String TEACHER_BASE = BASE_API + "/teachers";
    String TEACHER_REGISTER = TEACHER_BASE + "/register";
}
