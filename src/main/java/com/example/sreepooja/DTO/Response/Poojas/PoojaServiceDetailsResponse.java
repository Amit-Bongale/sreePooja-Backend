package com.example.sreepooja.DTO.Response.Poojas;

import com.example.sreepooja.Enum.Poojas.ServiceStatus;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PoojaServiceDetailsResponse {

    private Long id;

    private String serviceName;

    private String slug;

    private String categorySlug;

    private String shortDescription;

    private String fullDescription;

    private String benefits;

    private Integer durationMinutes;

    private ServiceStatus status;

    private Boolean featured;

    private Boolean cancellationAllowed = true;

    private Boolean refundAllowed = true;

    private String metaTitle;

    private String metaDescription;

    private String metaKeywords;

    private String thumbnailImage;

    private String bannerImage;

    private List<String> languages;

    private List<String> communities;

    private List<String> cities;

    private List<ServicePackageResponse> packages;

}