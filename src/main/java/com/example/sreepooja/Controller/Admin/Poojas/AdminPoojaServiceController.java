package com.example.sreepooja.Controller.Admin.Poojas;

import com.example.sreepooja.DTO.Request.Poojas.CreatePoojaServiceRequest;
import com.example.sreepooja.DTO.Response.Poojas.PoojaServiceCardResponse;
import com.example.sreepooja.DTO.Response.Poojas.PoojaServiceDetailsResponse;
import com.example.sreepooja.Service.Poojas.PoojaServicesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/pooja-services")
@RequiredArgsConstructor
public class AdminPoojaServiceController {

    private final PoojaServicesService poojaServicesService;

    // CREATE SERVICE UNDER CATEGORY

    @PostMapping
    public ResponseEntity<String> createPoojaService(

            @Valid
            @RequestBody
            CreatePoojaServiceRequest request
    ) {

        String response =
                poojaServicesService.createPoojaService(
                        request
                );

        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<List<PoojaServiceCardResponse>>
    getAllServices() {

        return ResponseEntity.ok(
                poojaServicesService.getAllServicesForAdmin()
        );
    }

    @GetMapping("/{slug}")
    public ResponseEntity<PoojaServiceDetailsResponse>
    getServiceBySlug(
            @PathVariable String slug
    ) {

        return ResponseEntity.ok(
                poojaServicesService.getServiceDetailsForAdmin(slug)
        );
    }

    @PutMapping("/{slug}")
    public ResponseEntity<String> updatePoojaService(

            @PathVariable String slug,

            @Valid
            @RequestBody
            CreatePoojaServiceRequest request
    ) {

        return ResponseEntity.ok(
                poojaServicesService.updatePoojaService(
                        slug,
                        request
                )
        );
    }
}