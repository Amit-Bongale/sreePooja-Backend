package com.example.sreepooja.Controller.Admin.Priests;

import com.example.sreepooja.DTO.Request.Priests.CreatePriestRequest;
import com.example.sreepooja.DTO.Response.Priests.PriestResponse;
import com.example.sreepooja.Service.Priests.PriestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/priests")
@RequiredArgsConstructor
public class PriestController {

    private final PriestService priestService;

    @PostMapping
    public ResponseEntity<PriestResponse>
    createPriest(

            @Valid
            @RequestBody
            CreatePriestRequest request
    ) {

        return ResponseEntity.ok(
                priestService
                        .createPriest(
                                request
                        )
        );
    }
}
