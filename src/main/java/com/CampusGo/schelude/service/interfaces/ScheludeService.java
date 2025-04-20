package com.CampusGo.schelude.service.interfaces;

import com.CampusGo.schelude.presentation.dto.ScheludeResponseDTO;
import com.CampusGo.schelude.presentation.payload.CreateScheludeRequest;

public interface ScheludeService {
    ScheludeResponseDTO createSchelude(CreateScheludeRequest request);
}
