package com.example.sreepooja.Service.Priests;

import com.example.sreepooja.DTO.Request.Priests.CreatePriestRegistrationRequest;
import com.example.sreepooja.DTO.Response.Priests.PriestRegistrationCardResponse;
import com.example.sreepooja.DTO.Response.Priests.PriestRegistrationDetailsResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface PriestRegistrationService {

    String registerPriest(
            CreatePriestRegistrationRequest request,
            MultipartFile priestPhoto,
            MultipartFile aadhaarPdf
    );

    Page<PriestRegistrationCardResponse> getPendingRegistrations(
            int page,
            int size
    );

    PriestRegistrationDetailsResponse getRegistrationDetails(
            Long registrationId
    );
}
