package com.example.sreepooja.DTO.Response.Masters;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LanguageResponse {

    private Long id;

    private String languageName;

    private Boolean active;
}