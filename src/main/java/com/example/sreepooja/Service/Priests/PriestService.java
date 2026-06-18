package com.example.sreepooja.Service.Priests;

import com.example.sreepooja.DTO.Request.Priests.CreatePriestRequest;
import com.example.sreepooja.DTO.Request.Priests.PriestFilterRequest;
import com.example.sreepooja.DTO.Response.Priests.PriestDetailsResponse;
import com.example.sreepooja.DTO.Response.Priests.PriestResponse;
import org.springframework.data.domain.Page;

public interface PriestService {

    PriestResponse createPriest(
            CreatePriestRequest request
    );

    Page<PriestResponse> getAllPriests(
            PriestFilterRequest request,
            int page,
            int size
    );

    PriestDetailsResponse getPriestById(
            Long priestId
    );

    PriestResponse updatePriest(
            Long priestId,
            CreatePriestRequest request
    );
}
