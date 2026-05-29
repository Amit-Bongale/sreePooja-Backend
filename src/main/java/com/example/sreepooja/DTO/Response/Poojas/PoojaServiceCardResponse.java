package com.example.sreepooja.DTO.Response.Poojas;

import com.example.sreepooja.Enum.Poojas.ServiceStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PoojaServiceCardResponse {

    private Long id;

    private String serviceName;

    private String slug;

    private String categorySlug;

    private Integer durationMinutes;

    private String thumbnailImage;

    private String startingPrice;

    private ServiceStatus status;

    private Boolean featured;
}