package com.example.sreepooja.DTO.Poojas.Response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PoojaServiceCardResponse {

    private Long id;

    private String serviceName;

    private String slug;

    private String categoryName;

    private Integer durationMinutes;

    private String thumbnailImage;

    private String startingPrice;
}