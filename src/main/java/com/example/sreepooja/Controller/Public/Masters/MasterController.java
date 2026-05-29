package com.example.sreepooja.Controller.Public.Masters;

import com.example.sreepooja.DTO.Response.Masters.CityResponse;
import com.example.sreepooja.DTO.Response.Masters.CommunityResponse;
import com.example.sreepooja.DTO.Response.Masters.LanguageResponse;
import com.example.sreepooja.Service.Masters.MasterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/masters")
@RequiredArgsConstructor
public class MasterController {

    private final MasterService masterService;


    // GET ALL ACTIVE CITIES

    @GetMapping("/cities")
    public List<CityResponse> getAllCities() {

        return masterService.getAllActiveCities();
    }


    // GET CITY DETAILS WITH STATE + PINCODES

    @GetMapping("/cities/{cityId}")
    public CityResponse getCityDetails(
            @PathVariable Long cityId
    ) {

        return masterService.getCityDetails(cityId);
    }

    // GET ALL ACTIVE LANGUAGES
    @GetMapping("/languages")
    public List<LanguageResponse> getAllActiveLanguages() {

        return masterService.getAllActiveLanguages();
    }

    //GET ALL ACTIVE COMMUNITIES
    @GetMapping("/communities")
    public List<CommunityResponse> getAllActiveCommunities() {

        return masterService.getAllActiveCommunities();
    }
}