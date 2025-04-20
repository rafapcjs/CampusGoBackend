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
}
