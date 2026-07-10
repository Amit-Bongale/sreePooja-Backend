package com.example.sreepooja.Controller.Public.Priest;

import com.example.sreepooja.DTO.Request.Priests.CreatePriestRegistrationRequest;
import com.example.sreepooja.Service.Priests.PriestRegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/public/priest-registration")
@RequiredArgsConstructor
public class PriestRegistrationController {

    private final PriestRegistrationService priestRegistrationService;

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<String> registerPriest(

            @RequestPart("request")
            @Valid
            CreatePriestRegistrationRequest request,

            @RequestPart("priestPhoto")
            MultipartFile priestPhoto,

            @RequestPart("aadhaarPdf")
            MultipartFile aadhaarPdf
    ) {

        return ResponseEntity.ok(
                priestRegistrationService.registerPriest(
                        request,
                        priestPhoto,
                        aadhaarPdf
                )
        );
    }
}
