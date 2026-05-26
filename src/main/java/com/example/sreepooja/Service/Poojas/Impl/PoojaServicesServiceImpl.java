package com.example.sreepooja.Service.Poojas.Impl;

import com.example.sreepooja.DTO.Poojas.Request.CreatePoojaServiceRequest;
import com.example.sreepooja.DTO.Poojas.Request.CreateServicePackageRequest;
import com.example.sreepooja.DTO.Poojas.Response.*;
import com.example.sreepooja.Entity.Masters.City;
import com.example.sreepooja.Entity.Masters.Community;
import com.example.sreepooja.Entity.Masters.Language;
import com.example.sreepooja.Entity.Poojas.*;
import com.example.sreepooja.Enum.Poojas.PackageType;
import com.example.sreepooja.Repository.CityRepository;
import com.example.sreepooja.Repository.CommunityRepository;
import com.example.sreepooja.Repository.LanguageRepository;
import com.example.sreepooja.Repository.Poojas.PoojaServicesRepository;
import com.example.sreepooja.Repository.Poojas.ServiceCategoryRepository;
import com.example.sreepooja.Service.Poojas.PoojaServicesService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PoojaServicesServiceImpl implements PoojaServicesService {

    private final PoojaServicesRepository poojaServicesRepository;
    private final ServiceCategoryRepository serviceCategoryRepository;
    private final LanguageRepository languageRepository;
    private final CommunityRepository communityRepository;
    private final CityRepository cityRepository;

    @Override
    public String createPoojaService(CreatePoojaServiceRequest request) {

        if (poojaServicesRepository.existsBySlug(request.getSlug())) {
            throw new RuntimeException("Slug already exists");
        }

        ServiceCategory category = serviceCategoryRepository
                .findByIdAndDeletedFalse(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));



        PoojaServices poojaService = PoojaServices.builder()
                .serviceName(request.getServiceName())
                .slug(request.getSlug())
                .category(category)
                .shortDescription(request.getShortDescription())
                .fullDescription(request.getFullDescription())
                .benefits(request.getBenefits())
                .durationMinutes(request.getDurationMinutes())
                .status(request.getStatus())
                .featured(request.getFeatured())
                .cancellationAllowed(request.getCancellationAllowed())
                .refundAllowed(request.getRefundAllowed())
                .metaTitle(request.getMetaTitle())
                .metaDescription(request.getMetaDescription())
                .metaKeywords(request.getMetaKeywords())
                .thumbnailImage(request.getThumbnailImage())
                .bannerImage(request.getBannerImage())
                .build();


        // PACKAGES

        List<ServicePackage> packageList = new ArrayList<>();

        if (request.getPackages() != null) {

            for (CreateServicePackageRequest packageRequest : request.getPackages()) {

                ServicePackage servicePackage = ServicePackage.builder()
                        .packageType(packageRequest.getPackageType())
                        .shortDescription(packageRequest.getShortDescription())
                        .includedItems(packageRequest.getIncludedItems())
                        .price(packageRequest.getPrice())
                        .advancePercentage(packageRequest.getAdvancePercentage())
                        .durationMinutes(packageRequest.getDurationMinutes())
                        .active(packageRequest.getActive())
                        .poojaService(poojaService)
                        .build();

                packageList.add(servicePackage);
            }
        }

        poojaService.setPackages(packageList);


        // LANGUAGES

        List<ServiceLanguageMapping> languageMappings = new ArrayList<>();

        if (request.getLanguageIds() != null) {

            for (Long languageId : request.getLanguageIds()) {

                Language language = languageRepository.findById(languageId)
                        .orElseThrow(() -> new RuntimeException("Language not found"));

                ServiceLanguageMapping mapping = ServiceLanguageMapping.builder()
                        .poojaService(poojaService)
                        .language(language)
                        .build();

                languageMappings.add(mapping);
            }
        }

        poojaService.setLanguages(languageMappings);


        // COMMUNITIES

        List<ServiceCommunityMapping> communityMappings = new ArrayList<>();

        if (request.getCommunityIds() != null) {

            for (Long communityId : request.getCommunityIds()) {

                Community community = communityRepository.findById(communityId)
                        .orElseThrow(() -> new RuntimeException("Community not found"));

                ServiceCommunityMapping mapping = ServiceCommunityMapping.builder()
                        .poojaService(poojaService)
                        .community(community)
                        .build();

                communityMappings.add(mapping);
            }
        }

        poojaService.setCommunities(communityMappings);


        // CITIES

        List<ServiceCityMapping> cityMappings = new ArrayList<>();

        if (request.getCityIds() != null) {

            for (Long cityId : request.getCityIds()) {

                City city = cityRepository.findById(cityId)
                        .orElseThrow(() -> new RuntimeException("City not found"));

                ServiceCityMapping mapping = ServiceCityMapping.builder()
                        .poojaService(poojaService)
                        .city(city)
                        .build();

                cityMappings.add(mapping);
            }
        }

        poojaService.setLocations(cityMappings);



        poojaServicesRepository.save(poojaService);

        return "Pooja service created successfully";
    }

    @Override
    public List<CategoryResponse> getAllCategories() {

        List<ServiceCategory> categories =
                serviceCategoryRepository.findByDeletedFalse();

        return categories.stream()
                .map(category -> CategoryResponse.builder()
                        .id(category.getId())
                        .categoryName(category.getCategoryName())
                        .slug(category.getSlug())
                        .build())
                .toList();
    }

    @Override
    public List<LanguageResponse> getAllLanguages() {

        return languageRepository.findAll()
                .stream()
                .filter(Language::getActive)
                .map(language -> LanguageResponse.builder()
                        .id(language.getId())
                        .name(language.getName())
                        .build())
                .toList();
    }

    @Override
    public List<CommunityResponse> getAllCommunities() {

        return communityRepository.findAll()
                .stream()
                .filter(Community::getActive)
                .map(community -> CommunityResponse.builder()
                        .id(community.getId())
                        .name(community.getName())
                        .build())
                .toList();
    }

    @Override
    public List<CityResponse> getAllCities() {

        return cityRepository.findAll()
                .stream()
                .filter(City::getActive)
                .map(city -> CityResponse.builder()
                        .id(city.getId())
                        .cityName(city.getCityName())
                        .build())
                .toList();
    }

    @Override
    public List<PoojaServiceCardResponse> filterServices(
            String categorySlug,
            Long cityId,
            Long languageId,
            Long communityId,
            String search
    ) {

        List<PoojaServices> services =
                poojaServicesRepository.filterServices(
                        categorySlug,
                        cityId,
                        languageId,
                        communityId,
                        search
                );



        return services.stream()
                .map(service -> {

                    String startingPrice = null;

                    if (service.getPackages() != null) {

                        ServicePackage classicPackage =
                                service.getPackages()
                                        .stream()
                                        .filter(pkg ->
                                                pkg.getPackageType()
                                                        == PackageType.CLASSIC
                                        )
                                        .findFirst()
                                        .orElse(null);

                        if (classicPackage != null) {

                            startingPrice =
                                    "₹" + classicPackage.getPrice();
                        }
                    }



                    return PoojaServiceCardResponse.builder()
                            .id(service.getId())
                            .serviceName(service.getServiceName())
                            .slug(service.getSlug())
                            .categoryName(
                                    service.getCategory()
                                            .getCategoryName()
                            )
                            .durationMinutes(
                                    service.getDurationMinutes()
                            )
                            .thumbnailImage(
                                    service.getThumbnailImage()
                            )
                            .startingPrice(startingPrice)
                            .build();
                })
                .toList();
    }

    @Override
    public PoojaServiceDetailsResponse
    getServiceDetails(String slug) {

        PoojaServices service =
                poojaServicesRepository
                        .findBySlugAndDeletedFalse(slug)
                        .orElseThrow(() ->
                                new RuntimeException("Service not found")
                        );



        List<String> languages =
                service.getLanguages()
                        .stream()
                        .map(mapping ->
                                mapping.getLanguage().getName()
                        )
                        .toList();



        List<String> communities =
                service.getCommunities()
                        .stream()
                        .map(mapping ->
                                mapping.getCommunity().getName()
                        )
                        .toList();



        List<String> cities =
                service.getLocations()
                        .stream()
                        .map(mapping ->
                                mapping.getCity().getCityName()
                        )
                        .toList();



        List<ServicePackageResponse> packages =
                service.getPackages()
                        .stream()
                        .map(pkg ->
                                ServicePackageResponse.builder()
                                        .packageType(pkg.getPackageType())
                                        .shortDescription(pkg.getShortDescription())
                                        .includedItems(pkg.getIncludedItems())
                                        .price(pkg.getPrice())
                                        .advancePercentage(
                                                pkg.getAdvancePercentage()
                                        )
                                        .durationMinutes(
                                                pkg.getDurationMinutes()
                                        )
                                        .build()
                        )
                        .toList();



        return PoojaServiceDetailsResponse.builder()
                .id(service.getId())
                .serviceName(service.getServiceName())
                .slug(service.getSlug())
                .categoryName(
                        service.getCategory().getCategoryName()
                )
                .shortDescription(service.getShortDescription())
                .fullDescription(service.getFullDescription())
                .benefits(service.getBenefits())
                .durationMinutes(service.getDurationMinutes())
                .thumbnailImage(service.getThumbnailImage())
                .bannerImage(service.getBannerImage())
                .languages(languages)
                .communities(communities)
                .cities(cities)
                .packages(packages)
                .build();
    }
}