package com.example.sreepooja.Controller.Admin.Poojas;

import com.example.sreepooja.DTO.Request.Poojas.CreatePoojaServiceRequest;
import com.example.sreepooja.DTO.Response.Poojas.PoojaServiceCardResponse;
import com.example.sreepooja.DTO.Response.Poojas.PoojaServiceDetailsResponse;
import com.example.sreepooja.Service.Poojas.PoojaServicesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/admin/pooja-services")
@RequiredArgsConstructor
public class AdminPoojaServiceController {

    private final PoojaServicesService poojaServicesService;

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<String> createPoojaService(

            @RequestPart("request")
            @Valid
            CreatePoojaServiceRequest request,

            @RequestPart("thumbnailImage")
            MultipartFile thumbnailImage,

            @RequestPart(
                    value = "bannerImage",
                    required = false
            )
            MultipartFile bannerImage
    ) {

        String response =
                poojaServicesService.createPoojaService(
                        request,
                        thumbnailImage,
                        bannerImage
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

    @PutMapping(
            value = "/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<String> updatePoojaService(

            @PathVariable
            Long id,

            @RequestPart("request")
            @Valid
            CreatePoojaServiceRequest request,

            @RequestPart(
                    value = "thumbnailImage",
                    required = false
            )
            MultipartFile thumbnailImage,

            @RequestPart(
                    value = "bannerImage",
                    required = false
            )
            MultipartFile bannerImage
    ){

        return ResponseEntity.ok(
                poojaServicesService.updatePoojaService(
                        id,
                        request,
                        thumbnailImage,
                        bannerImage
                )
        );
    }
}