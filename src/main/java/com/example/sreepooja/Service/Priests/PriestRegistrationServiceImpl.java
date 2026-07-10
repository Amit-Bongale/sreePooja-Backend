package com.example.sreepooja.Service.Priests;

import com.example.sreepooja.Repository.Masters.*;
import com.example.sreepooja.Repository.Priests.PriestRegistrationRepository;
import com.example.sreepooja.Repository.Priests.PriestRepository;
import com.example.sreepooja.Repository.Users.UsersRepository;
import com.example.sreepooja.Service.File.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PriestRegistrationServiceImpl
//        implements PriestRegistrationService
{

    private final PriestRegistrationRepository priestRegistrationRepository;

    private final UsersRepository usersRepository;

    private final PriestRepository priestRepository;

    private final CommunityRepository communityRepository;

    private final StateRepository stateRepository;

    private final CityRepository cityRepository;

    private final CityPincodeRepository cityPincodeRepository;

    private final LanguageRepository languageRepository;

    private final FileService fileService;

}
