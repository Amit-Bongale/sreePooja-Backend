package com.example.sreepooja.DTO.Poojas.Response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PoojaServiceDetailsResponse {

    private Long id;

    private String serviceName;

    private String slug;

    private String categoryName;

    private String shortDescription;

    private String fullDescription;

    private String benefits;

    private Integer durationMinutes;

    private String thumbnailImage;

    private String bannerImage;

    private List<String> languages;

    private List<String> communities;

    private List<String> cities;

    private List<ServicePackageResponse> packages;
}