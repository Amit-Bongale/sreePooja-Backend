package com.example.sreepooja.Controller.Poojas;

import com.example.sreepooja.DTO.Poojas.Request.CreatePoojaServiceRequest;
import com.example.sreepooja.DTO.Poojas.Response.*;
import com.example.sreepooja.Service.Poojas.PoojaServicesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pooja-services")
@RequiredArgsConstructor
public class PoojaServicesController {

    private final PoojaServicesService poojaServicesService;

    // CREATE SERVICE

    @PostMapping
    public ResponseEntity<String> createPoojaService(
            @Valid @RequestBody CreatePoojaServiceRequest request
    ) {

        String response = poojaServicesService.createPoojaService(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {

        return ResponseEntity.ok(
                poojaServicesService.getAllCategories()
        );
    }

    @GetMapping("/languages")
    public ResponseEntity<List<LanguageResponse>> getAllLanguages() {

        return ResponseEntity.ok(
                poojaServicesService.getAllLanguages()
        );
    }

    @GetMapping("/communities")
    public ResponseEntity<List<CommunityResponse>> getAllCommunities() {

        return ResponseEntity.ok(
                poojaServicesService.getAllCommunities()
        );
    }

    @GetMapping("/cities")
    public ResponseEntity<List<CityResponse>> getAllCities() {

        return ResponseEntity.ok(
                poojaServicesService.getAllCities()
        );
    }

    @GetMapping
    public ResponseEntity<List<PoojaServiceCardResponse>>
    filterServices(

            @RequestParam(required = false)
            String categorySlug,

            @RequestParam(required = false)
            Long cityId,

            @RequestParam(required = false)
            Long languageId,

            @RequestParam(required = false)
            Long communityId,

            @RequestParam(required = false)
            String search
    ) {

        return ResponseEntity.ok(
                poojaServicesService.filterServices(
                        categorySlug,
                        cityId,
                        languageId,
                        communityId,
                        search
                )
        );
    }

    @GetMapping("/{slug}")
    public ResponseEntity<PoojaServiceDetailsResponse>
    getServiceDetails(
            @PathVariable String slug
    ) {

        return ResponseEntity.ok(
                poojaServicesService.getServiceDetails(slug)
        );
    }
}