package com.CampusGo.subject.persistencie.repository;

import com.CampusGo.subject.persistencie.entity.Subject;
import com.CampusGo.subject.presentation.dto.SubjectDetailsResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    boolean existsByCode(Integer code);
    Optional<Subject> findByCode(Integer code);

    @Query("""
    SELECT NEW com.CampusGo.subject.presentation.dto.SubjectDetailsResponseDto(
        s.code, 
        s.name, 
        CONCAT(a.anio, ' - Semestre ', a.nSemestre), 
        a.code, 
        prof.teacherCode, 
        u.name, 
        u.lastName
    )
    FROM Subject s
    JOIN Academic a ON a.code = s.codePeriodoAca
    JOIN Teacher prof ON prof.id = s.teacher.id 
    JOIN UserEntity u ON u.id = prof.user.id  
    WHERE s.code = :code
""")
    Optional<SubjectDetailsResponseDto> findSubjectDetailsByCode(Integer code);


    @Query("""
    SELECT NEW com.CampusGo.subject.presentation.dto.SubjectDetailsResponseDto(
        s.code, 
        s.name, 
        CONCAT(a.anio, ' - Semestre ', a.nSemestre), 
        a.code, 
        prof.teacherCode, 
        u.name, 
        u.lastName
    )
    FROM Subject s
    JOIN Academic a ON a.code = s.codePeriodoAca
    JOIN Teacher prof ON prof.id = s.teacher.id 
    JOIN UserEntity u ON u.id = prof.user.id  
    ORDER BY s.code ASC
""")
    Page<SubjectDetailsResponseDto> findAllSubjectDetailsOrderedByCode(Pageable pageable);




    @Query("""
    SELECT NEW com.CampusGo.subject.presentation.dto.SubjectDetailsResponseDto(
        s.code, 
        s.name, 
        CONCAT(a.anio, ' - Semestre ', a.nSemestre), 
        a.code, 
        prof.teacherCode, 
        u.name, 
        u.lastName
    )
    FROM Subject s
    JOIN Academic a ON a.code = s.codePeriodoAca
    JOIN Teacher prof ON prof.id = s.teacher.id 
    JOIN UserEntity u ON u.id = prof.user.id  
    ORDER BY s.name ASC
""")
    Page<SubjectDetailsResponseDto> findAllSubjectDetailsOrderedByName(Pageable pageable);


    @Query("""
    SELECT new com.CampusGo.subject.presentation.dto.SubjectDetailsResponseDto(
        s.code,
        s.name,
        CONCAT(a.anio, ' - Semestre ', a.nSemestre),
        a.code,
        prof.teacherCode,
        u.name,
        u.lastName
    )
    FROM Subject s
    JOIN s.academic a
    JOIN s.teacher prof
    JOIN prof.user u
    WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))
    ORDER BY s.code ASC
""")
    Page<SubjectDetailsResponseDto> findBySubjectNameContainingIgnoreCase(@Param("name") String name, Pageable pageable);

}
