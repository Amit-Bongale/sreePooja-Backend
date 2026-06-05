package com.example.sreepooja.Service.Bookings;

import com.example.sreepooja.DTO.Request.Bookings.CreateBookingRequest;
import com.example.sreepooja.DTO.Response.Bookings.CheckoutResponse;
import com.example.sreepooja.DTO.Response.Bookings.CreateBookingResponse;
import com.example.sreepooja.Entity.Bookings.Booking;
import com.example.sreepooja.Entity.Masters.City;
import com.example.sreepooja.Entity.Masters.CityPincode;
import com.example.sreepooja.Entity.Masters.State;
import com.example.sreepooja.Entity.Poojas.PoojaServices;
import com.example.sreepooja.Entity.Poojas.ServicePackage;
import com.example.sreepooja.Entity.Users;
import com.example.sreepooja.Enum.Bookings.BookingStatus;
import com.example.sreepooja.Enum.Bookings.PaymentOption;
import com.example.sreepooja.Enum.Bookings.PaymentStatus;
import com.example.sreepooja.Enum.Poojas.ServiceStatus;
import com.example.sreepooja.ExceptionHandlers.BadRequestException;
import com.example.sreepooja.ExceptionHandlers.ResourceNotFoundException;
import com.example.sreepooja.Repository.Bookings.BookingRepository;
import com.example.sreepooja.Repository.Masters.CityPincodeRepository;
import com.example.sreepooja.Repository.Masters.CityRepository;
import com.example.sreepooja.Repository.Masters.StateRepository;
import com.example.sreepooja.Repository.Poojas.ServicePackageRepository;
import com.example.sreepooja.Repository.Users.UsersRepository;
import com.example.sreepooja.Service.CustomUserDetails.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    private final ServicePackageRepository servicePackageRepository;

    private final StateRepository stateRepository;

    private final CityRepository cityRepository;

    private final CityPincodeRepository cityPincodeRepository;

    private final UsersRepository usersRepository;

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

    @Transactional
    @Override
    public CreateBookingResponse createBooking(
            CreateBookingRequest request
    ) {

        ServicePackage servicePackage =
                servicePackageRepository
                        .findById(request.getPackageId())
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Package not found"
                                )
                        );

        if (servicePackage.getStatus() != ServiceStatus.ACTIVE) {

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

        if (service.getStatus() != ServiceStatus.ACTIVE) {

            throw new BadRequestException(
                    "Pooja service is not active"
            );
        }

        State state =
                stateRepository
                        .findByIdAndActiveTrue(
                                request.getStateId()
                        )
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "State not found"
                                )
                        );

        City city =
                cityRepository
                        .findByIdAndActiveTrue(
                                request.getCityId()
                        )
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "City not found"
                                )
                        );

        if (!city.getState().getId()
                .equals(state.getId())) {

            throw new BadRequestException(
                    "City does not belong to selected state"
            );
        }

        CityPincode pincode =
                cityPincodeRepository
                        .findByIdAndActiveTrue(
                                request.getPincodeId()
                        )
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Pincode not found"
                                )
                        );

        if (!pincode.getCity().getId()
                .equals(city.getId())) {

            throw new BadRequestException(
                    "Pincode does not belong to selected city"
            );
        }

        CustomUserDetails userDetails =
                (CustomUserDetails)
                        SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getPrincipal();

        Users user =
                usersRepository
                        .findById(userDetails.getUserId())
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "User not found"
                                )
                        );

        BigDecimal packagePrice =
                servicePackage.getPrice();

        BigDecimal advancePercentage =
                servicePackage.getAdvancePercentage();

        BigDecimal advanceAmount =
                packagePrice
                        .multiply(advancePercentage)
                        .divide(
                                BigDecimal.valueOf(100),
                                2,
                                RoundingMode.HALF_UP
                        );

        BigDecimal balanceAmount =
                packagePrice.subtract(advanceAmount);

        BigDecimal taxAmount =
                BigDecimal.ZERO;

        BigDecimal totalAmount =
                packagePrice.add(taxAmount);

        Booking booking =
                Booking.builder()
                        .bookingNumber("TEMP")
                        .user(user)
                        .service(service)
                        .selectedPackage(servicePackage)
                        .preferredDate(
                request.getPreferredDate())
                        .preferredTimeSlot(
                request.getPreferredTimeSlot())
                        .preferredLanguage(
                request.getPreferredLanguage())
                        .preferredCommunity(
                request.getPreferredCommunity()
        )

                        .address(
                request.getAddress()
        )

                        .state(state)

                        .city(city)

                        .pincode(pincode)

                        .specialInstructions(
                request.getSpecialInstructions()
        )

                        .packagePrice(packagePrice)

                        .taxAmount(taxAmount)

                        .totalAmount(totalAmount)

                        .advancePercentage(
                advancePercentage
        )

                        .advanceAmount(
                advanceAmount
        )

                        .balanceAmount(
                balanceAmount
        )

                        .bookingStatus(
                BookingStatus.PENDING_PAYMENT
                        )

                        .paymentStatus(PaymentStatus.PENDING)
                        .build();
        Booking savedBooking =
                bookingRepository.save(booking);

        savedBooking.setBookingNumber(
                "SP" + savedBooking.getId()
        );

        savedBooking =
                bookingRepository.save(savedBooking);

        BigDecimal amountToPay;

        if (request.getPaymentOption()
                == PaymentOption.ADVANCE) {

            amountToPay = advanceAmount;

        } else {

            amountToPay = totalAmount;
        }

        return CreateBookingResponse.builder()
                .bookingId(
                        savedBooking.getId()
                )
                .bookingNumber(
                        savedBooking.getBookingNumber()
                )
                .bookingStatus(
                        savedBooking.getBookingStatus()
                )
                .paymentStatus(savedBooking.getPaymentStatus())
                .amountToPay(
                        amountToPay
                )
                .build();
    }
}
