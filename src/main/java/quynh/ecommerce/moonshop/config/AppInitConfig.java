package quynh.ecommerce.moonshop.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import quynh.ecommerce.moonshop.common.enums.Role;
import quynh.ecommerce.moonshop.common.enums.UserStatus;
import quynh.ecommerce.moonshop.user.entity.User;
import quynh.ecommerce.moonshop.user.repository.UserRepository;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class AppInitConfig {
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository){
        log.info("Initializing application...");
        return args -> {
            if(userRepository.findByEmail(adminEmail).isEmpty()){
                User user = User.builder()
                        .email(adminEmail)
                        .fullName("admin")
                        .password(passwordEncoder.encode(adminPassword))
                        .role(Role.ADMIN)
                        .userStatus(UserStatus.ACTIVE)
                        .build();
                userRepository.save(user);
                log.warn("admin user has been created, please rotate the initial password after setup");
            }
            log.info("Application initialization completed .....");
        };
    }
}
