package com.example.sreepooja.DTO.Poojas.Response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryResponse {

    private Long id;

    private String categoryName;

    private String slug;
}