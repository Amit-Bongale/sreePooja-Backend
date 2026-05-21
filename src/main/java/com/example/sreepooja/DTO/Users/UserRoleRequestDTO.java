package com.example.sreepooja.DTO.Users;


import com.example.sreepooja.Enum.UserRoles;

public class UserRoleRequestDTO {
    Long userID;
    UserRoles role;

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public UserRoles getRole() {
        return role;
    }

    public void setRole(UserRoles role) {
        this.role = role;
    }
}
