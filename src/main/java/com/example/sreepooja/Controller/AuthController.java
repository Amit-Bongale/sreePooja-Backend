package com.example.sreepooja.Controller;

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

        boolean otpValid = otpService.verifyOtp(
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

        if (mobileNo == null || mobileNo.length() < 10) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", "Invalid mobile number"));
        }

        if(userRepository.findByMobileNo(mobileNo).isEmpty()){
            throw new ResourceNotFoundException("User not Registered, Please Register to Login");
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

        boolean isValid = otpService.verifyOtp(mobileNo, otp);

        if (!isValid) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid or expired OTP"));
        }

        // Load User
        Users user = userRepository.findByMobileNo(mobileNo)
                .orElseThrow(() ->
                        new RuntimeException("User not registered"));

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

}
