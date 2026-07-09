package com.example.sreepooja.Controller;

import com.example.sreepooja.DTO.Request.Auth.ResetPasswordRequest;
import com.example.sreepooja.DTO.Request.Auth.StaffLoginRequest;
import com.example.sreepooja.DTO.Users.SignupRequestDTO;
import com.example.sreepooja.Entity.UserRole;
import com.example.sreepooja.Entity.Users;
import com.example.sreepooja.Enum.UserRoles;
import com.example.sreepooja.ExceptionHandlers.BadRequestException;
import com.example.sreepooja.ExceptionHandlers.ResourceNotFoundException;
import com.example.sreepooja.JWT.JwtUtil;
import com.example.sreepooja.Repository.Users.UsersRepository;
import com.example.sreepooja.Service.CustomUserDetails.CustomUserDetails;
import com.example.sreepooja.Service.CustomUserDetails.CustomUserDetailsService;
import com.example.sreepooja.Service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired private OtpService otpService;
    @Autowired private UsersRepository userRepository;
    @Autowired private CustomUserDetailsService userDetailsService;
    @Autowired private JwtUtil jwtUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;


    // send otp request for signup
    @PostMapping("/signup/request-otp")
    public ResponseEntity<?> signupRequestOtp(@RequestParam String mobileNo) {
        System.out.println("signup otp request");
        if (userRepository.findByMobileNo(mobileNo).isPresent()) {
            throw new BadRequestException("User already registered , Please Login");
        }

        otpService.sendOtp(mobileNo);

        return ResponseEntity.ok(
                Map.of(
                        "message", "OTP sent for signup",
                        "mobileNo", mobileNo
                )
        );
    }


    // create user validating otp
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequestDTO request){

        if(userRepository.findByMobileNo(request.getMobileNo()).isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error" , "User Already Exist"));
        }

        boolean otpValid = otpService.verifySignupOtp(
                request.getMobileNo(),
                request.getOtp()
        );

        if (!otpValid) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid or expired OTP"));
        }

        Users user = new Users();
        user.setFirstName(request.getFirstName());

        user.setLastName(request.getLastName());
        user.setMobileNo(request.getMobileNo());
        user.setEmail(request.getEmail());
        user.setDob(request.getDob());
        UserRole userRole = new UserRole();
        userRole.setRole(UserRoles.USER);
        userRole.setUser(user);

        user.getRoles().add(userRole);

        Users user1 = userRepository.save(user);

        CustomUserDetails userDetails =
                (CustomUserDetails) userDetailsService
                        .loadUserByUsername(user1.getMobileNo());

        // Extract roles
        Set<String> roles = user.getRoles()
                .stream()
                .map(r -> r.getRole().name())
                .collect(Collectors.toSet());

        // Generate JWT
        String token = jwtUtil.generateToken(
                userDetails,
                roles
        );

        return ResponseEntity.ok(
                Map.of(
                        "message", "user registered successfully",
                        "token", token,
                        "userId", user1.getId()
                )
        );
    }

    // send otp for login
    @PostMapping("/request-otp")
    public ResponseEntity<?> requestOtp(@RequestParam String mobileNo) {

        Users user =
                userRepository
                        .findByMobileNo(mobileNo)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "User not Registered, Please Register to Login"
                                )
                        );

        if (mobileNo == null || mobileNo.length() < 10) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", "Invalid mobile number"));
        }

        if(userRepository.findByMobileNo(mobileNo).isEmpty()){
            throw new ResourceNotFoundException("User not Registered, Please Register to Login");
        }

        boolean isOnlyUser =
                user.getRoles()
                        .stream()
                        .allMatch(role ->
                                role.getRole() == UserRoles.USER
                        );

        if (!isOnlyUser) {

            throw new BadRequestException(
                    "Only customers can login using OTP."
            );
        }

        otpService.sendOtp(mobileNo);

        return ResponseEntity.ok(
                Map.of(
                        "message", "OTP sent successfully",
                        "mobileNo", mobileNo
                )
        );
    }


    // verify otp and user return jwt
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestParam String mobileNo,
                                       @RequestParam String otp) {

        boolean isValid = otpService.verifyLoginOtp(mobileNo, otp);

        if (!isValid) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid or expired OTP"));
        }

        // Load User
        Users user = userRepository.findByMobileNo(mobileNo)
                .orElseThrow(() ->
                        new BadRequestException("User not registered"));

        // Load Spring Security UserDetails
        CustomUserDetails userDetails =
                (CustomUserDetails) userDetailsService
                        .loadUserByUsername(mobileNo);


        // Extract roles
        Set<String> roles = user.getRoles()
                .stream()
                .map(r -> r.getRole().name())
                .collect(Collectors.toSet());

        // Generate JWT
        String token = jwtUtil.generateToken(
                userDetails,
                roles
        );

        return ResponseEntity.ok(
                Map.of(
                        "token", token,
                        "userId", user.getId(),
                        "roles", roles
                )
        );
    }

    @PostMapping("/staff-login")
    public ResponseEntity<?> staffLogin(
            @RequestBody StaffLoginRequest request
    ) {

        Users user = userRepository
                .findByMobileNo(request.getMobileNo())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Invalid mobile number or password"
                        )
                );

        if (user.getPassword() == null) {
            throw new BadRequestException(
                    "Password login is not enabled for this account."
            );
        }

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )) {

            throw new BadRequestException(
                    "Invalid mobile number or password"
            );
        }

        Set<String> roles =
                user.getRoles()
                        .stream()
                        .map(role -> role.getRole().name())
                        .collect(Collectors.toSet());

        if (roles.contains(UserRoles.USER.name())) {

            throw new BadRequestException(
                    "Customers must login using OTP."
            );
        }

        CustomUserDetails userDetails =
                (CustomUserDetails)
                        userDetailsService.loadUserByUsername(
                                user.getMobileNo()
                        );

        String token =
                jwtUtil.generateToken(
                        userDetails,
                        roles
                );

        return ResponseEntity.ok(
                Map.of(
                        "token", token,
                        "userId", user.getId(),
                        "roles", roles
                )
        );
    }

    @PostMapping("/forgot-password/request-otp")
    public ResponseEntity<?> forgotPasswordRequestOtp(
            @RequestParam String mobileNo
    ) {

        Users user =
                userRepository
                        .findByMobileNo(mobileNo)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found."
                                )
                        );

        boolean isCustomer =
                user.getRoles()
                        .stream()
                        .anyMatch(role ->
                                role.getRole() == UserRoles.USER
                        );

        if (isCustomer) {

            throw new BadRequestException(
                    "Customers login using OTP."
            );
        }

        otpService.sendOtp(
                mobileNo
        );

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "OTP sent successfully."
                )
        );
    }

    @PostMapping("/forgot-password/reset-password")
    public ResponseEntity<?> resetPassword(
            @RequestBody ResetPasswordRequest request
    ) {

        boolean valid =
                otpService.verifyForgotPasswordOtp(
                        request.getMobileNo(),
                        request.getOtp()
                );

        if (!valid) {

            throw new BadRequestException(
                    "Invalid or expired OTP."
            );
        }

        Users user =
                userRepository
                        .findByMobileNo(
                                request.getMobileNo()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found."
                                )
                        );

        boolean isCustomer =
                user.getRoles()
                        .stream()
                        .anyMatch(role ->
                                role.getRole() == UserRoles.USER
                        );

        if (isCustomer) {

            throw new BadRequestException(
                    "Customers login using OTP."
            );
        }

        user.setPassword(
                passwordEncoder.encode(
                        request.getNewPassword()
                )
        );

        userRepository.save(
                user
        );

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Password updated successfully."
                )
        );
    }

}
