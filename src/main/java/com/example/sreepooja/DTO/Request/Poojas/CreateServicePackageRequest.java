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

    @NotNull(message = "Description is required")
    private String shortDescription;

    private String includedItems;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal price;

    @NotNull(message = "Advance percentage is required")
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal advancePercentage;

    private ServiceStatus status;
}