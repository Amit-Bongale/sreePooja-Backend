package com.example.sreepooja.Service.Bookings;

import com.example.sreepooja.DTO.Response.Bookings.CheckoutResponse;
import com.example.sreepooja.Entity.Poojas.PoojaServices;
import com.example.sreepooja.Entity.Poojas.ServicePackage;
import com.example.sreepooja.Enum.Poojas.ServiceStatus;
import com.example.sreepooja.ExceptionHandlers.BadRequestException;
import com.example.sreepooja.ExceptionHandlers.ResourceNotFoundException;
import com.example.sreepooja.Repository.Poojas.ServicePackageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final ServicePackageRepository servicePackageRepository;

    @Override
    public CheckoutResponse getCheckout(Long packageId){
        ServicePackage servicePackage= servicePackageRepository.findById(packageId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Package not found"
                )
        );

        if(servicePackage.getStatus() != ServiceStatus.ACTIVE) {

            throw new BadRequestException(
                    "Package is not active"
            );
        }

        PoojaServices service =
                servicePackage.getPoojaService();

        if (service == null) {

            throw new BadRequestException(
                    "Package is not mapped to any service"
            );
        }

        if (service.getStatus() != ServiceStatus.ACTIVE){
            throw new BadRequestException("Pooja Service is not active");
        }

        BigDecimal packagePrice =
                servicePackage.getPrice();

        BigDecimal advanceAmount =
                packagePrice
                        .multiply(
                                servicePackage
                                        .getAdvancePercentage()
                        )
                        .divide(
                                BigDecimal.valueOf(100),
                                2,
                                RoundingMode.HALF_UP
                        );;

        BigDecimal balanceAmount =
                packagePrice.subtract(
                        advanceAmount
                );

        return CheckoutResponse.builder()

                .packageId(
                        servicePackage.getId()
                )

                .serviceName(
                        service.getServiceName()
                )

                .shortDescription(
                        servicePackage.getShortDescription()
                )

                .packageType(
                        servicePackage.getPackageType()
                )

                .packagePrice(
                        packagePrice
                )

                .advancePercentage(
                        servicePackage.getAdvancePercentage()
                )

                .advanceAmount(
                        advanceAmount
                )

                .balanceAmount(
                        balanceAmount
                )

                .thumbnailImage(
                        service.getThumbnailImage()
                )

                .build();
    }
}
