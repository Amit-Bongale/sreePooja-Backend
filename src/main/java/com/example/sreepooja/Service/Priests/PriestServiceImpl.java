package com.example.sreepooja.Service.Priests;

import com.example.sreepooja.DTO.Request.Priests.CreatePriestRequest;
import com.example.sreepooja.DTO.Request.Priests.PriestFilterRequest;
import com.example.sreepooja.DTO.Response.Priests.PriestDetailsResponse;
import com.example.sreepooja.DTO.Response.Priests.PriestResponse;
import com.example.sreepooja.Entity.Masters.Community;
import com.example.sreepooja.Entity.Priests.Priest;
import com.example.sreepooja.ExceptionHandlers.BadRequestException;
import com.example.sreepooja.ExceptionHandlers.ResourceNotFoundException;
import com.example.sreepooja.Repository.Masters.CityRepository;
import com.example.sreepooja.Repository.Masters.CommunityRepository;
import com.example.sreepooja.Repository.Masters.LanguageRepository;
import com.example.sreepooja.Repository.Priests.PriestRepository;
import com.example.sreepooja.Repository.Users.UsersRepository;
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
    private final UsersRepository usersRepository;
    private final LanguageRepository languageRepository;
    private final CityRepository cityRepository;
    private final CommunityRepository communityRepository;

    @Override
    @Transactional
    public PriestResponse createPriest(
            CreatePriestRequest request
    ) {
        if (priestRepository.existsDuplicateNumbers(
                request.getMobileNumber(),
                request.getWhatsappNumber()
        )) {

            throw new BadRequestException(
                    "Mobile number or WhatsApp number already exists"
            );
        }

        if (usersRepository.existsDuplicateNumbers(
                request.getMobileNumber(),
                request.getWhatsappNumber()
        )) {

            throw new BadRequestException(
                    "Mobile number or WhatsApp number already belongs to a user"
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

        Community community =
                communityRepository
                        .findById(
                                request.getCommunityId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Community not found"
                                )
                        );

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

                        .community(community)

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

                                request.getName(),

                                request.getMobileNumber(),

                                request.getCommunityId(),

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

                        .communityId(
                                priest.getCommunity().getId()
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

    @Override
    public PriestDetailsResponse getPriestById(
            Long priestId
    ) {

        Priest priest =
                priestRepository
                        .findById(priestId)
                        .orElseThrow(
                                () ->
                                        new ResourceNotFoundException(
                                                "Priest not found"
                                        )
                        );

        return PriestDetailsResponse
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

                .age(
                        priest.getAge()
                )

                .gothra(
                        priest.getGothra()
                )

                .pravara(
                        priest.getPravara()
                )

                .nativePlace(
                        priest.getNativePlace()
                )

                .aadhaarNumber(
                        priest.getAadhaarNumber()
                )

                .mobileNumber(
                        priest.getMobileNumber()
                )

                .whatsappNumber(
                        priest.getWhatsappNumber()
                )

                .email(
                        priest.getEmail()
                )

                .addressLine1(
                        priest.getAddressLine1()
                )

                .addressLine2(
                        priest.getAddressLine2()
                )

                .city(
                        priest.getCity()
                )

                .pincode(
                        priest.getPincode()
                )

                .languagesSpoken(
                        priest.getLanguagesSpoken()
                )

                .communityId(priest.getCommunity().getId())

                .communityName(priest.getCommunity().getCommunityName())

                .experience(
                        priest.getExperience()
                )

                .referredBy(
                        priest.getReferredBy()
                )

                .active(
                        priest.getActive()
                )

                .createdAt(
                        priest.getCreatedAt()
                )

                .build();
    }

    @Override
    @Transactional
    public PriestResponse updatePriest(

            Long priestId,

            CreatePriestRequest request
    ) {

        Priest priest =
                priestRepository
                        .findById(priestId)
                        .orElseThrow(
                                () ->
                                        new ResourceNotFoundException(
                                                "Priest not found"
                                        )
                        );

        if (priestRepository.existsDuplicateNumbers(
                priestId,
                request.getMobileNumber(),
                request.getWhatsappNumber()
        )) {
            throw new BadRequestException(
                    "Mobile number or WhatsApp number already exists"
            );
        }

        if (usersRepository.existsDuplicateNumbers(
                request.getMobileNumber(),
                request.getWhatsappNumber()
        )) {
            throw new BadRequestException(
                    "Mobile number or WhatsApp number already belongs to a user"
            );
        }

        if (!priest.getAadhaarNumber()
                .equals(request.getAadhaarNumber())
                && priestRepository
                .existsByAadhaarNumber(
                        request.getAadhaarNumber()
                )) {

            throw new BadRequestException(
                    "Aadhaar number already exists"
            );
        }

        Community community =
                communityRepository
                        .findById(
                                request.getCommunityId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Community not found"
                                )
                        );

        priest.setFirstName(
                request.getFirstName()
        );

        priest.setLastName(
                request.getLastName()
        );

        priest.setAge(
                request.getAge()
        );

        priest.setGothra(
                request.getGothra()
        );

        priest.setPravara(
                request.getPravara()
        );

        priest.setNativePlace(
                request.getNativePlace()
        );

        priest.setAadhaarNumber(
                request.getAadhaarNumber()
        );

        priest.setMobileNumber(
                request.getMobileNumber()
        );

        priest.setWhatsappNumber(
                request.getWhatsappNumber()
        );

        priest.setEmail(
                request.getEmail()
        );

        priest.setAddressLine1(
                request.getAddressLine1()
        );

        priest.setAddressLine2(
                request.getAddressLine2()
        );

        priest.setCity(
                request.getCity()
        );

        priest.setPincode(
                request.getPincode()
        );

        priest.setLanguagesSpoken(
                StringCommaUtil
                        .normalizeCommaSeparatedValues(
                                request.getLanguagesSpoken()
                        )
        );

        priest.setCommunity(
                community
        );

        priest.setExperience(
                request.getExperience()
        );

        priest.setReferredBy(
                request.getReferredBy()
        );

        priest.setActive(
                request.getActive()
        );

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

                .city(
                        priest.getCity()
                )

                .languagesSpoken(
                        priest.getLanguagesSpoken()
                )

                .communityId(priest.getCommunity().getId())

                .experience(
                        priest.getExperience()
                )

                .active(
                        priest.getActive()
                )

                .build();
    }
}
