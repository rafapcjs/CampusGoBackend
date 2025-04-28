package com.CampusGo.grade.service.interfaces;

import com.CampusGo.grade.presentation.dto.StudentGradesResponse;
import com.CampusGo.grade.presentation.dto.StudentSubjectGradeResponseDto;
import com.CampusGo.grade.presentation.payload.UpdateGradeRequest;

import java.util.List;

public interface GradeService {

    List<StudentGradesResponse> listGradesBySubject();
    StudentGradesResponse listMyGrades();
    List<StudentSubjectGradeResponseDto> listGradesBySubjectForTeacher(Integer subjectCode);

    void udpateGrade (UpdateGradeRequest request,  Integer code);
}
