package com.example.sreepooja.DTO.Poojas.Response;

import com.example.sreepooja.Enum.Poojas.PackageType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ServicePackageResponse {

    private PackageType packageType;

    private String shortDescription;

    private String includedItems;

    private BigDecimal price;

    private BigDecimal advancePercentage;

    private Integer durationMinutes;
}