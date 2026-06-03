package com.example.sreepooja.Controller.Admin.Poojas;

import com.example.sreepooja.DTO.Request.Poojas.CreateCategoryRequest;
import com.example.sreepooja.DTO.Response.Poojas.CategoryResponse;
import com.example.sreepooja.Service.Poojas.PoojaServicesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/service-categories")
@RequiredArgsConstructor
public class AdminServiceCategoryController {

    private final PoojaServicesService poojaServicesService;

    // CREATE CATEGORY

    @PostMapping
    public ResponseEntity<String> createCategory(

            @Valid
            @RequestBody
            CreateCategoryRequest request
    ) {

        return ResponseEntity.ok(
                poojaServicesService.createCategory(request)
        );
    }

    @GetMapping("/{slug}")
    public ResponseEntity<CategoryResponse>
    getCategoryBySlug(
            @PathVariable String slug
    ) {

        return ResponseEntity.ok(
                poojaServicesService
                        .getCategoryDetailsForAdmin(slug)
        );
    }

    // UPDATE CATEGORY

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(

            @PathVariable
            Long id,

            @Valid
            @RequestBody
            CreateCategoryRequest request
    ) {

        return ResponseEntity.ok(
                poojaServicesService.updateCategory(
                        id,
                        request
                )
        );
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {

        return ResponseEntity.ok(
                poojaServicesService.getAllCategoriesForAdmin()
        );
    }
}