package com.example.sreepooja.Service.Priests;

import com.example.sreepooja.DTO.Request.Priests.CreatePriestRegistrationRequest;
import org.springframework.web.multipart.MultipartFile;

public interface PriestRegistrationService {

    String registerPriest(
            CreatePriestRegistrationRequest request,
            MultipartFile priestPhoto,
            MultipartFile aadhaarPdf
    );



}
