package com.example.sreepooja.DTO.Users;


import org.springframework.cglib.core.Local;

import java.time.LocalDate;

public class SignupRequestDTO {

    private String firstName;
    private String lastName;
    private String mobileNo;
    private LocalDate dob;
    private String otp;
    private String email;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public String getOtp() {
        return otp;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getDob() {
        return dob;
    }
}
