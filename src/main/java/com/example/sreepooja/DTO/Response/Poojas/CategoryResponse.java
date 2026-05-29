package com.example.sreepooja.DTO.Response.Poojas;

import com.example.sreepooja.Enum.Poojas.ServiceStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryResponse {

    private Long id;

    private String categoryName;

    private String slug;

    private ServiceStatus status;
}