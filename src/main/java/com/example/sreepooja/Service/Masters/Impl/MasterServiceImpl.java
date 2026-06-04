package com.example.sreepooja.Service.Masters.Impl;

import com.example.sreepooja.DTO.Request.Masters.*;
import com.example.sreepooja.DTO.Response.Masters.*;
import com.example.sreepooja.Entity.Masters.*;
import com.example.sreepooja.ExceptionHandlers.BadRequestException;
import com.example.sreepooja.ExceptionHandlers.DuplicateResourceException;
import com.example.sreepooja.ExceptionHandlers.ResourceNotFoundException;
import com.example.sreepooja.Repository.Masters.*;
import com.example.sreepooja.Service.Masters.MasterService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MasterServiceImpl implements MasterService {

    private final StateRepository stateRepository;
    private final CityRepository cityRepository;
    private final CityPincodeRepository cityPincodeRepository;
    private final LanguageRepository languageRepository;
    private final CommunityRepository communityRepository;


    @Override
    public StateResponse createState(StateRequest request) {

        if(request.getStateName()==null || request.getStateName().isBlank()){
            throw new BadRequestException("State name is required");
        }

        if (stateRepository.existsByStateNameIgnoreCase(
                request.getStateName()
        )) {
            throw new DuplicateResourceException("State already exists");
        }

        State state = State.builder()
                .stateName(request.getStateName())
                .active(request.getActive())
                .build();

        State savedState = stateRepository.save(state);

        return StateResponse.builder()
                .id(savedState.getId())
                .stateName(savedState.getStateName())
                .active(savedState.getActive())
                .build();
    }

    @Transactional
    @Override
    public StateResponse updateState(
            Long stateId,
            StateRequest request
    ) {

        State state = stateRepository.findById(stateId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("State not found"));

        if(request.getStateName()==null || request.getStateName().isBlank()){
            throw new BadRequestException("State name is required");
        }

        if (!state.getStateName().equalsIgnoreCase(
                request.getStateName()
        )) {

            if (stateRepository.existsByStateNameIgnoreCase(
                    request.getStateName()
            )) {
                throw new DuplicateResourceException(
                        "State name already exists"
                );
            }
        }

        state.setStateName(request.getStateName());
        state.setActive(request.getActive());

        if (Boolean.FALSE.equals(request.getActive())) {

            List<City> cities =
                    cityRepository.findByStateId(state.getId());

            for (City city : cities) {

                city.setActive(false);

                List<CityPincode> pincodes =
                        cityPincodeRepository
                                .findByCityId(city.getId());

                for (CityPincode pincode : pincodes) {
                    pincode.setActive(false);
                }

                cityPincodeRepository.saveAll(pincodes);
            }

            cityRepository.saveAll(cities);
        }

        State updatedState = stateRepository.save(state);

        return StateResponse.builder()
                .id(updatedState.getId())
                .stateName(updatedState.getStateName())
                .active(updatedState.getActive())
                .build();
    }

    @Override
    public CityResponse createCity(
            Long stateId,
            CityRequest request
    ) {

        State state = stateRepository.findById(stateId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("State not found"));

        if(request.getCityName()==null || request.getCityName().isBlank()){
            throw new BadRequestException("City name is required");
        }

        if (cityRepository.existsByCityNameIgnoreCaseAndStateId(
                request.getCityName(),
                stateId
        )) {
            throw new DuplicateResourceException(
                    "City already exists in this state"
            );
        }

        Boolean cityActive = request.getActive();

        if (Boolean.FALSE.equals(state.getActive())) {
            cityActive = false;
        }

        City city = City.builder()
                .cityName(request.getCityName())
                .active(cityActive)
                .state(state)
                .build();

        City savedCity = cityRepository.save(city);

        return CityResponse.builder()
                .id(savedCity.getId())
                .cityName(savedCity.getCityName())
                .stateName(savedCity.getState().getStateName())
                .active(savedCity.getActive())
                .pincodes(new ArrayList<>())
                .build();
    }

    @Override
    public List<CityResponse> getCitiesByState(Long stateId) {

        List<City> cities =
                cityRepository.findByStateId(stateId);

        List<CityResponse> responseList = new ArrayList<>();

        for (City city : cities) {

            List<PincodeResponse> pincodeResponses =
                    new ArrayList<>();

            List<CityPincode> pincodes =
                    cityPincodeRepository
                            .findByCityId(city.getId());

            for (CityPincode pincode : pincodes) {

                PincodeResponse pincodeResponse =
                        PincodeResponse.builder()
                                .id(pincode.getId())
                                .pincode(pincode.getPincode())
                                .active(pincode.getActive())
                                .build();

                pincodeResponses.add(pincodeResponse);
            }

            CityResponse response = CityResponse.builder()
                    .id(city.getId())
                    .cityName(city.getCityName())
                    .stateName(city.getState().getStateName())
                    .active(city.getActive())
                    .pincodes(new ArrayList<>())
                    .build();

            responseList.add(response);
        }

        return responseList;
    }

    @Transactional
    @Override
    public CityResponse updateCity(
            Long cityId,
            CityRequest request
    ) {

        City city = cityRepository.findById(cityId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("City not found"));

        if(request.getCityName()==null || request.getCityName().isBlank()){
            throw new BadRequestException("City name is required");
        }

        if (!city.getCityName().equalsIgnoreCase(
                request.getCityName()
        )) {

            if (cityRepository
                    .existsByCityNameIgnoreCaseAndStateId(
                            request.getCityName(),
                            city.getState().getId()
                    )) {

                throw new DuplicateResourceException(
                        "City already exists in this state"
                );
            }
        }

        if (Boolean.TRUE.equals(request.getActive())
                && Boolean.FALSE.equals(city.getState().getActive())) {

            throw new BadRequestException(
                    "Cannot activate city because parent state is inactive"
            );
        }

        if (Boolean.FALSE.equals(request.getActive())) {

            List<CityPincode> pincodes =
                    cityPincodeRepository
                            .findByCityId(city.getId());

            for (CityPincode pincode : pincodes) {
                pincode.setActive(false);
            }

            cityPincodeRepository.saveAll(pincodes);
        }

        city.setCityName(request.getCityName());
        city.setActive(request.getActive());

        City updatedCity = cityRepository.save(city);

        return CityResponse.builder()
                .id(updatedCity.getId())
                .cityName(updatedCity.getCityName())
                .stateName(updatedCity.getState().getStateName())
                .active(updatedCity.getActive())
                .pincodes(new ArrayList<>())
                .build();
    }

    @Override
    public List<PincodeResponse> createPincodes(
            Long cityId,
            PincodeRequest request
    ) {

        City city = cityRepository.findById(cityId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("City not found"));

        Boolean pincodeActive = request.getActive();

        if (Boolean.FALSE.equals(city.getActive())) {
            pincodeActive = false;
        }

        if (request.getPincodes() == null ||
                request.getPincodes().isEmpty()) {

            throw new BadRequestException(
                    "At least one pincode is required"
            );
        }

        List<PincodeResponse> responseList = new ArrayList<>();

        for (String pin : request.getPincodes()) {

            if (pin == null ||
                    !pin.matches("\\d{6}")) {

                throw new BadRequestException(
                        "Pincode must be exactly 6 digits"
                );
            }

            if (cityPincodeRepository.existsByPincode(pin)) {
                throw new DuplicateResourceException(
                        "Pincode already exists: " + pin
                );
            }

            CityPincode cityPincode = CityPincode.builder()
                    .pincode(pin)
                    .active(pincodeActive)
                    .city(city)
                    .build();

            CityPincode savedPincode =
                    cityPincodeRepository.save(cityPincode);

            PincodeResponse response =
                    PincodeResponse.builder()
                            .id(savedPincode.getId())
                            .pincode(savedPincode.getPincode())
                            .active(savedPincode.getActive())
                            .build();

            responseList.add(response);
        }

        return responseList;
    }

    @Override
    public List<PincodeResponse> getPincodesByCity(Long cityId) {

        List<CityPincode> pincodes =
                cityPincodeRepository
                        .findByCityId(cityId);

        List<PincodeResponse> responseList =
                new ArrayList<>();

        for (CityPincode pincode : pincodes) {

            PincodeResponse response =
                    PincodeResponse.builder()
                            .id(pincode.getId())
                            .pincode(pincode.getPincode())
                            .active(pincode.getActive())
                            .build();

            responseList.add(response);
        }

        return responseList;
    }

    @Override
    public List<CityResponse> getAllActiveCities() {

        List<City> cities = cityRepository.findByActiveTrue();

        List<CityResponse> responseList = new ArrayList<>();

        for (City city : cities) {

            CityResponse response = CityResponse.builder()
                    .id(city.getId())
                    .cityName(city.getCityName())
                    .stateName(city.getState().getStateName())
                    .active(city.getActive())
                    .pincodes(new ArrayList<>())
                    .build();

            responseList.add(response);
        }

        return responseList;
    }

    @Override
    public CityResponse getCityDetails(Long cityId) {

        City city = cityRepository.findByIdAndActiveTrue(cityId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("City not found"));

        List<CityPincode> pincodes =
                cityPincodeRepository
                        .findByCityIdAndActiveTrue(cityId);

        return CityResponse.builder()
                .id(city.getId())
                .cityName(city.getCityName())
                .stateName(city.getState().getStateName())
                .active(city.getActive())
                .pincodes(pincodes.stream().map(CityPincode::getPincode).toList())
                .build();
    }

    @Override
    public PincodeResponse updatePincode(
            Long pincodeId,
            UpdatePincodeRequest request
    ) {

        CityPincode cityPincode =
                cityPincodeRepository.findById(pincodeId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Pincode not found"
                                ));

        if (request.getPincode() == null ||
                !request.getPincode().matches("\\d{6}")) {

            throw new BadRequestException(
                    "Pincode must be exactly 6 digits"
            );
        }

        if (!cityPincode.getPincode().equals(
                request.getPincode()
        )) {

            if (cityPincodeRepository.existsByPincode(
                    request.getPincode()
            )) {

                throw new DuplicateResourceException(
                        "Pincode already exists"
                );
            }
        }

        if (Boolean.TRUE.equals(request.getActive())
                && Boolean.FALSE.equals(
                cityPincode.getCity().getActive()
        )) {

            throw new BadRequestException(
                    "Cannot activate pincode because parent city is inactive"
            );
        }

        cityPincode.setPincode(request.getPincode());
        cityPincode.setActive(request.getActive());

        CityPincode updatedPincode =
                cityPincodeRepository.save(cityPincode);

        return PincodeResponse.builder()
                .id(updatedPincode.getId())
                .pincode(updatedPincode.getPincode())
                .active(updatedPincode.getActive())
                .build();
    }

    @Override
    public LanguageResponse createLanguage(
            LanguageRequest request
    ) {

        if (languageRepository
                .existsByLanguageNameIgnoreCase(
                        request.getLanguageName()
                )) {

            throw new DuplicateResourceException(
                    "Language already exists"
            );
        }

        Language language = Language.builder()
                .languageName(request.getLanguageName())
                .active(request.getActive())
                .build();

        Language savedLanguage =
                languageRepository.save(language);

        return LanguageResponse.builder()
                .id(savedLanguage.getId())
                .languageName(savedLanguage.getLanguageName())
                .active(savedLanguage.getActive())
                .build();
    }

    @Override
    public List<LanguageResponse> getAllActiveLanguages() {

        List<Language> languages =
                languageRepository.findByActiveTrue();

        List<LanguageResponse> responseList =
                new ArrayList<>();

        for (Language language : languages) {

            LanguageResponse response =
                    LanguageResponse.builder()
                            .id(language.getId())
                            .languageName(
                                    language.getLanguageName()
                            )
                            .active(language.getActive())
                            .build();

            responseList.add(response);
        }

        return responseList;
    }

    @Override
    public LanguageResponse updateLanguage(
            Long languageId,
            LanguageRequest request
    ) {

        Language language = languageRepository
                .findById(languageId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Language not found"
                        ));

        if (!language.getLanguageName()
                .equalsIgnoreCase(
                        request.getLanguageName()
                )) {

            if (languageRepository
                    .existsByLanguageNameIgnoreCase(
                            request.getLanguageName()
                    )) {

                throw new DuplicateResourceException(
                        "Language already exists"
                );
            }
        }

        language.setLanguageName(
                request.getLanguageName()
        );

        language.setActive(request.getActive());

        Language updatedLanguage =
                languageRepository.save(language);

        return LanguageResponse.builder()
                .id(updatedLanguage.getId())
                .languageName(
                        updatedLanguage.getLanguageName()
                )
                .active(updatedLanguage.getActive())
                .build();
    }

    @Override
    public CommunityResponse createCommunity(
            CommunityRequest request
    ) {

        if (communityRepository
                .existsByCommunityNameIgnoreCase(
                        request.getCommunityName()
                )) {

            throw new DuplicateResourceException(
                    "Community already exists"
            );
        }

        Community community = Community.builder()
                .communityName(request.getCommunityName())
                .active(request.getActive())
                .build();

        Community savedCommunity =
                communityRepository.save(community);

        return CommunityResponse.builder()
                .id(savedCommunity.getId())
                .communityName(
                        savedCommunity.getCommunityName()
                )
                .active(savedCommunity.getActive())
                .build();
    }

    @Override
    public List<CommunityResponse> getAllActiveCommunities() {

        List<Community> communities =
                communityRepository.findByActiveTrue();

        List<CommunityResponse> responseList =
                new ArrayList<>();

        for (Community community : communities) {

            CommunityResponse response =
                    CommunityResponse.builder()
                            .id(community.getId())
                            .communityName(
                                    community.getCommunityName()
                            )
                            .active(community.getActive())
                            .build();

            responseList.add(response);
        }

        return responseList;
    }

    @Override
    public CommunityResponse updateCommunity(
            Long communityId,
            CommunityRequest request
    ) {

        Community community =
                communityRepository.findById(communityId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Community not found"
                                ));

        if (!community.getCommunityName()
                .equalsIgnoreCase(
                        request.getCommunityName()
                )) {

            if (communityRepository
                    .existsByCommunityNameIgnoreCase(
                            request.getCommunityName()
                    )) {

                throw new DuplicateResourceException(
                        "Community already exists"
                );
            }
        }

        community.setCommunityName(
                request.getCommunityName()
        );

        community.setActive(
                request.getActive()
        );

        Community updatedCommunity =
                communityRepository.save(community);

        return CommunityResponse.builder()
                .id(updatedCommunity.getId())
                .communityName(
                        updatedCommunity.getCommunityName()
                )
                .active(updatedCommunity.getActive())
                .build();
    }

    @Override
    public List<StateResponse> getAllStatesForAdmin() {

        List<State> states = stateRepository.findAll();

        List<StateResponse> responseList = new ArrayList<>();

        for (State state : states) {

            responseList.add(
                    StateResponse.builder()
                            .id(state.getId())
                            .stateName(state.getStateName())
                            .active(state.getActive())
                            .build()
            );
        }

        return responseList;
    }

    @Override
    public List<LanguageResponse> getAllLanguagesForAdmin() {

        List<Language> languages =
                languageRepository.findAll();

        List<LanguageResponse> responseList =
                new ArrayList<>();

        for (Language language : languages) {

            responseList.add(
                    LanguageResponse.builder()
                            .id(language.getId())
                            .languageName(language.getLanguageName())
                            .active(language.getActive())
                            .build()
            );
        }

        return responseList;
    }

    @Override
    public List<CommunityResponse> getAllCommunitiesForAdmin() {

        List<Community> communities =
                communityRepository.findAll();

        List<CommunityResponse> responseList =
                new ArrayList<>();

        for (Community community : communities) {

            responseList.add(
                    CommunityResponse.builder()
                            .id(community.getId())
                            .communityName(
                                    community.getCommunityName()
                            )
                            .active(community.getActive())
                            .build()
            );
        }

        return responseList;
    }

    @Override
    public StateResponse getStateById(
            Long stateId
    ) {

        State state = stateRepository.findById(stateId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "State not found"
                        ));

        return StateResponse.builder()
                .id(state.getId())
                .stateName(state.getStateName())
                .active(state.getActive())
                .build();
    }

    @Override
    public CityResponse getCityById(
            Long cityId
    ) {

        City city = cityRepository.findById(cityId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "City not found"
                        ));

        return CityResponse.builder()
                .id(city.getId())
                .cityName(city.getCityName())
                .stateName(city.getState().getStateName())
                .active(city.getActive())
                .pincodes(new ArrayList<>())
                .build();
    }

    @Override
    public PincodeResponse getPincodeById(
            Long pincodeId
    ) {

        CityPincode pincode =
                cityPincodeRepository.findById(pincodeId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Pincode not found"
                                ));

        return PincodeResponse.builder()
                .id(pincode.getId())
                .pincode(pincode.getPincode())
                .active(pincode.getActive())
                .build();
    }

    @Override
    public LanguageResponse getLanguageById(
            Long languageId
    ) {

        Language language =
                languageRepository.findById(languageId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Language not found"
                                ));

        return LanguageResponse.builder()
                .id(language.getId())
                .languageName(
                        language.getLanguageName()
                )
                .active(language.getActive())
                .build();
    }

    @Override
    public CommunityResponse getCommunityById(
            Long communityId
    ) {

        Community community =
                communityRepository.findById(communityId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Community not found"
                                ));

        return CommunityResponse.builder()
                .id(community.getId())
                .communityName(
                        community.getCommunityName()
                )
                .active(community.getActive())
                .build();
    }
}