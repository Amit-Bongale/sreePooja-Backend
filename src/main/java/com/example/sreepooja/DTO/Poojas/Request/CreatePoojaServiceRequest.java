package com.example.sreepooja.DTO.Poojas.Request;

import com.example.sreepooja.Enum.Poojas.ServiceStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CreatePoojaServiceRequest {

    @NotBlank(message = "Service code is required")
    private String serviceCode;

    @NotBlank(message = "Service name is required")
    private String serviceName;

    @NotBlank(message = "Slug is required")
    private String slug;

    @NotNull(message = "Category is required")
    private Long categoryId;

    private String shortDescription;

    private String fullDescription;

    private String ritualImportance;

    private String benefits;

    private Integer durationMinutes;

    private ServiceStatus status;

    private Boolean featured = false;

    private Boolean cancellationAllowed = true;

    private Boolean refundAllowed = true;

    private String metaTitle;

    private String metaDescription;

    private String metaKeywords;

    private String thumbnailImage;

    private String bannerImage;

    private List<Long> languageIds;

    private List<Long> communityIds;

    private List<Long> cityIds;

    @Valid
    private List<CreateServicePackageRequest> packages;
}