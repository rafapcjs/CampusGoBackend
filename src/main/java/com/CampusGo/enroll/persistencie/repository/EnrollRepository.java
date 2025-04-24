package com.CampusGo.enroll.persistencie.repository;

import com.CampusGo.enroll.persistencie.entity.Enroll;
import com.CampusGo.enroll.presentation.dto.EnrollInfoDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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



}