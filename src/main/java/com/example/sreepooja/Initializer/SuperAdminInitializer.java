package com.example.sreepooja.Initializer;

import com.example.sreepooja.Config.SuperAdminProperties;
import com.example.sreepooja.Entity.UserRole;
import com.example.sreepooja.Entity.Users;
import com.example.sreepooja.Enum.UserRoles;
import com.example.sreepooja.Enum.UserStatus;
import com.example.sreepooja.Repository.Users.UserRoleRepository;
import com.example.sreepooja.Repository.Users.UsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SuperAdminInitializer implements CommandLineRunner {

    private final UsersRepository usersRepository;
    private final UserRoleRepository userRoleRepository;
    private final SuperAdminProperties properties;
    private final PasswordEncoder passwordEncoder;

    private static final Logger log =
            LoggerFactory.getLogger(SuperAdminInitializer.class);

    public SuperAdminInitializer(
            UsersRepository usersRepository,
            UserRoleRepository userRoleRepository,
            SuperAdminProperties properties,
            PasswordEncoder passwordEncoder
    ) {
        this.usersRepository = usersRepository;
        this.userRoleRepository = userRoleRepository;
        this.properties = properties;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        if (userRoleRepository.existsByRole(UserRoles.SUPER_ADMIN)) {
            return;
        }

        Users user = new Users();
        user.setFirstName(properties.getFirstName());
        user.setLastName(properties.getLastName());
        user.setMobileNo(properties.getMobileNo());
        user.setPassword(
                passwordEncoder.encode(
                        properties.getPassword()
                )
        );
        user.setStatus(UserStatus.ACTIVE);

        Users savedUser = usersRepository.save(user);

        UserRole role = new UserRole();
        role.setUser(savedUser);
        role.setRole(UserRoles.SUPER_ADMIN);

        userRoleRepository.save(role);

        log.info("SUPER_ADMIN created successfully.");
    }
}