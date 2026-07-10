package com.example.sreepooja.Service.Priests;

import com.example.sreepooja.DTO.Request.Priests.CreatePriestRegistrationRequest;
import com.example.sreepooja.DTO.Response.File.FileUploadResponse;
import com.example.sreepooja.DTO.Response.Priests.PriestRegistrationCardResponse;
import com.example.sreepooja.DTO.Response.Priests.PriestRegistrationDetailsResponse;
import com.example.sreepooja.Entity.Masters.*;
import com.example.sreepooja.Entity.Priests.Priest;
import com.example.sreepooja.Entity.Priests.PriestLanguageMapping;
import com.example.sreepooja.Entity.Priests.PriestRegistration;
import com.example.sreepooja.Entity.Priests.PriestRegistrationLanguageMapping;
import com.example.sreepooja.Entity.UserRole;
import com.example.sreepooja.Entity.Users;
import com.example.sreepooja.Enum.File.FileType;
import com.example.sreepooja.Enum.Priests.PriestRegistrationStatus;
import com.example.sreepooja.Enum.UserRoles;
import com.example.sreepooja.Enum.UserStatus;
import com.example.sreepooja.ExceptionHandlers.BadRequestException;
import com.example.sreepooja.ExceptionHandlers.InvalidFileException;
import com.example.sreepooja.ExceptionHandlers.ResourceNotFoundException;
import com.example.sreepooja.Repository.Masters.*;
import com.example.sreepooja.Repository.Priests.PriestRegistrationRepository;
import com.example.sreepooja.Repository.Priests.PriestRepository;
import com.example.sreepooja.Repository.Users.UserRoleRepository;
import com.example.sreepooja.Repository.Users.UsersRepository;
import com.example.sreepooja.Service.File.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class PriestRegistrationServiceImpl
        implements PriestRegistrationService {

    private final PriestRegistrationRepository priestRegistrationRepository;

    private final UsersRepository usersRepository;

    private final PriestRepository priestRepository;

    private final CommunityRepository communityRepository;

    private final StateRepository stateRepository;

    private final CityRepository cityRepository;

    private final CityPincodeRepository cityPincodeRepository;

    private final LanguageRepository languageRepository;

    private final FileService fileService;

    private final PasswordEncoder passwordEncoder;

    private final UserRoleRepository userRoleRepository;

    @Override
    @Transactional
    public String registerPriest(
            CreatePriestRegistrationRequest request,
            MultipartFile priestPhoto,
            MultipartFile aadhaarPdf
    ) {
        if (usersRepository.existsByMobileNo(request.getMobileNumber())
                || priestRegistrationRepository.existsByMobileNumber(request.getMobileNumber())
        ||priestRegistrationRepository.existsByWhatsappNumber(request.getMobileNumber())
        ||priestRepository.existsByWhatsappNumber(request.getWhatsappNumber())) {

            throw new BadRequestException(
                    "Mobile number already exists"
            );
        }

        if (request.getWhatsappNumber() != null) {

            if (usersRepository.existsByMobileNo(request.getWhatsappNumber())
                    || priestRegistrationRepository.existsByMobileNumber(request.getWhatsappNumber())
                    || priestRepository.existsByWhatsappNumber(request.getWhatsappNumber())
                    || priestRegistrationRepository.existsByWhatsappNumber(request.getWhatsappNumber())) {

                throw new BadRequestException(
                        "WhatsApp number already exists"
                );
            }
        }

        if (priestRepository.existsByAadhaarNumber(request.getAadhaarNumber())
                || priestRegistrationRepository.existsByAadhaarNumber(request.getAadhaarNumber())) {

            throw new BadRequestException(
                    "Aadhaar number already exists"
            );
        }

        if (request.getEmail() != null &&
                usersRepository.findByEmail(request.getEmail()).isPresent()) {

            throw new BadRequestException(
                    "Email already exists"
            );
        }

        if (priestPhoto == null || priestPhoto.isEmpty()) {

            throw new InvalidFileException(
                    "Priest photo is required"
            );
        }

        if (aadhaarPdf == null || aadhaarPdf.isEmpty()) {

            throw new InvalidFileException(
                    "Aadhaar PDF is required"
            );
        }

        Community community =
                communityRepository
                        .findById(request.getCommunityId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Community not found"
                                ));

        State state =
                stateRepository
                        .findByIdAndActiveTrue(request.getStateId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "State not found"
                                ));

        City city =
                cityRepository
                        .findByIdAndActiveTrue(request.getCityId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "City not found"
                                ));

        if (!city.getState().getId().equals(state.getId())) {

            throw new BadRequestException(
                    "City does not belong to selected state"
            );
        }

        CityPincode pincode =
                cityPincodeRepository
                        .findByIdAndActiveTrue(request.getPincodeId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Pincode not found"
                                ));

        if (!pincode.getCity().getId().equals(city.getId())) {

            throw new BadRequestException(
                    "Pincode does not belong to selected city"
            );
        }

        String priestPhotoUrl = null;
        String aadhaarPdfUrl = null;

        try {

            FileUploadResponse priestPhotoResponse =
                    fileService.uploadImage(
                            priestPhoto,
                            FileType.PRIEST_PHOTOS
                    );

            priestPhotoUrl =
                    priestPhotoResponse.getFileUrl();

            FileUploadResponse aadhaarResponse =
                    fileService.uploadDocument(
                            aadhaarPdf,
                            FileType.PRIEST_AADHAAR
                    );

            aadhaarPdfUrl =
                    aadhaarResponse.getFileUrl();

            PriestRegistration registration =
                    PriestRegistration.builder()

                            .firstName(request.getFirstName())

                            .lastName(request.getLastName())

                            .mobileNumber(request.getMobileNumber())

                            .whatsappNumber(request.getWhatsappNumber())

                            .email(request.getEmail())

                            .dob(request.getDob())

                            .gothra(request.getGothra())

                            .pravara(request.getPravara())

                            .nativePlace(request.getNativePlace())

                            .aadhaarNumber(request.getAadhaarNumber())

                            .addressLine1(request.getAddressLine1())

                            .addressLine2(request.getAddressLine2())

                            .state(state)

                            .city(city)

                            .pincode(pincode)

                            .community(community)

                            .experience(request.getExperience())

                            .referredBy(request.getReferredBy())

                            .bankingName(request.getBankingName())

                            .bankName(request.getBankName())

                            .bankBranchName(request.getBankBranchName())

                            .bankIfscCode(request.getBankIfscCode())

                            .bankAccountNumber(request.getBankAccountNumber())

                            .upiId(request.getUpiId())

                            .priestPhotoUrl(priestPhotoUrl)

                            .aadhaarPdfUrl(aadhaarPdfUrl)

                            .build();

            for (Long languageId : request.getLanguageIds()) {

                Language language =
                        languageRepository
                                .findByIdAndActiveTrue(languageId)
                                .orElseThrow(() ->
                                        new ResourceNotFoundException(
                                                "Language not found"
                                        ));

                PriestRegistrationLanguageMapping mapping =
                        PriestRegistrationLanguageMapping
                                .builder()
                                .registration(registration)
                                .language(language)
                                .build();

                registration
                        .getLanguages()
                        .add(mapping);
            }

            priestRegistrationRepository.save(registration);

            return "Registration submitted successfully.";

        } catch (Exception ex) {

            if (priestPhotoUrl != null) {

                fileService.deleteImage(priestPhotoUrl);
            }

            if (aadhaarPdfUrl != null) {

                fileService.deleteImage(aadhaarPdfUrl);
            }

            throw ex;
        }
    }

    @Override
    public Page<PriestRegistrationCardResponse> getPendingRegistrations(
            int page,
            int size
    ) {

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by("createdAt").descending()
                );

        Page<PriestRegistration> registrations =
                priestRegistrationRepository.findByStatus(
                        PriestRegistrationStatus.PENDING,
                        pageable
                );

        return registrations.map(registration ->

                PriestRegistrationCardResponse.builder()

                        .registrationId(
                                registration.getId()
                        )

                        .firstName(
                                registration.getFirstName()
                        )

                        .lastName(
                                registration.getLastName()
                        )

                        .mobileNumber(
                                registration.getMobileNumber()
                        )

                        .status(
                                registration.getStatus()
                        )

                        .createdAt(
                                registration.getCreatedAt()
                        )

                        .build()
        );
    }

    @Override
    public PriestRegistrationDetailsResponse getRegistrationDetails(
            Long registrationId
    ) {

        PriestRegistration registration =
                priestRegistrationRepository
                        .findById(registrationId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Registration not found"
                                )
                        );

        return PriestRegistrationDetailsResponse.builder()

                .registrationId(
                        registration.getId()
                )

                .firstName(
                        registration.getFirstName()
                )

                .lastName(
                        registration.getLastName()
                )

                .dob(
                        registration.getDob()
                )

                .mobileNumber(
                        registration.getMobileNumber()
                )

                .whatsappNumber(
                        registration.getWhatsappNumber()
                )

                .email(
                        registration.getEmail()
                )

                .gothra(
                        registration.getGothra()
                )

                .pravara(
                        registration.getPravara()
                )

                .nativePlace(
                        registration.getNativePlace()
                )

                .aadhaarNumber(
                        registration.getAadhaarNumber()
                )

                .addressLine1(
                        registration.getAddressLine1()
                )

                .addressLine2(
                        registration.getAddressLine2()
                )

                .state(
                        registration.getState()
                                .getStateName()
                )

                .city(
                        registration.getCity()
                                .getCityName()
                )

                .pincode(
                        registration.getPincode()
                                .getPincode()
                )

                .community(
                        registration.getCommunity()
                                .getCommunityName()
                )

                .languages(
                        registration.getLanguages()
                                .stream()
                                .map(mapping ->
                                        mapping.getLanguage()
                                                .getLanguageName()
                                )
                                .toList()
                )

                .experience(
                        registration.getExperience()
                )

                .referredBy(
                        registration.getReferredBy()
                )

                .bankingName(
                        registration.getBankingName()
                )

                .bankName(
                        registration.getBankName()
                )

                .bankBranchName(
                        registration.getBankBranchName()
                )

                .bankIfscCode(
                        registration.getBankIfscCode()
                )

                .bankAccountNumber(
                        registration.getBankAccountNumber()
                )

                .upiId(
                        registration.getUpiId()
                )

                .priestPhotoUrl(
                        registration.getPriestPhotoUrl()
                )

                .aadhaarPdfUrl(
                        registration.getAadhaarPdfUrl()
                )

                .status(
                        registration.getStatus()
                )

                .createdAt(
                        registration.getCreatedAt()
                )

                .build();
    }

    @Override
    @Transactional
    public String approveRegistration(Long registrationId){
        PriestRegistration registration =
                priestRegistrationRepository
                        .findById(registrationId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Registration not found"
                                )
                        );

        if (registration.getStatus() != PriestRegistrationStatus.PENDING) {

            throw new BadRequestException(
                    "Registration already processed"
            );
        }

        if (usersRepository.existsByMobileNo(
                registration.getMobileNumber()
        )) {

            throw new BadRequestException(
                    "Mobile number already exists"
            );
        }

        if (priestRepository.existsByWhatsappNumber(
                registration.getMobileNumber()
        )) {

            throw new BadRequestException(
                    "Mobile number already exists"
            );
        }

        if (registration.getWhatsappNumber() != null) {

            if (usersRepository.existsByMobileNo(
                    registration.getWhatsappNumber()
            )) {

                throw new BadRequestException(
                        "WhatsApp number already exists"
                );
            }

            if (priestRepository.existsByWhatsappNumber(
                    registration.getWhatsappNumber()
            )) {

                throw new BadRequestException(
                        "WhatsApp number already exists"
                );
            }
        }

        if (priestRepository.existsByAadhaarNumber(
                registration.getAadhaarNumber()
        )) {

            throw new BadRequestException(
                    "Aadhaar already exists"
            );
        }

        if (registration.getEmail() != null &&
                usersRepository.findByEmail(
                        registration.getEmail()
                ).isPresent()) {

            throw new BadRequestException(
                    "Email already exists"
            );
        }

        String first =
                registration
                        .getFirstName().trim().toLowerCase();

        String password =
                first.toLowerCase()
                        +
                        registration.getDob().format(
                                DateTimeFormatter.ofPattern("ddMM")
                        );

        Users user = new Users();

        user.setFirstName(
                registration.getFirstName()
        );

        user.setLastName(
                registration.getLastName()
        );

        user.setMobileNo(
                registration.getMobileNumber()
        );

        user.setEmail(
                registration.getEmail()
        );

        user.setDob(
                registration.getDob()
        );

        user.setPassword(
                passwordEncoder.encode(password)
        );

        user.setStatus(
                UserStatus.ACTIVE
        );

        usersRepository.save(user);

        UserRole role = new UserRole();

        role.setUser(user);

        role.setRole(
                UserRoles.PRIEST
        );

        userRoleRepository.save(role);

        Priest priest =
                Priest.builder()

                        .user(user)

                        .gothra(
                                registration.getGothra()
                        )

                        .pravara(
                                registration.getPravara()
                        )

                        .nativePlace(
                                registration.getNativePlace()
                        )

                        .aadhaarNumber(
                                registration.getAadhaarNumber()
                        )

                        .whatsappNumber(
                                registration.getWhatsappNumber()
                        )

                        .addressLine1(
                                registration.getAddressLine1()
                        )

                        .addressLine2(
                                registration.getAddressLine2()
                        )

                        .state(
                                registration.getState()
                        )

                        .city(
                                registration.getCity()
                        )

                        .pincode(
                                registration.getPincode()
                        )

                        .community(
                                registration.getCommunity()
                        )

                        .experience(
                                registration.getExperience()
                        )

                        .referredBy(
                                registration.getReferredBy()
                        )

                        .bankingName(
                                registration.getBankingName()
                        )

                        .bankName(
                                registration.getBankName()
                        )

                        .bankBranchName(
                                registration.getBankBranchName()
                        )

                        .bankIfscCode(
                                registration.getBankIfscCode()
                        )

                        .bankAccountNumber(
                                registration.getBankAccountNumber()
                        )

                        .upiId(
                                registration.getUpiId()
                        )

                        .priestPhotoUrl(
                                registration.getPriestPhotoUrl()
                        )

                        .aadhaarPdfUrl(
                                registration.getAadhaarPdfUrl()
                        )

                        .active(true)

                        .build();

        for (PriestRegistrationLanguageMapping registrationLanguage
                : registration.getLanguages()) {

            PriestLanguageMapping mapping =
                    PriestLanguageMapping.builder()
                            .priest(priest)
                            .language(
                                    registrationLanguage.getLanguage()
                            )
                            .build();

            priest.getLanguages().add(mapping);
        }

        priestRepository.save(priest);

        priestRegistrationRepository.delete(
                registration
        );

        return "Priest approved successfully.";
    }

    @Override
    @Transactional
    public String rejectRegistration(Long registrationId) {

        PriestRegistration registration =
                priestRegistrationRepository
                        .findById(registrationId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Registration not found"
                                )
                        );

        if (registration.getPriestPhotoUrl() != null) {

            fileService.deleteImage(
                    registration.getPriestPhotoUrl()
            );
        }

        if (registration.getAadhaarPdfUrl() != null) {

            fileService.deleteImage(
                    registration.getAadhaarPdfUrl()
            );
        }

        priestRegistrationRepository.delete(registration);

        return "Registration rejected successfully.";
    }
}
