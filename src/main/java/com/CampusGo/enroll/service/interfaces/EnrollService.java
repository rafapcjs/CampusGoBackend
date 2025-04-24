package com.CampusGo.enroll.service.interfaces;

import com.CampusGo.enroll.presentation.payload.BulkEnrollRequest;
import com.CampusGo.enroll.presentation.payload.CreateEnrollRequest;

public interface EnrollService {
    void createEnroll(CreateEnrollRequest request);
    void createBulkEnroll(BulkEnrollRequest request);
    void deleteEnrollByCode(Integer code);

}
