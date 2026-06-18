package com.example.sreepooja.Controller.Admin.Priests;

import com.example.sreepooja.DTO.Request.Priests.CreatePriestRequest;
import com.example.sreepooja.DTO.Request.Priests.PriestFilterRequest;
import com.example.sreepooja.DTO.Response.Priests.PriestDetailsResponse;
import com.example.sreepooja.DTO.Response.Priests.PriestResponse;
import com.example.sreepooja.Service.Priests.PriestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ResponseEntity<Page<PriestResponse>>
    getAllPriests(

            @ModelAttribute
            PriestFilterRequest request,

            @RequestParam(
                    defaultValue = "0"
            )
            int page,

            @RequestParam(
                    defaultValue = "15"
            )
            int size
    ) {

        return ResponseEntity.ok(
                priestService.getAllPriests(
                        request,
                        page,
                        size
                )
        );
    }

    @GetMapping("/{priestId}")
    public ResponseEntity<PriestDetailsResponse>
    getPriestById(

            @PathVariable
            Long priestId
    ) {

        return ResponseEntity.ok(
                priestService
                        .getPriestById(
                                priestId
                        )
        );
    }

    @PutMapping("/{priestId}")
    public ResponseEntity<PriestResponse>
    updatePriest(

            @PathVariable
            Long priestId,

            @Valid
            @RequestBody
            CreatePriestRequest request
    ) {

        return ResponseEntity.ok(
                priestService.updatePriest(
                        priestId,
                        request
                )
        );
    }
}
