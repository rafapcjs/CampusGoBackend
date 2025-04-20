package com.CampusGo.schelude.persistencie.repository;

import com.CampusGo.schelude.persistencie.entity.Schelude;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheludeRepository extends JpaRepository<Schelude, Integer> {

    // Spring puede generar esta automáticamente, pero puedes dejar el @Query si lo prefieres
    @Query("SELECT s FROM Schelude s WHERE s.codeAsignatureFk = :codeAsignatureFk")
    List<Schelude> findByCodeAsignatureFk(Integer codeAsignatureFk);

    @Query("SELECT MAX(s.code) FROM Schelude s")
    Integer findMaxCode();

    // Query personalizada para listar los horarios por dia de forma general
    @Query(value = """
        SELECT
            scl.code,
            s2.code AS codeSubject,
            s2.name,
            CASE 
                WHEN scl.dia = 0 THEN 'Domingo'
                WHEN scl.dia = 1 THEN 'Lunes'
                WHEN scl.dia = 2 THEN 'Martes'
                WHEN scl.dia = 3 THEN 'Miércoles'
                WHEN scl.dia = 4 THEN 'Jueves'
                WHEN scl.dia = 5 THEN 'Viernes'
                WHEN scl.dia = 6 THEN 'Sábado'
            END AS dia,
            scl.hora_inicial,
            scl.hora_final,
            UPPER(u.name || ' ' || u.last_name) AS nameTeacher
        FROM
            schelude scl
        JOIN subject s2 ON scl.code_asignature_fk = s2.code
        JOIN enroll erl ON erl.cod_asignature_fk = s2.code
        JOIN users u ON s2.code_profe_asignado = u.id
        ORDER BY scl.dia ASC
        """, nativeQuery = true)
    List<Object[]> findAllOrderedSchedulesRaw();


    // Query personalizada para listar los horarios de un estudiante por su ID

    @Query(value = """
    SELECT
        scl.code AS code,
        s2.code AS codeSubject,
        s2.name AS name,
        CASE 
            WHEN scl.dia = 0 THEN 'Domingo'
            WHEN scl.dia = 1 THEN 'Lunes'
            WHEN scl.dia = 2 THEN 'Martes'
            WHEN scl.dia = 3 THEN 'Miércoles'
            WHEN scl.dia = 4 THEN 'Jueves'
            WHEN scl.dia = 5 THEN 'Viernes'
            WHEN scl.dia = 6 THEN 'Sábado'
        END AS dia,
        scl.hora_inicial AS horaInicial,
        scl.hora_final AS horaFinal,
        UPPER(u.name || ' ' || u.last_name) AS nameTeacher
    FROM
        schelude scl
    JOIN 
        subject s2 ON scl.code_asignature_fk = s2.code
    JOIN 
        enroll erl ON erl.cod_asignature_fk = s2.code 
    JOIN 
        users u ON s2.code_profe_asignado = u.id
    WHERE
        erl.cod_estudiante_fk = :studentId
    ORDER BY scl.dia ASC
    """, nativeQuery = true)
    List<Object[]> findScheludeOrderedByDayForStudent(Integer studentId);





}
