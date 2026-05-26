package com.example.sreepooja.DTO.Poojas.Response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommunityResponse {

    private Long id;

    private String name;
}