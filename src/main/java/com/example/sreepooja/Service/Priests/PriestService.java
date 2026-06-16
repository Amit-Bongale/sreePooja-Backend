package com.example.sreepooja.Service.Priests;

import com.example.sreepooja.DTO.Request.Priests.CreatePriestRequest;
import com.example.sreepooja.DTO.Response.Priests.PriestResponse;

public interface PriestService {

    PriestResponse createPriest(
            CreatePriestRequest request
    );
}
