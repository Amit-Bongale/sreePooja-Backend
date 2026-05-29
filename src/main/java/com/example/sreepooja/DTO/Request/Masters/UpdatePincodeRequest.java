package com.example.sreepooja.DTO.Request.Masters;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePincodeRequest {

    @NotBlank(message = "Pincode is required")
    private String pincode;

    private Boolean active = true;
}