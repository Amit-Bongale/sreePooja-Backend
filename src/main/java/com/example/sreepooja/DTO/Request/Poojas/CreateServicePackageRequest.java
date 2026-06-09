package com.example.sreepooja.DTO.Request.Poojas;

import com.example.sreepooja.Enum.Poojas.PackageType;
import com.example.sreepooja.Enum.Poojas.ServiceStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateServicePackageRequest {

    @NotNull(message = "Package type is required")
    private PackageType packageType;

    private String shortDescription;

    private String includedItems;

    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal price;

    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal advancePercentage;

    @NotNull(message = "Status is required")
    private ServiceStatus status;
}