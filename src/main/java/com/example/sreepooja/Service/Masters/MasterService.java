package com.example.sreepooja.Service.Masters;

import com.example.sreepooja.DTO.Request.Masters.*;
import com.example.sreepooja.DTO.Response.Masters.*;

import java.util.List;

public interface MasterService {

    StateResponse createState(StateRequest request);

    StateResponse updateState(
            Long stateId,
            StateRequest request
    );

    CityResponse createCity(
            Long stateId,
            CityRequest request
    );

    List<CityResponse> getCitiesByState(Long stateId);

    List<CityResponse> getAllActiveCities();

    CityResponse getCityDetails(Long cityId);

    CityResponse updateCity(
            Long cityId,
            CityRequest request
    );

    List<PincodeResponse> createPincodes(
            Long cityId,
            PincodeRequest request
    );

    List<PincodeResponse> getPincodesByCity(Long cityId);

    PincodeResponse updatePincode(
            Long pincodeId,
            UpdatePincodeRequest request
    );

    LanguageResponse createLanguage(
            LanguageRequest request
    );

    List<LanguageResponse> getAllActiveLanguages();

    LanguageResponse updateLanguage(
            Long languageId,
            LanguageRequest request
    );

    CommunityResponse createCommunity(
            CommunityRequest request
    );

    List<CommunityResponse> getAllActiveCommunities();

    CommunityResponse updateCommunity(
            Long communityId,
            CommunityRequest request
    );

    List<StateResponse> getAllStatesForAdmin();

    List<LanguageResponse> getAllLanguagesForAdmin();

    List<CommunityResponse> getAllCommunitiesForAdmin();
}