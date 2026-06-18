package com.example.sreepooja.Service.Poojas.Impl;

import com.example.sreepooja.DTO.Request.Poojas.CreateCategoryRequest;
import com.example.sreepooja.DTO.Request.Poojas.CreatePoojaServiceRequest;
import com.example.sreepooja.DTO.Request.Poojas.CreateServicePackageRequest;
import com.example.sreepooja.DTO.Response.File.FileUploadResponse;
import com.example.sreepooja.DTO.Response.Poojas.CategoryResponse;
import com.example.sreepooja.DTO.Response.Poojas.PoojaServiceCardResponse;
import com.example.sreepooja.DTO.Response.Poojas.PoojaServiceDetailsResponse;
import com.example.sreepooja.DTO.Response.Poojas.ServicePackageResponse;
import com.example.sreepooja.Entity.Masters.City;
import com.example.sreepooja.Entity.Masters.Community;
import com.example.sreepooja.Entity.Masters.Language;
import com.example.sreepooja.Entity.Poojas.*;
import com.example.sreepooja.Enum.File.FileType;
import com.example.sreepooja.Enum.Poojas.PackageType;
import com.example.sreepooja.Enum.Poojas.ServiceStatus;
import com.example.sreepooja.ExceptionHandlers.BadRequestException;
import com.example.sreepooja.ExceptionHandlers.DuplicateResourceException;
import com.example.sreepooja.ExceptionHandlers.InvalidFileException;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
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
            CreatePoojaServiceRequest request,
            MultipartFile thumbnailImage,
            MultipartFile bannerImage
    ) {

        if (poojaServicesRepository.existsBySlug(request.getSlug())) {
            throw new DuplicateResourceException(
                    "Pooja service with slug '" + request.getSlug() + "' already exists"
            );
        }

        if (request.getServiceName() == null ||
                request.getServiceName().isBlank()) {
            throw new BadRequestException(
                    "Service name is required"
            );
        }

        if (request.getSlug() == null ||
                request.getSlug().isBlank()) {
            throw new BadRequestException(
                    "Slug is required"
            );
        }

        if (request.getCategorySlug() == null ||
                request.getCategorySlug().isBlank()) {
            throw new BadRequestException(
                    "Category is required"
            );
        }

        if (request.getShortDescription() == null ||
                request.getShortDescription().isBlank()) {
            throw new BadRequestException(
                    "Short description is required"
            );
        }

        if (request.getFullDescription() == null ||
                request.getFullDescription().isBlank()) {
            throw new BadRequestException(
                    "Full description is required"
            );
        }

        if (request.getBenefits() == null ||
                request.getBenefits().isBlank()) {
            throw new BadRequestException(
                    "Benefits are required"
            );
        }

        if (request.getDurationMinutes() == null) {
            throw new BadRequestException(
                    "Duration is required"
            );
        }

        if (thumbnailImage == null ||
                thumbnailImage.isEmpty()) {
            throw new InvalidFileException(
                    "Thumbnail image is required"
            );
        }

        ServiceCategory category = serviceCategoryRepository
                .findBySlug(request.getCategorySlug())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found with slug: " + request.getCategorySlug()
                        ));

        String thumbnailUrl = null;
        String bannerUrl = null;

        try {

            FileUploadResponse thumbnailResponse =
                    fileService.uploadImage(
                            thumbnailImage,
                            FileType.POOJA_SERVICE_THUMBNAILS
                    );

            thumbnailUrl = thumbnailResponse.getFileUrl();

            if (bannerImage != null && !bannerImage.isEmpty()) {

                FileUploadResponse bannerResponse =
                        fileService.uploadImage(
                                bannerImage,
                                FileType.POOJA_SERVICE_BANNERS
                        );

                bannerUrl = bannerResponse.getFileUrl();
            }

            PoojaServices poojaService = PoojaServices.builder()
                    .serviceName(request.getServiceName())
                    .slug(request.getSlug())
                    .category(category)
                    .shortDescription(request.getShortDescription())
                    .fullDescription(request.getFullDescription())
                    .benefits(request.getBenefits())
                    .durationMinutes(request.getDurationMinutes())
                    .status(request.getStatus())
                    .featured(Boolean.TRUE.equals(request.getFeatured()))
                    .cancellationAllowed(Boolean.TRUE.equals(request.getCancellationAllowed()))
                    .refundAllowed(Boolean.TRUE.equals(request.getRefundAllowed()))
                    .metaTitle(request.getMetaTitle())
                    .metaDescription(request.getMetaDescription())
                    .metaKeywords(request.getMetaKeywords())
                    .thumbnailImage(thumbnailUrl)
                    .bannerImage(bannerUrl)
                    .build();

            buildPackages(request, poojaService);

            buildLanguages(request, poojaService);

            buildCommunities(request, poojaService);

            buildCities(request, poojaService);

            poojaServicesRepository.save(poojaService);

            return "Pooja service created successfully";

        } catch (Exception ex) {

            if (thumbnailUrl != null) {
                fileService.deleteImage(thumbnailUrl);
            }

            if (bannerUrl != null) {
                fileService.deleteImage(bannerUrl);
            }

            throw ex;
        }
    }

    private void buildCities(
            CreatePoojaServiceRequest request,
            PoojaServices poojaService
    ) {

        List<ServiceCityMapping> cityMappings =
                new ArrayList<>();

        if (request.getCityIds() != null &&
                !request.getCityIds().isEmpty()) {

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

        poojaService.getLocations().addAll(cityMappings);
    }

    private void buildCommunities(
            CreatePoojaServiceRequest request,
            PoojaServices poojaService
    ) {

        List<ServiceCommunityMapping> communityMappings =
                new ArrayList<>();

        if (request.getCommunityIds() != null &&
                !request.getCommunityIds().isEmpty()) {

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

        poojaService.getCommunities().addAll(communityMappings);
    }

    private void buildLanguages(
            CreatePoojaServiceRequest request,
            PoojaServices poojaService
    ) {

        List<ServiceLanguageMapping> languageMappings =
                new ArrayList<>();

        if (request.getLanguageIds() != null &&
                !request.getLanguageIds().isEmpty()) {

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

        poojaService.getLanguages().addAll(languageMappings);
    }

    private void buildPackages(
            CreatePoojaServiceRequest request,
            PoojaServices poojaService
    ) {

        List<ServicePackage> packageList = new ArrayList<>();

        Set<PackageType> packageTypes = new HashSet<>();

        if (request.getPackages() != null
                && !request.getPackages().isEmpty()) {

            for (CreateServicePackageRequest packageRequest
                    : request.getPackages()) {

                if (packageRequest.getPackageType() == null) {
                    throw new BadRequestException(
                            "Package type is required"
                    );
                }

                if (!packageTypes.add(
                        packageRequest.getPackageType()
                )) {

                    throw new BadRequestException(
                            "Duplicate package type: "
                                    + packageRequest.getPackageType()
                    );
                }

                if (packageRequest.getPackageType()
                        != PackageType.CUSTOM
                        && (packageRequest.getShortDescription() == null
                        || packageRequest.getShortDescription().isBlank())) {

                    throw new BadRequestException(
                            "Package short description is required"
                    );
                }

                if (packageRequest.getPackageType()
                        != PackageType.CUSTOM && packageRequest.getPrice() == null) {

                    throw new BadRequestException(
                            "Package price is required"
                    );
                }

                if (packageRequest.getPackageType()
                        != PackageType.CUSTOM && packageRequest.getAdvancePercentage() == null) {

                    throw new BadRequestException(
                            "Advance percentage is required"
                    );
                }

                if (packageRequest.getPackageType()
                        == PackageType.PLATINUM
                        &&
                        (packageRequest.getIncludedItems() == null
                                || packageRequest.getIncludedItems().isBlank())) {

                    throw new BadRequestException(
                            "Included items are required for PLATINUM package"
                    );
                }

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
                                        packageRequest.getAdvancePercentage()
                                )
                                .status(packageRequest.getStatus())
                                .poojaService(poojaService)
                                .build();
                packageList.add(servicePackage);
            }
        }

        poojaService.getPackages().addAll(packageList);
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
    public Page<PoojaServiceCardResponse> filterServices(
            String categorySlug,
            Long cityId,
            Long languageId,
            Long communityId,
            String search,
            Pageable page
    ) {

        Page<PoojaServices> services =
                poojaServicesRepository.filterServices(
                        categorySlug,
                        cityId,
                        languageId,
                        communityId,
                        search,
                        page
                );



        return services
                .map(service -> {

                    BigDecimal startingPrice = null;

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

                        ServicePackage platinumPackage =
                                service.getPackages()
                                        .stream()
                                        .filter(pkg ->
                                                pkg.getPackageType()
                                                        == PackageType.PLATINUM
                                        )
                                        .findFirst()
                                        .orElse(null);

                        if (classicPackage != null) {
                            startingPrice = classicPackage.getPrice();
                        } else if (platinumPackage != null){
                            startingPrice = platinumPackage.getPrice();
                        } else{
                            startingPrice = BigDecimal.ZERO;
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
                            .status(service.getStatus())
                            .featured(service.getFeatured())
                            .build();
                });
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
                        .filter(packageitem-> packageitem.getStatus() == ServiceStatus.ACTIVE)
                        .map(pkg ->
                                ServicePackageResponse.builder()
                                        .id(pkg.getId())
                                        .packageType(pkg.getPackageType())
                                        .shortDescription(pkg.getShortDescription())
                                        .includedItems(pkg.getIncludedItems())
                                        .price(pkg.getPrice())
                                        .advancePercentage(
                                                pkg.getAdvancePercentage()
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
                        service.getCategory().getSlug()
                )
                .shortDescription(service.getShortDescription())
                .fullDescription(service.getFullDescription())
                .benefits(service.getBenefits())
                .durationMinutes(service.getDurationMinutes())
                .thumbnailImage(service.getThumbnailImage())
                .bannerImage(service.getBannerImage())
                .metaDescription(service.getMetaDescription())
                .metaKeywords(service.getMetaKeywords())
                .metaTitle(service.getMetaTitle())
                .refundAllowed(service.getRefundAllowed())
                .cancellationAllowed(service.getCancellationAllowed())
                .featured(service.getFeatured())
                .status(service.getStatus())
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

        if (request.getCategoryName() == null ||
                request.getCategoryName().isBlank()) {
            throw new BadRequestException(
                    "Category name is required"
            );
        }

        if (request.getSlug() == null ||
                request.getSlug().isBlank()) {
            throw new BadRequestException(
                    "Slug is required"
            );
        }


        if (request.getStatus() == null) {
            throw new BadRequestException(
                    "Status is required"
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
            Long id,
            CreateCategoryRequest request
    ) {

        ServiceCategory category =
                serviceCategoryRepository
                        .findById(id)
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

        if (request.getCategoryName() == null ||
                request.getCategoryName().isBlank()) {
            throw new BadRequestException(
                    "Category name is required"
            );
        }

        if (request.getSlug() == null ||
                request.getSlug().isBlank()) {
            throw new BadRequestException(
                    "Slug is required"
            );
        }

        if (request.getStatus() == null) {
            throw new BadRequestException(
                    "Status is required"
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
                .map(service -> {

                    BigDecimal startingPrice = null;

                    if(service.getPackages()!= null) {

                        ServicePackage classicPackage =
                                service.getPackages()
                                        .stream()
                                        .filter(pkg ->
                                                pkg.getPackageType()
                                                        == PackageType.CLASSIC
                                        )
                                        .findFirst()
                                        .orElse(null);

                        ServicePackage platinumPackage =
                                service.getPackages()
                                        .stream()
                                        .filter(pkg ->
                                                pkg.getPackageType()
                                                        == PackageType.PLATINUM
                                        )
                                        .findFirst()
                                        .orElse(null);

                        if (classicPackage != null) {
                            startingPrice = classicPackage.getPrice();
                        } else if (platinumPackage != null){
                            startingPrice = platinumPackage.getPrice();
                        } else {
                            startingPrice = BigDecimal.ZERO;
                        }
                    }


                    return PoojaServiceCardResponse.builder()

                            .id(service.getId())

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

                            .startingPrice(startingPrice)

                            .durationMinutes(service.getDurationMinutes())

                            .build();

                })

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
    @Transactional
    public String updatePoojaService(
            Long id,
            CreatePoojaServiceRequest request,
            MultipartFile thumbnailImage,
            MultipartFile bannerImage
    ) {

        if (request.getServiceName() == null ||
                request.getServiceName().isBlank()) {
            throw new BadRequestException(
                    "Service name is required"
            );
        }

        if (request.getSlug() == null ||
                request.getSlug().isBlank()) {
            throw new BadRequestException(
                    "Slug is required"
            );
        }

        if (request.getCategorySlug() == null ||
                request.getCategorySlug().isBlank()) {
            throw new BadRequestException(
                    "Category is required"
            );
        }

        if (request.getShortDescription() == null ||
                request.getShortDescription().isBlank()) {
            throw new BadRequestException(
                    "Short description is required"
            );
        }

        if (request.getFullDescription() == null ||
                request.getFullDescription().isBlank()) {
            throw new BadRequestException(
                    "Full description is required"
            );
        }

        if (request.getBenefits() == null ||
                request.getBenefits().isBlank()) {
            throw new BadRequestException(
                    "Benefits are required"
            );
        }

        if (request.getDurationMinutes() == null) {
            throw new BadRequestException(
                    "Duration is required"
            );
        }


        if (request.getStatus() == null) {
            throw new BadRequestException(
                    "Status is required"
            );
        }

        PoojaServices poojaService =
                poojaServicesRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Service not found"
                                )
                        );

        if (!poojaService.getSlug().equals(request.getSlug())
                &&
                poojaServicesRepository.existsBySlug(
                        request.getSlug()
                )) {

            throw new DuplicateResourceException(
                    "Slug already exists"
            );
        }

        ServiceCategory category =
                serviceCategoryRepository
                        .findBySlug(request.getCategorySlug())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Category not found"
                                )
                        );

        String oldThumbnailImage =
                poojaService.getThumbnailImage();

        String oldBannerImage =
                poojaService.getBannerImage();

        String newThumbnailImage = null;
        String newBannerImage = null;

        try {

            if (thumbnailImage != null
                    && !thumbnailImage.isEmpty()) {

                FileUploadResponse thumbnailResponse =
                        fileService.uploadImage(
                                thumbnailImage,
                                FileType.POOJA_SERVICE_THUMBNAILS
                        );

                newThumbnailImage =
                        thumbnailResponse.getFileUrl();

                poojaService.setThumbnailImage(
                        newThumbnailImage
                );
            }

            if (bannerImage != null
                    && !bannerImage.isEmpty()) {

                FileUploadResponse bannerResponse =
                        fileService.uploadImage(
                                bannerImage,
                                FileType.POOJA_SERVICE_BANNERS
                        );

                newBannerImage =
                        bannerResponse.getFileUrl();

                poojaService.setBannerImage(
                        newBannerImage
                );
            }

            poojaService.setServiceName(
                    request.getServiceName()
            );

            poojaService.setSlug(
                    request.getSlug()
            );

            poojaService.setCategory(
                    category
            );

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
                    Boolean.TRUE.equals(request.getFeatured())
            );

            poojaService.setCancellationAllowed(
                    Boolean.TRUE.equals(request.getCancellationAllowed())
            );

            poojaService.setRefundAllowed(
                    Boolean.TRUE.equals(request.getRefundAllowed())
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

            poojaService.getPackages().clear();

            poojaService.getLanguages().clear();

            poojaService.getCommunities().clear();

            poojaService.getLocations().clear();

            poojaServicesRepository.flush();

            buildPackages(
                    request,
                    poojaService
            );

            buildLanguages(
                    request,
                    poojaService
            );

            buildCommunities(
                    request,
                    poojaService
            );

            buildCities(
                    request,
                    poojaService
            );

            poojaServicesRepository.saveAndFlush(
                    poojaService
            );

            if (newThumbnailImage != null
                    && oldThumbnailImage != null) {

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

            if (newBannerImage != null
                    && oldBannerImage != null) {

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

        } catch (Exception ex) {

            if (newThumbnailImage != null) {

                try {

                    fileService.deleteImage(
                            newThumbnailImage
                    );

                } catch (Exception ignored) {
                }
            }

            if (newBannerImage != null) {

                try {

                    fileService.deleteImage(
                            newBannerImage
                    );

                } catch (Exception ignored) {
                }
            }

            throw ex;
        }
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

                    BigDecimal startingPrice = null;

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

                        ServicePackage platinumPackage =
                                service.getPackages()
                                        .stream()
                                        .filter(pkg ->
                                                pkg.getPackageType()
                                                        == PackageType.PLATINUM
                                        )
                                        .findFirst()
                                        .orElse(null);

                        if (classicPackage != null) {
                            startingPrice =classicPackage.getPrice();
                        } else if (platinumPackage != null){
                            startingPrice = platinumPackage.getPrice();
                        }
                        else{
                            startingPrice = BigDecimal.ZERO;
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
                            .shortDescription(service.getShortDescription())
                            .startingPrice(startingPrice)
                            .featured(service.getFeatured())
                            .status(service.getStatus())
                            .build();
                })
                .toList();
    }
}