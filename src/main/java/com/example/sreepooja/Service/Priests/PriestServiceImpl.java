package com.example.sreepooja.Service.Priests;

import com.example.sreepooja.DTO.Request.Priests.CreatePriestRequest;
import com.example.sreepooja.DTO.Response.Priests.PriestResponse;
import com.example.sreepooja.Entity.Priests.Priest;
import com.example.sreepooja.ExceptionHandlers.BadRequestException;
import com.example.sreepooja.Repository.Priests.PriestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PriestServiceImpl implements PriestService {

    private final PriestRepository priestRepository;

    @Override
    @Transactional
    public PriestResponse createPriest(
            CreatePriestRequest request
    ) {

        if (priestRepository
                .existsByMobileNumber(
                        request.getMobileNumber()
                )) {

            throw new BadRequestException(
                    "Mobile number already exists"
            );
        }

        if (priestRepository
                .existsByAadhaarNumber(
                        request.getAadhaarNumber()
                )) {

            throw new BadRequestException(
                    "Aadhaar number already exists"
            );
        }

        Priest priest =
                Priest.builder()

                        .firstName(
                                request.getFirstName()
                        )

                        .lastName(
                                request.getLastName()
                        )

                        .age(
                                request.getAge()
                        )

                        .gothra(
                                request.getGothra()
                        )

                        .pravara(
                                request.getPravara()
                        )

                        .nativePlace(
                                request.getNativePlace()
                        )

                        .aadhaarNumber(
                                request.getAadhaarNumber()
                        )

                        .mobileNumber(
                                request.getMobileNumber()
                        )

                        .whatsappNumber(
                                request.getWhatsappNumber()
                        )

                        .email(
                                request.getEmail()
                        )

                        .addressLine1(
                                request.getAddressLine1()
                        )

                        .addressLine2(
                                request.getAddressLine2()
                        )

                        .place(
                                request.getPlace()
                        )

                        .pincode(
                                request.getPincode()
                        )

                        .languagesSpoken(
                                request.getLanguagesSpoken()
                        )

                        .trimathastharu(
                                request.getTrimathastharu()
                        )

                        .experience(
                                request.getExperience()
                        )

                        .referredBy(
                                request.getReferredBy()
                        )

                        .active(
                                true
                        )

                        .build();

        priestRepository.save(
                priest
        );

        return PriestResponse
                .builder()

                .priestId(
                        priest.getId()
                )

                .firstName(
                        priest.getFirstName()
                )

                .lastName(
                        priest.getLastName()
                )

                .mobileNumber(
                        priest.getMobileNumber()
                )

                .whatsappNumber(
                        priest.getWhatsappNumber()
                )

                .place(
                        priest.getPlace()
                )

                .languagesSpoken(
                        priest.getLanguagesSpoken()
                )

                .trimathastharu(
                        priest.getTrimathastharu()
                )

                .experience(
                        priest.getExperience()
                )

                .active(
                        priest.getActive()
                )

                .build();
    }
}
