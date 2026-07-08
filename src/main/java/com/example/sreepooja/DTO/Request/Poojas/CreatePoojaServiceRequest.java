package com.example.sreepooja.DTO.Request.Poojas;

import com.example.sreepooja.Enum.Poojas.ServiceStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class    CreatePoojaServiceRequest {

    @NotBlank(message = "Service name is required")
    private String serviceName;

    @NotBlank(message = "Slug is required")
    private String slug;

    private String categorySlug;

    private String shortDescription;

    private String fullDescription;

    private String benefits;

    private Integer durationMinutes;

    private ServiceStatus status;

    private Boolean featured = false;

    private Boolean cancellationAllowed = true;

    private Boolean refundAllowed = true;

    private String metaTitle;

    private String metaDescription;

    private String metaKeywords;

    private List<Long> languageIds;

    private List<Long> communityIds;

    private List<Long> cityIds;

    @Valid
    private List<CreateServicePackageRequest> packages;

    private Boolean enableCustomPackage;
}