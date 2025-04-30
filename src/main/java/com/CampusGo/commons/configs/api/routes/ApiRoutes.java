package com.CampusGo.commons.configs.api.routes;


import org.springframework.http.HttpMethod;

public interface ApiRoutes {
    // 🌐 Ruta base de la API versionada
    String BASE_API = "/api/v1/campus-go";

    // 🔐 Rutas de autenticación
    String AUTH_BASE = BASE_API + "/auth";
    String LOGIN = AUTH_BASE + "/log-in";
    String REGISTER = AUTH_BASE + "/register";

    // 👤 Rutas para usuarios
    String USER_BASE = BASE_API + "/users";
    String USER_RECOVER_PASSWORD= USER_BASE + "/recover-password";
    String USER_CHANGE_PASSWORD = USER_BASE + "/change-password"; // 🔐 Cambiar contraseña profesor
    String USER_UPLOAD_IMAGE = USER_BASE + "/uploadImage";

    // 🎓 Rutas para estudiantes
    String STUDENT_BASE = BASE_API + "/students";
     String STUDENT_ME = STUDENT_BASE + "/me";
    String STUDENT_UPDATE = STUDENT_BASE + "/update";


    String STUDENT_REGISTER = STUDENT_BASE + "/register";

    // 👨‍🏫 Rutas para profesores
    String TEACHER_BASE = BASE_API + "/teachers";
    String TEACHER_UPDATE = TEACHER_BASE + "/update";
     String TEACHER_ME = TEACHER_BASE + "/me";
    String TEACHER_REGISTER = TEACHER_BASE + "/register";

    // 👨‍🏫 Rutas para periodos academicos
    String ACADEMIC_BASE = BASE_API + "/academic";
    String ACADEMIC_LIST = ACADEMIC_BASE + "/getAllAcademic";
    String ACADEMIC_REGISTER = ACADEMIC_BASE + "/registerAcademic";
    String ACADEMIC_UPDATE = ACADEMIC_BASE + "/updateAcademic";


    // ruta para subjetct

    String SUBJECT_BASE = BASE_API + "/subjects";
    String SUBJECT_SEARCH_CODE= SUBJECT_BASE + "/searchCodeSubject";
    String SUBJECT_REGISTER = SUBJECT_BASE + "/registerSubject";
    String SUBJECT_SEARCH_NAME= SUBJECT_BASE + "/searchNameSubject";
    String SUBJECT_UPDATE = SUBJECT_BASE + "/updateSubject";
    String SUBJECT_LIST_BY_ORDER_CODE = ACADEMIC_BASE + "/getAllOrderCodeSubject";
    String SUBJECT_LIST_BY_ORDER_NAME = ACADEMIC_BASE + "/getAllOrderNameSubject";
    String SUBJECT_MY_TEACHER = BASE_API + "/teacherWithHisSubject";
    String SUBJECT_MY_STUDENT = BASE_API + "/studentWithHisSubject";


    // ruta para schelude

    String SCHELUDE_BASE = BASE_API + "/schelude";
    String SCHELUDE_REGISTER = SCHELUDE_BASE + "/registerSchelude";
    String SCHELUDE_LIST_ORDER = SCHELUDE_BASE + "/listScheludeOrderAsc";
    String SCHELUDE_LIST_BY_STUDENT  =SCHELUDE_BASE + "/listByCodeStudent";
    String SCHELUDE_UPDATE = SCHELUDE_BASE + "/updateScheludeByCode";
    String SCHELUDE_DELETE = SCHELUDE_BASE + "/deleteSchelude";



    // ruta para schelude
    String ENROLL_BASE = BASE_API + "/enroll";
    String ENROLL_REGISTER = ENROLL_BASE + "/registerEnroll";
    String ENROLL_MULTI_REGISTER = ENROLL_BASE + "/registerMultiEnroll";
    String ENROLL_DELETE = ENROLL_BASE + "/deleteByCodeEnroll";
    String ENROLL_LIST_ALL = ENROLL_BASE + "/listAll";
    String ENROLL_LIST_BY_ID_STUDENT = ENROLL_BASE + "/listAllByCodeIdStudent";
    String ENROLL_LIST_BY_CODE_ASIGNATURE = ENROLL_BASE + "/listAllByCodeAsignature";
    String ENROLL_LIST_BY_DATE = ENROLL_BASE + "/listAllByDate";

    // ruta para grade


    String GRADE_BASE = BASE_API + "/grade";
    String GRADE_REGISTER = GRADE_BASE + "/registerGrade";
    String GRADE_LIST_BY_SUBJECT = GRADE_BASE + "/grades-by-subject";
    String GRADE_MY_GRADES   = GRADE_BASE + "/my-grades";
    String GRADE_LIST_BY_SUBJECT_TEACHER   = GRADE_BASE + "/my-gradesTheMyStudent";


}

