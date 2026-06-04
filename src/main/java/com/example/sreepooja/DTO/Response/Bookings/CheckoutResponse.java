package com.example.sreepooja.DTO.Response.Bookings;

import com.example.sreepooja.Enum.Poojas.PackageType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class CheckoutResponse {

    private Long packageId;

    private String serviceName;

    private String shortDescription;

    private PackageType packageType;

    private BigDecimal packagePrice;

    private BigDecimal advancePercentage;

    private BigDecimal advanceAmount;

    private BigDecimal balanceAmount;

    private String thumbnailImage;
}