package com.example.sreepooja.Service.Priests;

import com.example.sreepooja.DTO.Request.Priests.CreatePriestRequest;
import com.example.sreepooja.DTO.Request.Priests.PriestFilterRequest;
import com.example.sreepooja.DTO.Response.Priests.PriestDetailsResponse;
import com.example.sreepooja.DTO.Response.Priests.PriestResponse;
import com.example.sreepooja.Entity.Masters.*;
import com.example.sreepooja.Entity.Priests.Priest;
import com.example.sreepooja.Entity.Priests.PriestLanguageMapping;
import com.example.sreepooja.Entity.UserRole;
import com.example.sreepooja.Entity.Users;
import com.example.sreepooja.Enum.UserRoles;
import com.example.sreepooja.Enum.UserStatus;
import com.example.sreepooja.ExceptionHandlers.BadRequestException;
import com.example.sreepooja.ExceptionHandlers.ResourceNotFoundException;
import com.example.sreepooja.Repository.Masters.*;
import com.example.sreepooja.Repository.Priests.PriestRepository;
import com.example.sreepooja.Repository.Users.UserRoleRepository;
import com.example.sreepooja.Repository.Users.UsersRepository;
import com.example.sreepooja.Specification.PriestSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PriestServiceImpl implements PriestService {

    private final PriestRepository priestRepository;
    private final UsersRepository usersRepository;
    private final UserRoleRepository userRoleRepository;
    private final StateRepository stateRepository;
    private final CityRepository cityRepository;
    private final CityPincodeRepository cityPincodeRepository;
    private final LanguageRepository languageRepository;
    private final CommunityRepository communityRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public PriestResponse createPriest(
            CreatePriestRequest request
    ) {

        if (usersRepository.existsByMobileNo(
                request.getMobileNumber()
        )) {

            throw new BadRequestException(
                    "Mobile number already exists"
            );
        }

        if (priestRepository
                .existsByWhatsappNumber(request.getMobileNumber()
        )) {

            throw new BadRequestException(
                    "Mobile number belongs to another User"
            );
        }

        if (request.getWhatsappNumber() != null
                &&
                priestRepository
                        .existsByWhatsappNumber(
                                request.getWhatsappNumber()
                        )) {

            throw new BadRequestException(
                    "WhatsApp number already exists"
            );
        }

        if (request.getWhatsappNumber() != null
                &&
                usersRepository.existsByMobileNo(
                        request.getWhatsappNumber()
                )) {

            throw new BadRequestException(
                    "WhatsApp number already belongs to another user"
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

        // Check emailId
        if (request.getEmail() != null &&
                usersRepository.findByEmail(request.getEmail()).isPresent()) {

            throw new BadRequestException(
                    "Email ID already exists."
            );
        }

//        if (request.getPassword() == null || request.getPassword().isBlank()) {
//            throw new BadRequestException("Password field cannot be empty");
//        }

        Community community =
                communityRepository
                        .findById(
                                request.getCommunityId()
                        )
                        .orElseThrow(
                                () ->
                                        new ResourceNotFoundException(
                                                "Community not found"
                                        )
                        );

        State state =
                stateRepository
                        .findByIdAndActiveTrue(
                                request.getStateId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "State not found"
                                )
                        );

        City city =
                cityRepository
                        .findByIdAndActiveTrue(
                                request.getCityId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "City not found"
                                )
                        );

        if (!city.getState().getId().equals(state.getId())) {

            throw new BadRequestException(
                    "City does not belong to selected state"
            );
        }

        CityPincode pincode =
                cityPincodeRepository
                        .findByIdAndActiveTrue(
                                request.getPincodeId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Pincode not found"
                                )
                        );

        if (!pincode.getCity().getId().equals(city.getId())) {

            throw new BadRequestException(
                    "Pincode does not belong to selected city"
            );
        }

        /*
         * Create User
         */
        Users user = new Users();

        user.setFirstName(
                request.getFirstName()
        );

        user.setLastName(
                request.getLastName()
        );

        user.setMobileNo(
                request.getMobileNumber()
        );

//        user.setPassword(
//                passwordEncoder.encode(
//                        request.getPassword()
//                )
//        );

        user.setEmail(
                request.getEmail()
        );

        user.setDob(
                request.getDob()
        );

        user.setStatus(
                UserStatus.ACTIVE
        );

        usersRepository.save(
                user
        );

        UserRole userRole =
                new UserRole();

        userRole.setRole(
                UserRoles.PRIEST
        );

        userRole.setUser(
                user
        );

        userRoleRepository.save(
                userRole
        );

        /*
         * Create Priest
         */
        Priest priest =
                Priest.builder()

                        .user(
                                user
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

                        .whatsappNumber(
                                request.getWhatsappNumber()
                        )

                        .addressLine1(
                                request.getAddressLine1()
                        )

                        .addressLine2(
                                request.getAddressLine2()
                        )

                        .state(state)

                        .city(city)

                        .pincode(pincode)

                        .community(
                                community
                        )

                        .experience(
                                request.getExperience()
                        )

                        .referredBy(
                                request.getReferredBy()
                        )

                        .active(
                                request.getActive() != null
                                        ? request.getActive()
                                        : true
                        )

                        .build();

        for (Long languageId : request.getLanguageIds()) {

            Language language =
                    languageRepository
                            .findByIdAndActiveTrue(languageId)
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Language not found"
                                    )
                            );

            PriestLanguageMapping mapping =
                    PriestLanguageMapping
                            .builder()
                            .priest(priest)
                            .language(language)
                            .build();

            priest.getLanguages().add(mapping);
        }

        priestRepository.save(
                priest
        );

        return PriestResponse
                .builder()

                .priestId(
                        priest.getId()
                )

                .firstName(
                        user.getFirstName()
                )

                .lastName(
                        user.getLastName()
                )

                .mobileNumber(
                        user.getMobileNo()
                )

                .whatsappNumber(
                        priest.getWhatsappNumber()
                )

                .city(
                        priest.getCity()
                                .getCityName()
                )

                .state(
                        priest.getState()
                                .getStateName()
                )

                .languages(
                        priest.getLanguages()
                                .stream()
                                .map(mapping ->
                                        mapping.getLanguage()
                                                .getLanguageName()
                                )
                                .toList()
                )

                .communityId(
                        community.getId()
                )

                .communityName(
                        community.getCommunityName()
                )

                .experience(
                        priest.getExperience()
                )

                .active(
                        priest.getActive()
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
                        Sort.by("createdAt")
                                .descending()
                );

        Specification<Priest> specification =
                PriestSpecification
                        .filterPriests(

                                request.getActive(),

                                request.getName(),

                                request.getMobileNumber(),

                                request.getCommunityId(),

                                request.getExperience(),

                                request.getLanguageId(),

                                request.getCityId()
                        );

        Page<Priest> priests =
                priestRepository
                        .findAll(
                                specification,
                                pageable
                        );

        return priests.map(priest -> {

            Users user =
                    priest.getUser();

            return PriestResponse
                    .builder()

                    .priestId(
                            priest.getId()
                    )

                    .firstName(
                            user.getFirstName()
                    )

                    .lastName(
                            user.getLastName()
                    )

                    .mobileNumber(
                            user.getMobileNo()
                    )

                    .whatsappNumber(
                            priest.getWhatsappNumber()
                    )

                    .city(
                            priest.getCity()
                                    .getCityName()
                    )

                    .state(
                            priest.getState().getStateName()
                    )

                    .languages(
                            priest.getLanguages()
                                    .stream()
                                    .map(mapping ->
                                            mapping.getLanguage()
                                                    .getLanguageName()
                                    )
                                    .toList()
                    )

                    .communityId(
                            priest.getCommunity()
                                    .getId()
                    )

                    .communityName(
                            priest.getCommunity()
                                    .getCommunityName()
                    )

                    .experience(
                            priest.getExperience()
                    )

                    .active(
                            priest.getActive()
                    )

                    .build();
        });
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

        Users user =
                priest.getUser();

        return PriestDetailsResponse
                .builder()

                .priestId(
                        priest.getId()
                )

                .firstName(
                        user.getFirstName()
                )

                .lastName(
                        user.getLastName()
                )

                .dob(
                        user.getDob()
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
                        user.getMobileNo()
                )

                .whatsappNumber(
                        priest.getWhatsappNumber()
                )

                .email(
                        user.getEmail()
                )

                .addressLine1(
                        priest.getAddressLine1()
                )

                .addressLine2(
                        priest.getAddressLine2()
                )

                .city(
                        priest.getCity().getCityName()
                )

                .state(
                        priest.getState().getStateName()
                )

                .pincode(
                        priest.getPincode().getPincode()
                )

                .languages(
                        priest.getLanguages()
                                .stream()
                                .map(mapping ->
                                        mapping.getLanguage()
                                                .getLanguageName()
                                )
                                .toList()
                )

                .communityId(
                        priest.getCommunity()
                                .getId()
                )

                .communityName(
                        priest.getCommunity()
                                .getCommunityName()
                )

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

        Users user =
                priest.getUser();

        if (!user.getMobileNo()
                .equals(
                        request.getMobileNumber()
                )) {

            if (usersRepository.existsByMobileNo(
                    request.getMobileNumber()
            )) {

                throw new BadRequestException(
                        "Mobile number already exists"
                );
            }

            if (priestRepository
                    .existsByWhatsappNumber(
                            request.getMobileNumber()
                    )) {

                throw new BadRequestException(
                        "Mobile number already exists"
                );
            }
        }

        if (!Objects.equals(
                priest.getWhatsappNumber(),
                request.getWhatsappNumber()
        )) {

            if (usersRepository.existsByMobileNo(
                    request.getWhatsappNumber()
            )) {

                throw new BadRequestException(
                        "WhatsApp number already exists"
                );
            }

            if (priestRepository
                    .existsByWhatsappNumberAndIdNot(
                            request.getWhatsappNumber(),
                            priestId
                    )) {

                throw new BadRequestException(
                        "WhatsApp number already exists"
                );
            }
        }

        if (!priest.getAadhaarNumber()
                .equals(
                        request.getAadhaarNumber()
                )
                &&
                priestRepository
                        .existsByAadhaarNumber(
                                request.getAadhaarNumber()
                        )) {

            throw new BadRequestException(
                    "Aadhaar number already exists"
            );
        }

        // Check emailId
        if (!Objects.equals(user.getEmail(), request.getEmail())
                &&
                request.getEmail() != null
                &&
                usersRepository.findByEmail(request.getEmail()).isPresent()) {

            throw new BadRequestException(
                    "Email ID already exists."
            );
        }

        user.setFirstName(
                request.getFirstName()
        );

        user.setLastName(
                request.getLastName()
        );

        user.setMobileNo(
                request.getMobileNumber()
        );

        user.setEmail(
                request.getEmail()
        );

        user.setDob(
                request.getDob()
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

        priest.setWhatsappNumber(
                request.getWhatsappNumber()
        );

        priest.setAddressLine1(
                request.getAddressLine1()
        );

        priest.setAddressLine2(
                request.getAddressLine2()
        );

        State state =
                stateRepository
                        .findByIdAndActiveTrue(
                                request.getStateId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "State not found"
                                )
                        );

        City city =
                cityRepository
                        .findByIdAndActiveTrue(
                                request.getCityId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "City not found"
                                )
                        );

        if (!city.getState().getId().equals(state.getId())) {

            throw new BadRequestException(
                    "City does not belong to selected state"
            );
        }

        CityPincode pincode =
                cityPincodeRepository
                        .findByIdAndActiveTrue(
                                request.getPincodeId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Pincode not found"
                                )
                        );

        if (!pincode.getCity().getId().equals(city.getId())) {

            throw new BadRequestException(
                    "Pincode does not belong to selected city"
            );
        }

        priest.setState(state);
        priest.setCity(city);
        priest.setPincode(pincode);

        priest.getLanguages().clear();

        priestRepository.flush();

        for (Long languageId : request.getLanguageIds()) {

            Language language =
                    languageRepository
                            .findByIdAndActiveTrue(languageId)
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Language not found"
                                    )
                            );

            PriestLanguageMapping mapping =
                    PriestLanguageMapping
                            .builder()
                            .priest(priest)
                            .language(language)
                            .build();

            priest.getLanguages().add(mapping);
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

        priest.setCommunity(community);

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
                        user.getFirstName()
                )

                .lastName(
                        user.getLastName()
                )

                .mobileNumber(
                        user.getMobileNo()
                )

                .whatsappNumber(
                        priest.getWhatsappNumber()
                )

                .city(
                        priest.getCity()
                                .getCityName()
                )

                .state(
                        priest.getState()
                                .getStateName()
                )

                .languages(
                        priest.getLanguages()
                                .stream()
                                .map(mapping ->
                                        mapping.getLanguage()
                                                .getLanguageName()
                                )
                                .toList()
                )

                .communityId(
                        priest.getCommunity().getId()
                )

                .communityName(
                        priest.getCommunity()
                                .getCommunityName()
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
