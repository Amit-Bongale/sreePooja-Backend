package com.example.sreepooja.DTO.Response.Masters;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CommunityResponse {

    private Long id;

    private String communityName;

    private Boolean active;
}