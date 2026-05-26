package com.example.sreepooja.Service.Poojas;

import com.example.sreepooja.DTO.Poojas.Request.CreatePoojaServiceRequest;
import com.example.sreepooja.DTO.Poojas.Response.*;

import java.util.List;

public interface PoojaServicesService {

    String createPoojaService(CreatePoojaServiceRequest request);

    List<CategoryResponse> getAllCategories();

    List<LanguageResponse> getAllLanguages();

    List<CommunityResponse> getAllCommunities();

    List<CityResponse> getAllCities();

    List<PoojaServiceCardResponse> filterServices(
            String categorySlug,
            Long cityId,
            Long languageId,
            Long communityId,
            String search
    );

    PoojaServiceDetailsResponse
    getServiceDetails(String slug);
}