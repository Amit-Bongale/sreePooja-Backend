package com.example.sreepooja.Service.Bookings;

import com.example.sreepooja.DTO.Request.Bookings.CreateBookingRequest;
import com.example.sreepooja.DTO.Response.Bookings.BookingDetailsResponse;
import com.example.sreepooja.DTO.Response.Bookings.CheckoutResponse;
import com.example.sreepooja.DTO.Response.Bookings.CreateBookingResponse;
import com.example.sreepooja.DTO.Response.Bookings.MyBookingResponse;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

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
    public CheckoutResponse getCheckout(Long packageId) {
        ServicePackage servicePackage = servicePackageRepository.findById(packageId).orElseThrow(
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
                        );
        ;

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

        if (request.getPreferredDate() == null) {
            throw new BadRequestException("Preferred Date is required");
        }

        if (request.getAddress() == null || request.getAddress().isBlank()) {
            throw new BadRequestException("Address is required");
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
                        .paymentOption(request.getPaymentOption())
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

    @Override
    public Page<MyBookingResponse> getMyBookings(
            int page,
            int size
    ) {

        CustomUserDetails userDetails =
                (CustomUserDetails)
                        SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getPrincipal();

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by("createdAt")
                                .descending()
                );

        Page<Booking> bookings =
                bookingRepository
                        .findByUserId(
                                userDetails.getUserId(),
                                pageable
                        );

        return bookings.map(booking -> {

            if (!booking.getUser().getId()
                    .equals(userDetails.getUserId())) {

                throw new BadRequestException(
                        "Access denied"
                );
            }

            LocalDate effectiveDate =
                    booking.getConfirmedDate() != null
                            ? booking.getConfirmedDate()
                            : booking.getPreferredDate();

            boolean showPayBalanceButton = false;

            LocalDate balancePaymentDeadline = null;

            String paymentMessage = null;

            if (booking.getPaymentStatus()
                    == PaymentStatus.PARTIALLY_PAID) {

                showPayBalanceButton = true;

                balancePaymentDeadline =
                        effectiveDate.minusDays(3);

                paymentMessage =
                        "Please complete the remaining payment before "
                                + balancePaymentDeadline +" for booking to be valid";
            }

            return MyBookingResponse.builder()

                    .bookingId(
                            booking.getId()
                    )

                    .bookingNumber(
                            booking.getBookingNumber()
                    )

                    .serviceName(
                            booking.getService()
                                    .getServiceName()
                    )

                    .packageType(
                            booking.getSelectedPackage()
                                    .getPackageType()
                    )

                    .poojaDate(
                            effectiveDate
                    )

                        .poojaTime(
                                booking.getConfirmedTime()
                        )

                    .bookingStatus(
                            booking.getBookingStatus()
                    )

                    .paymentStatus(
                            booking.getPaymentStatus()
                    )

                    .paymentOption(
                            booking.getPaymentOption()
                    )

                    .totalAmount(
                            booking.getTotalAmount()
                    )

                    .balanceAmount(
                            booking.getBalanceAmount()
                    )

                    .showPayBalanceButton(
                            showPayBalanceButton
                    )

                    .paymentMessage(
                            paymentMessage
                    )

                    .priestName(booking.getPriestName())

                    .address(booking.getAddress())

                    .build();
        });
    }

    @Override
    public BookingDetailsResponse getBookingDetails(
            Long bookingId
    ) {

        CustomUserDetails userDetails =
                (CustomUserDetails)
                        SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getPrincipal();

        Booking booking =
                bookingRepository
                        .findById(bookingId)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Booking not found"
                                )
                        );

        if (!booking.getUser().getId()
                .equals(userDetails.getUserId())) {

            throw new BadRequestException(
                    "Access denied"
            );
        }

        boolean showPayBalanceButton = false;

        String paymentMessage = null;

        if (booking.getPaymentStatus()
                == PaymentStatus.PARTIALLY_PAID) {

            showPayBalanceButton = true;

            LocalDate effectiveDate =
                    booking.getConfirmedDate() != null
                            ? booking.getConfirmedDate()
                            : booking.getPreferredDate();

            paymentMessage =
                    "Please complete the remaining payment before "
                            + effectiveDate.minusDays(3)
                            + " for booking to be valid";
        }

        return BookingDetailsResponse.builder()

                .bookingId(
                        booking.getId()
                )

                .bookingNumber(
                        booking.getBookingNumber()
                )

                .serviceName(
                        booking.getService()
                                .getServiceName()
                )

                .packageType(
                        booking.getSelectedPackage()
                                .getPackageType()
                )

                .bookingStatus(
                        booking.getBookingStatus()
                )

                .paymentStatus(
                        booking.getPaymentStatus()
                )

                .paymentOption(
                        booking.getPaymentOption()
                )

                .preferredDate(
                        booking.getPreferredDate()
                )

                .preferredTimeSlot(
                        booking.getPreferredTimeSlot()
                )

                .confirmedDate(
                        booking.getConfirmedDate()
                )

                .confirmedTime(
                        booking.getConfirmedTime()
                )

                .priestName(
                        booking.getPriestName()
                )

                .preferredLanguage(
                        booking.getPreferredLanguage()
                )

                .preferredCommunity(
                        booking.getPreferredCommunity()
                )

                .address(
                        booking.getAddress()
                )

                .state(
                        booking.getState()
                                .getStateName()
                )

                .city(
                        booking.getCity()
                                .getCityName()
                )

                .pincode(
                        booking.getPincode()
                                .getPincode()
                )

                .specialInstructions(
                        booking.getSpecialInstructions()
                )

                .packagePrice(
                        booking.getPackagePrice()
                )

                .taxAmount(
                        booking.getTaxAmount()
                )

                .totalAmount(
                        booking.getTotalAmount()
                )

                .advancePercentage(
                        booking.getAdvancePercentage()
                )

                .advanceAmount(
                        booking.getAdvanceAmount()
                )

                .balanceAmount(
                        booking.getBalanceAmount()
                )

                .bookedAt(
                        booking.getCreatedAt()
                )

                .showPayBalanceButton(
                        showPayBalanceButton
                )

                .paymentMessage(
                        paymentMessage
                )

                .build();
    }
}
