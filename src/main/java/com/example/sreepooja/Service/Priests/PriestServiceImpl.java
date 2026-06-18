package com.example.sreepooja.Service.Priests;

import com.example.sreepooja.DTO.Request.Priests.CreatePriestRequest;
import com.example.sreepooja.DTO.Request.Priests.PriestFilterRequest;
import com.example.sreepooja.DTO.Response.Priests.PriestResponse;
import com.example.sreepooja.Entity.Priests.Priest;
import com.example.sreepooja.ExceptionHandlers.BadRequestException;
import com.example.sreepooja.ExceptionHandlers.ResourceNotFoundException;
import com.example.sreepooja.Repository.Masters.CityRepository;
import com.example.sreepooja.Repository.Masters.LanguageRepository;
import com.example.sreepooja.Repository.Priests.PriestRepository;
import com.example.sreepooja.Specification.PriestSpecification;
import com.example.sreepooja.Utility.StringCommaUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PriestServiceImpl implements PriestService {

    private final PriestRepository priestRepository;
    private final LanguageRepository languageRepository;
    private final CityRepository cityRepository;

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

                        .city(
                                request.getCity()
                        )

                        .state(
                                request.getState()
                        )

                        .pincode(
                                request.getPincode()
                        )

                        .languagesSpoken(
                        StringCommaUtil.normalizeCommaSeparatedValues(
                                        request.getLanguagesSpoken()
                                )
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

                .mobileNumber(
                        priest.getMobileNumber()
                )

                .build();
    }

    @Override
    public Page<PriestResponse> getAllPriests(

            PriestFilterRequest request,

            int page,

            int size
    ) {

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by("firstName")
                                .ascending()
                );

        String languageName = null;

        if (request.getLanguageId() != null) {

            languageName =
                    languageRepository
                            .findById(
                                    request.getLanguageId()
                            )
                            .orElseThrow(
                                    () ->
                                            new ResourceNotFoundException(
                                                    "Language not found"
                                            )
                            )
                            .getLanguageName();
        }

        String cityName = null;

        if (request.getCityId() != null) {

            cityName =
                    cityRepository
                            .findById(
                                    request.getCityId()
                            )
                            .orElseThrow(
                                    () ->
                                            new ResourceNotFoundException(
                                                    "City not found"
                                            )
                            )
                            .getCityName();
        }

        Specification<Priest> specification =
                PriestSpecification
                        .filterPriests(

                                request.getActive(),

                                request.getMobileNumber(),

                                request.getTrimathastharu(),

                                request.getExperience(),

                                languageName,

                                cityName
                        );

        Page<Priest> priests =
                priestRepository
                        .findAll(
                                specification,
                                pageable
                        );

        return priests.map(priest ->
                PriestResponse.builder()

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

                        .city(
                                priest.getCity()
                        )

                        .state(
                                priest.getState()
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

                        .build()
        );
    }
}
