package com.example.sreepooja.DTO.Request.Poojas;

import com.example.sreepooja.Enum.Poojas.ServiceStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateCategoryRequest {

    @NotBlank(message = "Category name is required")
    private String categoryName;

    @NotBlank(message = "Slug is required")
    private String slug;

    private ServiceStatus status;
}