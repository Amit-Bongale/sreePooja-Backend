package com.example.sreepooja.Service.Poojas.Impl;

import com.example.sreepooja.DTO.Request.Poojas.CreateCategoryRequest;
import com.example.sreepooja.DTO.Request.Poojas.CreatePoojaServiceRequest;
import com.example.sreepooja.DTO.Request.Poojas.CreateServicePackageRequest;
import com.example.sreepooja.DTO.Response.Poojas.CategoryResponse;
import com.example.sreepooja.DTO.Response.Poojas.PoojaServiceCardResponse;
import com.example.sreepooja.DTO.Response.Poojas.PoojaServiceDetailsResponse;
import com.example.sreepooja.DTO.Response.Poojas.ServicePackageResponse;
import com.example.sreepooja.Entity.Masters.City;
import com.example.sreepooja.Entity.Masters.Community;
import com.example.sreepooja.Entity.Masters.Language;
import com.example.sreepooja.Entity.Poojas.*;
import com.example.sreepooja.Enum.Poojas.PackageType;
import com.example.sreepooja.Enum.Poojas.ServiceStatus;
import com.example.sreepooja.ExceptionHandlers.DuplicateResourceException;
import com.example.sreepooja.ExceptionHandlers.ResourceNotFoundException;
import com.example.sreepooja.Repository.Masters.CityRepository;
import com.example.sreepooja.Repository.Masters.CommunityRepository;
import com.example.sreepooja.Repository.Masters.LanguageRepository;
import com.example.sreepooja.Repository.Poojas.PoojaServicesRepository;
import com.example.sreepooja.Repository.Poojas.ServiceCategoryRepository;
import com.example.sreepooja.Service.File.FileService;
import com.example.sreepooja.Service.Poojas.PoojaServicesService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PoojaServicesServiceImpl implements PoojaServicesService {

    private final PoojaServicesRepository poojaServicesRepository;
    private final ServiceCategoryRepository serviceCategoryRepository;
    private final LanguageRepository languageRepository;
    private final CommunityRepository communityRepository;
    private final CityRepository cityRepository;
    private final FileService fileService;

    @Override
    @Transactional
    public String createPoojaService(
            CreatePoojaServiceRequest request
    ) {

        if (poojaServicesRepository.existsBySlug(request.getSlug())) {
            throw new DuplicateResourceException("Slug already exists");
        }

        ServiceCategory category = serviceCategoryRepository
                .findBySlug(request.getCategorySlug())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found"));

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

            for (CreateServicePackageRequest packageRequest
                    : request.getPackages()) {

                ServicePackage servicePackage = ServicePackage.builder()
                        .packageType(packageRequest.getPackageType())
                        .shortDescription(
                                packageRequest.getShortDescription()
                        )
                        .includedItems(
                                packageRequest.getIncludedItems()
                        )
                        .price(packageRequest.getPrice())
                        .advancePercentage(
                                packageRequest.getAdvancePercentage()
                        )
                        .durationMinutes(
                                packageRequest.getDurationMinutes()
                        )
                        .status(ServiceStatus.ACTIVE)
                        .poojaService(poojaService)
                        .build();

                packageList.add(servicePackage);
            }
        }

        poojaService.setPackages(packageList);


        // LANGUAGES

        List<ServiceLanguageMapping> languageMappings =
                new ArrayList<>();

        if (request.getLanguageIds() != null) {

            Set<Long> uniqueLanguageIds =
                    new HashSet<>(request.getLanguageIds());

            for (Long languageId : uniqueLanguageIds) {

                Language language = languageRepository
                        .findByIdAndActiveTrue(languageId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Language not found"
                                ));

                ServiceLanguageMapping mapping =
                        ServiceLanguageMapping.builder()
                                .poojaService(poojaService)
                                .language(language)
                                .build();

                languageMappings.add(mapping);
            }
        }

        poojaService.setLanguages(languageMappings);


        // COMMUNITIES

        List<ServiceCommunityMapping> communityMappings =
                new ArrayList<>();

        if (request.getCommunityIds() != null) {

            Set<Long> uniqueCommunityIds =
                    new HashSet<>(request.getCommunityIds());

            for (Long communityId : uniqueCommunityIds) {

                Community community = communityRepository
                        .findByIdAndActiveTrue(communityId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Community not found"
                                ));

                ServiceCommunityMapping mapping =
                        ServiceCommunityMapping.builder()
                                .poojaService(poojaService)
                                .community(community)
                                .build();

                communityMappings.add(mapping);
            }
        }

        poojaService.setCommunities(communityMappings);


        // CITIES

        List<ServiceCityMapping> cityMappings =
                new ArrayList<>();

        if (request.getCityIds() != null) {

            Set<Long> uniqueCityIds =
                    new HashSet<>(request.getCityIds());

            for (Long cityId : uniqueCityIds) {

                City city = cityRepository
                        .findByIdAndActiveTrue(cityId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "City not found"
                                ));

                ServiceCityMapping mapping =
                        ServiceCityMapping.builder()
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
    public List<CategoryResponse> getAllCategoriesForAdmin() {

        List<ServiceCategory> categories =
                serviceCategoryRepository.findAll();

        return categories.stream()
                .map(category -> CategoryResponse.builder()
                        .id(category.getId())
                        .categoryName(category.getCategoryName())
                        .slug(category.getSlug())
                        .status(category.getStatus())
                        .build())
                .toList();
    }

    @Override
    public CategoryResponse
    getCategoryDetailsForAdmin(String slug) {

        ServiceCategory category =
                serviceCategoryRepository
                        .findBySlug(slug)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Category not found"
                                )
                        );

        return CategoryResponse.builder()
                .id(category.getId())
                .categoryName(category.getCategoryName())
                .slug(category.getSlug())
                .status(category.getStatus())
                .build();
    }

    @Override
    public List<CategoryResponse> getActiveCategories() {

        List<ServiceCategory> categories =
                serviceCategoryRepository.findByStatus(
                        ServiceStatus.ACTIVE
                );

        return categories.stream()
                .map(category -> CategoryResponse.builder()
                        .id(category.getId())
                        .categoryName(category.getCategoryName())
                        .slug(category.getSlug())
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
                            .categorySlug(
                                    service.getCategory()
                                            .getSlug()
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
                        .findBySlugAndStatus(slug, ServiceStatus.ACTIVE)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Service not found")
                        );



        List<String> languages =
                service.getLanguages()
                        .stream()
                        .map(mapping ->
                                mapping.getLanguage().getLanguageName()
                        )
                        .toList();



        List<String> communities =
                service.getCommunities()
                        .stream()
                        .map(mapping ->
                                mapping.getCommunity().getCommunityName()
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
                .categorySlug(
                        service.getCategory().getSlug()
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

    @Override
    public String createCategory(
            CreateCategoryRequest request
    ) {

        if (serviceCategoryRepository.existsByCategoryName(
                request.getCategoryName()
        )) {

            throw new DuplicateResourceException(
                    "Category name already exists"
            );
        }

        if (serviceCategoryRepository.existsBySlug(
                request.getSlug()
        )) {

            throw new DuplicateResourceException(
                    "Slug already exists"
            );
        }

        ServiceCategory category =
                ServiceCategory.builder()
                        .categoryName(request.getCategoryName())
                        .slug(request.getSlug())
                        .status(request.getStatus())
                        .build();

        serviceCategoryRepository.save(category);

        return "Category created successfully";
    }

    @Override
    public String updateCategory(
            String slug,
            CreateCategoryRequest request
    ) {

        ServiceCategory category =
                serviceCategoryRepository
                        .findBySlug(slug)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Category not found"
                                )
                        );

        if (!category.getCategoryName()
                .equals(request.getCategoryName())

                &&

                serviceCategoryRepository.existsByCategoryName(
                        request.getCategoryName()
                )) {

            throw new DuplicateResourceException(
                    "Category name already exists"
            );
        }



        if (!category.getSlug()
                .equals(request.getSlug())

                &&

                serviceCategoryRepository.existsBySlug(
                        request.getSlug()
                )) {

            throw new DuplicateResourceException(
                    "Slug already exists"
            );
        }

        category.setCategoryName(
                request.getCategoryName()
        );

        category.setSlug(
                request.getSlug()
        );

        category.setStatus(
                request.getStatus()
        );



        serviceCategoryRepository.save(category);

        return "Category updated successfully";
    }

    @Override
    public List<PoojaServiceCardResponse>
    getAllServicesForAdmin() {

        List<PoojaServices> services =
                poojaServicesRepository.findAll();

        return services.stream()
                .map(service -> PoojaServiceCardResponse.builder()

                        .serviceName(service.getServiceName())

                        .slug(service.getSlug())

                        .thumbnailImage(
                                service.getThumbnailImage()
                        )

                        .status(service.getStatus())

                        .featured(service.getFeatured())

                        .categorySlug(
                                service.getCategory()
                                        .getSlug()
                        )

                        .build())

                .toList();
    }

    @Override
    public PoojaServiceDetailsResponse
    getServiceDetailsForAdmin(String slug) {

        PoojaServices service =
                poojaServicesRepository
                        .findBySlug(slug)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Service not found"
                                )
                        );



        List<String> languages =
                service.getLanguages()
                        .stream()
                        .map(mapping ->
                                mapping.getLanguage().getLanguageName()
                        )
                        .toList();



        List<String> communities =
                service.getCommunities()
                        .stream()
                        .map(mapping ->
                                mapping.getCommunity().getCommunityName()
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
                                        .packageType(
                                                pkg.getPackageType()
                                        )
                                        .shortDescription(
                                                pkg.getShortDescription()
                                        )
                                        .includedItems(
                                                pkg.getIncludedItems()
                                        )
                                        .price(pkg.getPrice())
                                        .advancePercentage(
                                                pkg.getAdvancePercentage()
                                        )
                                        .durationMinutes(
                                                pkg.getDurationMinutes()
                                        )
                                        .status(pkg.getStatus())
                                        .build()
                        )
                        .toList();



        return PoojaServiceDetailsResponse.builder()

                .id(service.getId())

                .serviceName(service.getServiceName())

                .slug(service.getSlug())

                .categorySlug(
                        service.getCategory()
                                .getSlug()
                )

                .shortDescription(
                        service.getShortDescription()
                )

                .fullDescription(
                        service.getFullDescription()
                )

                .benefits(service.getBenefits())

                .durationMinutes(
                        service.getDurationMinutes()
                )

                .status(service.getStatus())

                .featured(service.getFeatured())

                .cancellationAllowed(
                        service.getCancellationAllowed()
                )

                .refundAllowed(
                        service.getRefundAllowed()
                )

                .metaTitle(service.getMetaTitle())

                .metaDescription(
                        service.getMetaDescription()
                )

                .metaKeywords(
                        service.getMetaKeywords()
                )

                .thumbnailImage(
                        service.getThumbnailImage()
                )

                .bannerImage(
                        service.getBannerImage()
                )

                .languages(languages)

                .communities(communities)

                .cities(cities)

                .packages(packages)

                .build();
    }

    @Override
    public String updatePoojaService(
            String slug,
            CreatePoojaServiceRequest request
    ) {

        PoojaServices poojaService =
                poojaServicesRepository
                        .findBySlug(slug)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Service not found"
                                )
                        );


        // CHECK DUPLICATE SLUG

        if (!poojaService.getSlug().equals(request.getSlug())
                &&
                poojaServicesRepository.existsBySlug(
                        request.getSlug()
                )) {

            throw new DuplicateResourceException(
                    "Slug already exists"
            );
        }


        // CATEGORY

        ServiceCategory category =
                serviceCategoryRepository
                        .findBySlug(request.getCategorySlug())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Category not found"
                                )
                        );


        // UPDATE BASIC FIELDS

        poojaService.setServiceName(
                request.getServiceName()
        );

        poojaService.setSlug(
                request.getSlug()
        );

        poojaService.setCategory(category);

        poojaService.setShortDescription(
                request.getShortDescription()
        );

        poojaService.setFullDescription(
                request.getFullDescription()
        );

        poojaService.setBenefits(
                request.getBenefits()
        );

        poojaService.setDurationMinutes(
                request.getDurationMinutes()
        );

        poojaService.setStatus(
                request.getStatus()
        );

        poojaService.setFeatured(
                request.getFeatured()
        );

        poojaService.setCancellationAllowed(
                request.getCancellationAllowed()
        );

        poojaService.setRefundAllowed(
                request.getRefundAllowed()
        );

        poojaService.setMetaTitle(
                request.getMetaTitle()
        );

        poojaService.setMetaDescription(
                request.getMetaDescription()
        );

        poojaService.setMetaKeywords(
                request.getMetaKeywords()
        );

        String oldThumbnailImage =
                poojaService.getThumbnailImage();

        String oldBannerImage =
                poojaService.getBannerImage();


        poojaService.setThumbnailImage(
                request.getThumbnailImage()
        );

        poojaService.setBannerImage(
                request.getBannerImage()
        );


        // CLEAR OLD RELATIONS

        poojaService.getPackages().clear();

        poojaService.getLanguages().clear();

        poojaService.getCommunities().clear();

        poojaService.getLocations().clear();


        // PACKAGES

        List<ServicePackage> packageList =
                new ArrayList<>();

        if (request.getPackages() != null) {

            for (CreateServicePackageRequest packageRequest
                    : request.getPackages()) {

                ServicePackage servicePackage =
                        ServicePackage.builder()
                                .packageType(
                                        packageRequest.getPackageType()
                                )
                                .shortDescription(
                                        packageRequest.getShortDescription()
                                )
                                .includedItems(
                                        packageRequest.getIncludedItems()
                                )
                                .price(
                                        packageRequest.getPrice()
                                )
                                .advancePercentage(
                                        packageRequest
                                                .getAdvancePercentage()
                                )
                                .durationMinutes(
                                        packageRequest
                                                .getDurationMinutes()
                                )
                                .status(packageRequest.getStatus())
                                .poojaService(poojaService)
                                .build();

                packageList.add(servicePackage);
            }
        }

        poojaService.setPackages(packageList);


        // LANGUAGES

        List<ServiceLanguageMapping> languageMappings =
                new ArrayList<>();

        if (request.getLanguageIds() != null) {

            for (Long languageId : request.getLanguageIds()) {

                Language language =
                        languageRepository.findByIdAndActiveTrue(languageId)
                                .orElseThrow(() ->
                                        new ResourceNotFoundException(
                                                "Language not found"
                                        )
                                );

                ServiceLanguageMapping mapping =
                        ServiceLanguageMapping.builder()
                                .poojaService(poojaService)
                                .language(language)
                                .build();

                languageMappings.add(mapping);
            }
        }

        poojaService.setLanguages(languageMappings);


        // COMMUNITIES

        List<ServiceCommunityMapping> communityMappings =
                new ArrayList<>();

        if (request.getCommunityIds() != null) {

            for (Long communityId : request.getCommunityIds()) {

                Community community =
                        communityRepository.findByIdAndActiveTrue(communityId)
                                .orElseThrow(() ->
                                        new ResourceNotFoundException(
                                                "Community not found"
                                        )
                                );

                ServiceCommunityMapping mapping =
                        ServiceCommunityMapping.builder()
                                .poojaService(poojaService)
                                .community(community)
                                .build();

                communityMappings.add(mapping);
            }
        }

        poojaService.setCommunities(
                communityMappings
        );


        // CITIES

        List<ServiceCityMapping> cityMappings =
                new ArrayList<>();

        if (request.getCityIds() != null) {

            for (Long cityId : request.getCityIds()) {

                City city =
                        cityRepository.findByIdAndActiveTrue(cityId)
                                .orElseThrow(() ->
                                        new ResourceNotFoundException(
                                                "City not found"
                                        )
                                );

                ServiceCityMapping mapping =
                        ServiceCityMapping.builder()
                                .poojaService(poojaService)
                                .city(city)
                                .build();

                cityMappings.add(mapping);
            }
        }

        poojaService.setLocations(cityMappings);


        poojaServicesRepository.save(poojaService);

        if (oldThumbnailImage != null
                &&
                !oldThumbnailImage.equals(
                        request.getThumbnailImage()
                )) {

            try {

                fileService.deleteImage(
                        oldThumbnailImage
                );

            } catch (Exception ex) {

                log.error(
                        "Failed to delete thumbnail image: {}",
                        oldThumbnailImage,
                        ex
                );
            }
        }

        if (oldBannerImage != null
                &&
                !oldBannerImage.equals(
                        request.getBannerImage()
                )) {

            try {

                fileService.deleteImage(
                        oldBannerImage
                );

            } catch (Exception ex) {

                log.error(
                        "Failed to delete banner image: {}",
                        oldBannerImage,
                        ex
                );
            }
        }

        return "Pooja service updated successfully";
    }

    @Override
    public List<PoojaServiceCardResponse>
    getFeaturedServices() {

        List<PoojaServices> services =
                poojaServicesRepository
                        .findByFeaturedTrueAndStatus(
                                ServiceStatus.ACTIVE
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
                            .categorySlug(
                                    service.getCategory().getSlug()
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
}