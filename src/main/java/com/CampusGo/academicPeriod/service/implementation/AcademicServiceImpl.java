package com.CampusGo.academicPeriod.service.implementation;

import com.CampusGo.academicPeriod.persistencie.entity.Academic;
import com.CampusGo.academicPeriod.persistencie.repository.AcademicRepository;
import com.CampusGo.academicPeriod.presentation.dto.AcademicResponseDto;
import com.CampusGo.academicPeriod.presentation.payload.CreateAcademicRequest;
import com.CampusGo.academicPeriod.presentation.payload.UpdateAcademicRequest;
import com.CampusGo.academicPeriod.service.interfaces.AcademicService;
import com.CampusGo.commons.configs.message.InfoMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AcademicServiceImpl implements AcademicService {

    @Autowired
    private AcademicRepository academicRepository;



    public ResponseEntity<?> getAllAcademicPeriods() {
        List<Academic> academics = academicRepository.findAll();

        if (academics.isEmpty()) {
            return ResponseEntity.ok(
                    new InfoMessage("No hay registros de periodos académicos.", LocalDateTime.now())
            );
        }

        List<AcademicResponseDto> response = academics.stream()
                .map(a -> new AcademicResponseDto(a.getCode(), a.getNSemestre(), a.getAnio()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }


    public AcademicResponseDto createAcademicPeriod(CreateAcademicRequest request) {
        // Obtener el mayor code actual y sumarle 1
        Integer nextCode = academicRepository.findAll()
                .stream()
                .map(Academic::getCode)
                .max(Comparator.naturalOrder())
                .orElse(0) + 1;

        Academic academic = new Academic();
        academic.setCode(nextCode);
        academic.setNSemestre(request.getNSemestre());
        academic.setAnio(request.getAnio());

        Academic saved = academicRepository.save(academic);
        return new AcademicResponseDto(saved.getCode(), saved.getNSemestre(), saved.getAnio());
    }


    public AcademicResponseDto updateAcademicPeriod(UpdateAcademicRequest request) {
        Academic academic = academicRepository.findByCode(request.getCode());
        if (academic == null) {
            throw new RuntimeException("Academic period with code " + request.getCode() + " not found");
        }

        academic.setNSemestre(request.getNSemestre());
        academic.setAnio(request.getAnio());

        Academic updated = academicRepository.save(academic);
        return new AcademicResponseDto(updated.getCode(), updated.getNSemestre(), updated.getAnio());
    }
}
