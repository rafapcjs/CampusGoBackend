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
    String STUDENT_CHANGE_PASSWORD = STUDENT_BASE + "/change-password"; // 🔐 Cambiar contraseña estudiante

    // 👨‍🏫 Rutas para profesores
    String TEACHER_BASE = BASE_API + "/teachers";
    String TEACHER_REGISTER = TEACHER_BASE + "/register";
    String TEACHER_CHANGE_PASSWORD = TEACHER_BASE + "/change-password"; // 🔐 Cambiar contraseña profesor

    // 👨‍🏫 Rutas para periodos academicos
    String ACADEMIC_BASE = BASE_API + "/academic";
    String ACADEMIC_LIST = ACADEMIC_BASE + "/getAllAcademic";
    String ACADEMIC_REGISTER = ACADEMIC_BASE + "/registerAcademic";
    String ACADEMIC_UPDATE = ACADEMIC_BASE + "/updateAcademic";

}
