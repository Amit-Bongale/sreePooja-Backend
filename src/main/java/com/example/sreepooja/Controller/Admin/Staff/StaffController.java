package com.example.sreepooja.Controller.Admin.Staff;

import com.example.sreepooja.DTO.Request.Staff.CreateStaffRequest;
import com.example.sreepooja.DTO.Request.Staff.UpdateStaffRequest;
import com.example.sreepooja.DTO.Response.Staff.StaffResponse;
import com.example.sreepooja.Enum.UserRoles;
import com.example.sreepooja.Enum.UserStatus;
import com.example.sreepooja.Service.Staff.StaffService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/staff")
public class StaffController {

    private final StaffService staffService;

    public StaffController(
            StaffService staffService
    ) {
        this.staffService = staffService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<StaffResponse> createStaff(
            @Valid
            @RequestBody
            CreateStaffRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        staffService.createStaff(request)
                );
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<Page<StaffResponse>> getAllStaff(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(required = false)
            String search,

            @RequestParam(required = false)
            UserStatus status,

            @RequestParam(required = false)
            UserRoles role

    ) {

        return ResponseEntity.ok(

                staffService.getAllStaff(
                        page,
                        size,
                        search,
                        status,
                        role
                )

        );

    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<StaffResponse> getStaffById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                staffService.getStaffById(id)
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<StaffResponse> updateStaff(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStaffRequest request
    ) {

        return ResponseEntity.ok(
                staffService.updateStaff(id, request)
        );
    }
}