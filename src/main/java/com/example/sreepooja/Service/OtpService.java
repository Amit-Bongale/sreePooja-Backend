package com.example.sreepooja.Service;

import com.example.sreepooja.Entity.OtpVerification;
import com.example.sreepooja.Entity.Users;
import com.example.sreepooja.Enum.UserRoles;
import com.example.sreepooja.Enum.UserStatus;
import com.example.sreepooja.ExceptionHandlers.BadRequestException;
import com.example.sreepooja.Repository.OtpRepository;
import com.example.sreepooja.Repository.Users.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

@RequiredArgsConstructor
@Service
public class OtpService {

    // set otp attempts = 5 and resend timer to 60 sec
    private static final int MAX_ATTEMPTS = 5;
    private static final int RESEND_SECONDS = 10;

    private final UsersRepository usersRepository;

    @Autowired
    private OtpRepository otpRepository;

    public void sendOtp(String mobileNo) {
        OtpVerification otp = otpRepository
                .findByMobileNo(mobileNo)
                .orElse(new OtpVerification());

        if (otp.getLastSentAt() != null &&
                Duration.between(otp.getLastSentAt(), LocalDateTime.now()).getSeconds() < RESEND_SECONDS) {
            throw new BadRequestException("OTP resend limit exceeded");
        }

        String code = String.valueOf(100000 + new Random().nextInt(900000));

        otp.setMobileNo(mobileNo);
        otp.setOtp(code);
        otp.setAttempts(0);
        otp.setExpiresAt(LocalDateTime.now().plusMinutes(5));
        otp.setLastSentAt(LocalDateTime.now());

        otpRepository.save(otp);

        // MSG91 (DEV)
        System.out.println("OTP for " + mobileNo + " is: " + code);
    }

    public boolean verifyLoginOtp(String mobileNo, String otpCode) {

        OtpVerification otp = otpRepository
                .findByMobileNo(mobileNo)
                .orElseThrow(() -> new BadRequestException("OTP not sent"));

        if (otp.getAttempts() >= MAX_ATTEMPTS)
            throw new BadRequestException("OTP attempts exceeded");

        otp.setAttempts(otp.getAttempts() + 1);
        otpRepository.save(otp);

        if (otp.getExpiresAt().isBefore(LocalDateTime.now()))
            return false;

        if (!otp.getOtp().equals(otpCode))
            return false;

        Users user = usersRepository.findByMobileNo(mobileNo)
                .orElseThrow(() -> new BadRequestException("User not found."));

        if (user.getStatus() != UserStatus.ACTIVE) {

            boolean isCustomer = user.getRoles()
                    .stream()
                    .anyMatch(role -> role.getRole() == UserRoles.USER);

            if (isCustomer) {
                throw new BadRequestException(
                        "Your account is inactive. Please contact Customer Service."
                );
            }

            throw new BadRequestException(
                    "Your account is inactive. Please contact your Administrator."
            );
        }

        return true;
    }

    public boolean verifySignupOtp(String mobileNo, String otpCode) {

        OtpVerification otp = otpRepository
                .findByMobileNo(mobileNo)
                .orElseThrow(() -> new BadRequestException("OTP not sent"));

        if (otp.getAttempts() >= MAX_ATTEMPTS)
            throw new BadRequestException("OTP attempts exceeded");

        otp.setAttempts(otp.getAttempts() + 1);
        otpRepository.save(otp);

        if (otp.getExpiresAt().isBefore(LocalDateTime.now()))
            return false;

        if (!otp.getOtp().equals(otpCode))
            return false;

        return true;
    }
}

