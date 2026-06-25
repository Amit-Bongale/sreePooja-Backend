package com.example.sreepooja.DTO.Users;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long id;

    private String fullName;

    private String mobileNo;
}
