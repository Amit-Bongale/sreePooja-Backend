package com.example.sreepooja.DTO.Request.Masters;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LanguageRequest {

    @NotBlank(message = "Language name is required")
    private String languageName;

    private Boolean active = true;
}