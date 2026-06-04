package com.example.sreepooja.Service.Poojas;

import com.example.sreepooja.DTO.Request.Poojas.CreateCategoryRequest;
import com.example.sreepooja.DTO.Request.Poojas.CreatePoojaServiceRequest;
import com.example.sreepooja.DTO.Response.Poojas.CategoryResponse;
import com.example.sreepooja.DTO.Response.Poojas.PoojaServiceCardResponse;
import com.example.sreepooja.DTO.Response.Poojas.PoojaServiceDetailsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PoojaServicesService {

    String createPoojaService(
            CreatePoojaServiceRequest request,
            MultipartFile thumbnailImage,
            MultipartFile bannerImage
    );

    Page<PoojaServiceCardResponse> filterServices(
            String categorySlug,
            Long cityId,
            Long languageId,
            Long communityId,
            String search,
            Pageable pageable
    );

    String createCategory(CreateCategoryRequest request);

    CategoryResponse getCategoryDetailsForAdmin(String slug);

    String updatePoojaService(
            Long id,
            CreatePoojaServiceRequest request,
            MultipartFile thumbnailImage,
            MultipartFile bannerImage
    );

    PoojaServiceDetailsResponse
    getServiceDetails(String slug);

    List<CategoryResponse> getAllCategoriesForAdmin();

    List<CategoryResponse> getActiveCategories();

    List<PoojaServiceCardResponse> getAllServicesForAdmin();

    PoojaServiceDetailsResponse
    getServiceDetailsForAdmin(String slug);

    String updateCategory(
            Long id,
            CreateCategoryRequest request
    );

    List<PoojaServiceCardResponse>
    getFeaturedServices();
}