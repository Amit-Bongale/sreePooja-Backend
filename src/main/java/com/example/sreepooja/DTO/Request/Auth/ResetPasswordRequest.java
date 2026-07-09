package com.example.sreepooja.DTO.Request.Auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetPasswordRequest {

    @NotBlank
    private String mobileNo;

    @NotBlank
    private String otp;

    @NotBlank
    private String newPassword;
}