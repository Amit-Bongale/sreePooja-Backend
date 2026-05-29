package com.example.sreepooja.Service.Poojas;

import com.example.sreepooja.DTO.Request.Poojas.CreateCategoryRequest;
import com.example.sreepooja.DTO.Request.Poojas.CreatePoojaServiceRequest;
import com.example.sreepooja.DTO.Response.Poojas.CategoryResponse;
import com.example.sreepooja.DTO.Response.Poojas.PoojaServiceCardResponse;
import com.example.sreepooja.DTO.Response.Poojas.PoojaServiceDetailsResponse;

import java.util.List;

public interface PoojaServicesService {

    String createPoojaService(
            CreatePoojaServiceRequest request
    );

    List<PoojaServiceCardResponse> filterServices(
            String categorySlug,
            Long cityId,
            Long languageId,
            Long communityId,
            String search
    );

    String createCategory(CreateCategoryRequest request);

    CategoryResponse getCategoryDetailsForAdmin(String slug);

    String updateCategory(
            String Slug,
            CreateCategoryRequest request
    );

    PoojaServiceDetailsResponse
    getServiceDetails(String slug);

    List<CategoryResponse> getAllCategoriesForAdmin();

    List<CategoryResponse> getActiveCategories();

    List<PoojaServiceCardResponse> getAllServicesForAdmin();

    PoojaServiceDetailsResponse
    getServiceDetailsForAdmin(String slug);

    String updatePoojaService(
            String slug,
            CreatePoojaServiceRequest request
    );

    List<PoojaServiceCardResponse>
    getFeaturedServices();
}