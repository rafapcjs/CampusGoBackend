package com.CampusGo.enroll.persistencie.repository;

import com.CampusGo.enroll.persistencie.entity.Enroll;
import com.CampusGo.enroll.presentation.dto.EnrollInfoDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollRepository extends JpaRepository<Enroll, Integer> {
    Optional<Enroll> findTopByOrderByCodeDesc();

    boolean existsByStudentIdAndSubjectCode(Long studentId, Integer subjectCode);

    Optional<Enroll> findByCode(Integer code);




    @Query(value = """
    SELECT
        e.code,
        e.cod_asignature_fk,
        UPPER(s.name),
        e.cod_estudiante_fk,
        UPPER(u.name || ' ' || u.last_name),
        e.fecha_registra
    FROM
        enroll e
    JOIN
        subject s ON e.cod_asignature_fk = s.code
    JOIN
        users u ON e.cod_estudiante_fk = u.id
    ORDER BY
        e.code ASC
    """, nativeQuery = true)
    List<Object[]> findAllEnrollInfoRaw();




    @Query(value = """
    SELECT
        e.code AS code,
        e.cod_asignature_fk AS codAsignatureFk,
        UPPER(s.name) AS nameAsignature,
        e.cod_estudiante_fk AS codEstudianteFk,
        UPPER(u.name || ' ' || u.last_name) AS fullName,
        e.fecha_registra AS fechaRegistra
    FROM
        enroll e
    JOIN
        subject s ON e.cod_asignature_fk = s.code
    JOIN
        users u ON e.cod_estudiante_fk = u.id
    WHERE
        CAST(e.cod_estudiante_fk AS TEXT) LIKE %:studentId%
    ORDER BY
        e.code ASC
    """, nativeQuery = true)
    List<Object[]> findAllEnrollInfoByStudentId(@Param("studentId") String studentId);




    @Query(value = """
    SELECT
        e.code,
        e.cod_asignature_fk,
        UPPER(s.name) AS nameAsignature,
        e.cod_estudiante_fk,
        UPPER(u.name || ' ' || u.last_name) AS fullName,
        e.fecha_registra
    FROM
        enroll e
    JOIN
        subject s ON e.cod_asignature_fk = s.code
    JOIN
        users u ON e.cod_estudiante_fk = u.id
    WHERE
        CAST(e.cod_asignature_fk AS TEXT) LIKE %:subjectCode%
    ORDER BY
        e.code ASC
    """, nativeQuery = true)
    List<Object[]> getAllEnrollInfoBySubjectCode(@Param("subjectCode") String subjectCode);




    @Query(value = """
    SELECT
        e.code,
        e.cod_asignature_fk,
        UPPER(s.name) AS nameAsignature,
        e.cod_estudiante_fk,
        UPPER(u.name || ' ' || u.last_name) AS fullName,
        e.fecha_registra
    FROM
        enroll e
    JOIN
        subject s ON e.cod_asignature_fk = s.code
    JOIN
        users u ON e.cod_estudiante_fk = u.id
    WHERE
        TO_CHAR(e.fecha_registra, 'DD/MM/YYYY') = :registerDate
    ORDER BY
        e.code ASC
""", nativeQuery = true)
    List<Object[]> findAllEnrollInfoByRegisterDate(@Param("registerDate") String registerDate);


}