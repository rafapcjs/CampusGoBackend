package com.CampusGo.schelude.service.interfaces;

import com.CampusGo.schelude.presentation.dto.ListOrderScheludeDTO;
import com.CampusGo.schelude.presentation.dto.ScheludeResponseDTO;
import com.CampusGo.schelude.presentation.payload.CreateScheludeRequest;

import java.util.List;

public interface ScheludeService {

    ScheludeResponseDTO createSchelude(CreateScheludeRequest request);
    List<ListOrderScheludeDTO> listAllOrderSchelude();
    List<ListOrderScheludeDTO> getOrderedScheludeByStudent(Integer studentId);

}
