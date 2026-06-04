package com.example.sreepooja.Controller.Public.Poojas;

import com.example.sreepooja.DTO.Response.Poojas.CategoryResponse;
import com.example.sreepooja.DTO.Response.Poojas.PoojaServiceCardResponse;
import com.example.sreepooja.DTO.Response.Poojas.PoojaServiceDetailsResponse;
import com.example.sreepooja.Service.Poojas.PoojaServicesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/pooja-services")
@RequiredArgsConstructor
public class PoojaServicesController {

    private final PoojaServicesService poojaServicesService;

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryResponse>> getActiveCategories() {

        return ResponseEntity.ok(
                poojaServicesService.getActiveCategories()
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

    @GetMapping("/featured")
    public ResponseEntity<
            List<PoojaServiceCardResponse>
            > getFeaturedServices() {

        return ResponseEntity.ok(
                poojaServicesService
                        .getFeaturedServices()
        );
    }
}