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

    //GET STATE BY ID
    @GetMapping("/states/{stateId}")
    public StateResponse getStateById(
            @PathVariable Long stateId
    ) {

        return masterService.getStateById(stateId);
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

    //GET CITY BY ID
    @GetMapping("/cities/{cityId}")
    public CityResponse getCityById(
            @PathVariable Long cityId
    ) {

        return masterService.getCityById(cityId);
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

    //GET PINCODE BY ID
    @GetMapping("/pincodes/{pincodeId}")
    public PincodeResponse getPincodeById(
            @PathVariable Long pincodeId
    ) {

        return masterService.getPincodeById(pincodeId);
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

    //GET LANGUAGE BY ID
    @GetMapping("/languages/{languageId}")
    public LanguageResponse getLanguageById(
            @PathVariable Long languageId
    ) {

        return masterService.getLanguageById(languageId);
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

    //GET COMMUNITY BY ID
    @GetMapping("/communities/{communityId}")
    public CommunityResponse getCommunityById(
            @PathVariable Long communityId
    ) {

        return masterService.getCommunityById(communityId);
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