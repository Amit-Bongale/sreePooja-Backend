package com.example.sreepooja.Controller.Admin.Masters;

import com.example.sreepooja.DTO.Request.Masters.*;
import com.example.sreepooja.DTO.Response.Masters.*;
import com.example.sreepooja.Service.Masters.MasterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/masters")
@RequiredArgsConstructor
public class AdminMasterController {

    private final MasterService masterService;

    // CREATE STATE
    @PostMapping("/states")
    public StateResponse createState(
            @Valid @RequestBody StateRequest request
    ) {

        return masterService.createState(request);
    }


    // GET ALL STATES
    @GetMapping("/states")
    public List<StateResponse> getAllStates() {

        return masterService.getAllStatesForAdmin();
    }

    //UPDATE STATE
    @PutMapping("/states/{stateId}")
    public StateResponse updateState(
            @PathVariable Long stateId,
            @Valid @RequestBody StateRequest request
    ) {

        return masterService.updateState(stateId, request);
    }

    // CREATE CITY UNDER STATE
    @PostMapping("/states/{stateId}/cities")
    public CityResponse createCity(
            @PathVariable Long stateId,
            @Valid @RequestBody CityRequest request
    ) {

        return masterService.createCity(stateId, request);
    }


    // GET CITIES BY STATE
    @GetMapping("/states/{stateId}/cities")
    public List<CityResponse> getCitiesByState(
            @PathVariable Long stateId
    ) {

        return masterService.getCitiesByState(stateId);
    }

    //UPDATE CITY
    @PutMapping("/cities/{cityId}")
    public CityResponse updateCity(
            @PathVariable Long cityId,
            @Valid @RequestBody CityRequest request
    ) {

        return masterService.updateCity(cityId, request);
    }


    // CREATE PINCODES UNDER CITY
    @PostMapping("/cities/{cityId}/pincodes")
    public List<PincodeResponse> createPincodes(
            @PathVariable Long cityId,
            @Valid @RequestBody PincodeRequest request
    ) {

        return masterService.createPincodes(cityId, request);
    }


    // GET PINCODES BY CITY
    @GetMapping("/cities/{cityId}/pincodes")
    public List<PincodeResponse> getPincodesByCity(
            @PathVariable Long cityId
    ) {

        return masterService.getPincodesByCity(cityId);
    }

    //UPDATE PINCODE
    @PutMapping("/pincodes/{pincodeId}")
    public PincodeResponse updatePincode(
            @PathVariable Long pincodeId,
            @Valid @RequestBody UpdatePincodeRequest request
    ) {

        return masterService.updatePincode(
                pincodeId,
                request
        );
    }

    //CREATE LANGUAGE
    @PostMapping("/languages")
    public LanguageResponse createLanguage(
            @Valid @RequestBody LanguageRequest request
    ) {

        return masterService.createLanguage(request);
    }

    //GET LANGUAGES
    @GetMapping("/languages")
    public List<LanguageResponse> getAllLanguages() {

        return masterService.getAllLanguagesForAdmin();
    }

    //UPDATE LANGUAGE
    @PutMapping("/languages/{languageId}")
    public LanguageResponse updateLanguage(
            @PathVariable Long languageId,
            @Valid @RequestBody LanguageRequest request
    ) {

        return masterService.updateLanguage(
                languageId,
                request
        );
    }

    //CREATE COMMUNITY
    @PostMapping("/communities")
    public CommunityResponse createCommunity(
            @Valid @RequestBody CommunityRequest request
    ) {

        return masterService.createCommunity(request);
    }

    //GET COMMUNITY
    @GetMapping("/communities")
    public List<CommunityResponse> getAllCommunities() {

        return masterService.getAllCommunitiesForAdmin();
    }

    //UPDATE COMMUNITY
    @PutMapping("/communities/{communityId}")
    public CommunityResponse updateCommunity(
            @PathVariable Long communityId,
            @Valid @RequestBody CommunityRequest request
    ) {

        return masterService.updateCommunity(
                communityId,
                request
        );
    }

}