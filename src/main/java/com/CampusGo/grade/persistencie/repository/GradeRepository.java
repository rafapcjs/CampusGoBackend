package com.CampusGo.grade.persistencie.repository;

import com.CampusGo.grade.persistencie.entity.Grade;
import com.CampusGo.grade.presentation.dto.StudentSubjectGradeResponseDto;
import com.CampusGo.grade.presentation.dto.SubjectGradeResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Integer> {
    Optional<Grade> findTopByOrderByCodeDesc();

    Optional<Grade> findByCode(Integer code);

    Optional<Grade> findByStudentIdAndSubjectCode(Long studentId, Integer subjectCode);


    @Query("SELECT g FROM Grade g "
            + "JOIN FETCH g.student s "
            + "JOIN FETCH g.subject  sub")
    List<Grade> findAllWithStudentAndSubject();

    @Query(value = """
        SELECT
          sub.name     AS subject_name,
          g.corte1,
          g.corte2,
          g.corte3,
          g.corte4
        FROM grade g
        JOIN student s 
          ON g.cod_estudiante_fk = s.id_user
        JOIN users u    
          ON s.id_user            = u.id
        JOIN subject sub 
          ON g.cod_asignature_fk  = sub.code
        WHERE u.username = :username
        ORDER BY sub.name
        """,
            nativeQuery = true
    )
    List<SubjectGradeResponse> findGradesByUsername(@Param("username") String username);

    @Query(value = """
        SELECT
          stu.student_code                                     AS studentCode,
          CONCAT(u_s.name, ' ', u_s.last_name)                 AS studentName,
          g.corte1                                             AS corte1,
          g.corte2                                             AS corte2,
          g.corte3                                             AS corte3,
          g.corte4                                             AS corte4
        FROM grade    AS g
        INNER JOIN student AS stu  ON g.cod_estudiante_fk = stu.id_user
        INNER JOIN users   AS u_s  ON stu.id_user           = u_s.id
        INNER JOIN subject AS sub  ON g.cod_asignature_fk  = sub.code
        INNER JOIN teacher AS prof ON prof.id              = sub.code_profe_asignado
        INNER JOIN users   AS u_t  ON u_t.id                = prof.id_user
        WHERE sub.code        = :subjectCode
          AND u_t.username    = :teacherUsername
        ORDER BY stu.student_code ASC
        """,
            nativeQuery = true
    )
    List<StudentSubjectGradeResponseDto> findGradesBySubjectAndTeacher(
            @Param("subjectCode")     Integer subjectCode,
            @Param("teacherUsername") String teacherUsername
    );
}
