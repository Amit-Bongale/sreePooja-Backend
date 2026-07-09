package com.example.sreepooja.Service.Staff;

import com.example.sreepooja.DTO.Request.Staff.CreateStaffRequest;
import com.example.sreepooja.DTO.Request.Staff.UpdateStaffProfileRequest;
import com.example.sreepooja.DTO.Request.Staff.UpdateStaffRequest;
import com.example.sreepooja.DTO.Response.Staff.StaffResponse;
import com.example.sreepooja.Entity.UserRole;
import com.example.sreepooja.Entity.Users;
import com.example.sreepooja.Enum.UserRoles;
import com.example.sreepooja.Enum.UserStatus;
import com.example.sreepooja.ExceptionHandlers.BadRequestException;
import com.example.sreepooja.ExceptionHandlers.ResourceNotFoundException;
import com.example.sreepooja.Repository.Users.UserRoleRepository;
import com.example.sreepooja.Repository.Users.UsersRepository;
import com.example.sreepooja.Service.CustomUserDetails.CustomUserDetails;
import com.example.sreepooja.Specification.StaffSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService {

    private final UsersRepository usersRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public StaffResponse createStaff(CreateStaffRequest request) {

        // Check mobile number
        if (usersRepository.findByMobileNo(request.getMobileNo()).isPresent()) {
            throw new BadRequestException("Mobile number already exists.");
        }

        // Check emailId
        if (usersRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BadRequestException("Email ID already exists.");
        }

        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new BadRequestException("Password field cannot be empty");
        }

        // USER role should not be assigned
        if (request.getRoles().contains(UserRoles.USER)) {
            throw new BadRequestException("USER role cannot be assigned.");
        }

        // SUPER_ADMIN role should not be assigned
        if (request.getRoles().contains(UserRoles.SUPER_ADMIN)) {
            throw new BadRequestException("SUPER_ADMIN role cannot be assigned.");
        }

        // Create User
        Users user = new Users();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setMobileNo(request.getMobileNo());
        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );
        user.setEmail(request.getEmail());
        user.setDob(request.getDob());
        user.setStatus(UserStatus.ACTIVE);

        Users savedUser = usersRepository.save(user);

        // Save Roles
        for (UserRoles role : request.getRoles()) {

            UserRole userRole = new UserRole();
            userRole.setUser(savedUser);
            userRole.setRole(role);

            userRoleRepository.save(userRole);
        }

        return StaffResponse.builder()
                .id(savedUser.getId())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .mobileNo(savedUser.getMobileNo())
                .email(savedUser.getEmail())
                .dob(savedUser.getDob())
                .status(savedUser.getStatus())
                .roles(
                        request.getRoles()
                )
                .build();
    }

    private StaffResponse mapToResponse(Users user) {

        return StaffResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .mobileNo(user.getMobileNo())
                .email(user.getEmail())
                .dob(user.getDob())
                .status(user.getStatus())
                .roles(
                        user.getRoles()
                                .stream()
                                .map(UserRole::getRole)
                                .collect(Collectors.toSet())
                )
                .build();
    }

    @Override
    public Page<StaffResponse> getAllStaff(
            int page,
            int size,
            String search,
            UserStatus status,
            UserRoles role
    ) {

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by("firstName").ascending()
                );

        Page<Users> users =
                usersRepository.findAll(
                        StaffSpecification.filterStaff(
                                search,
                                status,
                                role
                        ),
                        pageable
                );

        return users.map(this::mapToResponse);
    }

    @Override
    public StaffResponse getStaffById(Long id) {

        Users user = usersRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Staff not found."));

        boolean isStaff = user.getRoles()
                .stream()
                .map(UserRole::getRole)
                .anyMatch(role ->
                        role != UserRoles.USER &&
                                role != UserRoles.PRIEST
                );

        if (!isStaff) {
            throw new ResourceNotFoundException("Staff not found.");
        }

        return mapToResponse(user);
    }

    private boolean isStaff(Users user) {

        return user.getRoles()
                .stream()
                .map(UserRole::getRole)
                .anyMatch(role ->
                        role != UserRoles.USER &&
                                role != UserRoles.PRIEST
                );
    }

    @Override
    @Transactional
    public StaffResponse updateStaff(
            Long id,
           UpdateStaffRequest request
    ) {

        Users user = usersRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Staff not found."));

        if (!isStaff(user)) {
            throw new ResourceNotFoundException("Staff not found.");
        }

        // First Name
        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }

        // Last Name
        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }

        // Mobile
        if (request.getMobileNo() != null &&
                !request.getMobileNo().equals(user.getMobileNo())) {

            usersRepository.findByMobileNo(request.getMobileNo())
                    .ifPresent(existing -> {
                        throw new BadRequestException("Mobile number already exists.");
                    });

            user.setMobileNo(request.getMobileNo());
        }

        // Email
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }

        // DOB
        if (request.getDob() != null) {
            user.setDob(request.getDob());
        }

        // Password
        if (request.getPassword() != null &&
                !request.getPassword().isBlank()) {

            user.setPassword(
                    passwordEncoder.encode(
                            request.getPassword()
                    )
            );
        }

        // Status
        if (request.getStatus() != null) {
            user.setStatus(request.getStatus());
        }

        // Roles
        if (request.getRoles() != null) {

            if (request.getRoles().contains(UserRoles.USER)) {
                throw new BadRequestException("USER role cannot be assigned.");
            }

            if (request.getRoles().contains(UserRoles.SUPER_ADMIN)) {
                throw new BadRequestException("SUPER_ADMIN role cannot be assigned.");
            }

            user.getRoles().clear();

            usersRepository.saveAndFlush(user);

            for (UserRoles role : request.getRoles()) {

                UserRole userRole = new UserRole();
                userRole.setUser(user);
                userRole.setRole(role);

                user.getRoles().add(userRole);
            }
        }

            Users updatedUser = usersRepository.save(user);

        return mapToResponse(updatedUser);
    }

    @Override
    @Transactional
    public StaffResponse updateMyProfile(
            UpdateStaffProfileRequest request
    ) {

        CustomUserDetails userDetails =
                (CustomUserDetails)
                        SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getPrincipal();

        Users user =
                usersRepository
                        .findById(userDetails.getUserId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Staff not found."
                                )
                        );

        if (!isStaff(user)) {
            throw new ResourceNotFoundException(
                    "Staff not found."
            );
        }

        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }

        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }

        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }

        if (request.getDob() != null) {
            user.setDob(request.getDob());
        }

        if (request.getPassword() != null
                && !request.getPassword().isBlank()) {

            user.setPassword(
                    passwordEncoder.encode(
                            request.getPassword()
                    )
            );
        }

        Users updatedUser =
                usersRepository.save(user);

        return mapToResponse(updatedUser);
    }
}