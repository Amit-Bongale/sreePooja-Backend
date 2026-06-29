package com.example.sreepooja.Service.Staff;

import com.example.sreepooja.DTO.Request.Staff.CreateStaffRequest;
import com.example.sreepooja.DTO.Request.Staff.UpdateStaffRequest;
import com.example.sreepooja.DTO.Response.Staff.StaffResponse;
import com.example.sreepooja.Enum.UserRoles;
import com.example.sreepooja.Enum.UserStatus;
import org.springframework.data.domain.Page;

public interface StaffService {

    StaffResponse createStaff(CreateStaffRequest request);

    Page<StaffResponse> getAllStaff(
            int page,
            int size,
            String search,
            UserStatus status,
            UserRoles role
    );

    StaffResponse getStaffById(Long id);

    StaffResponse updateStaff(
            Long id,
            UpdateStaffRequest request
    );

}