package hexlet.code.config;

import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class InitialDataLoader {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public ApplicationRunner loadInitialData() {
        return args -> {
            String rawPassword = "qwerty";
            String encodedPassword = passwordEncoder.encode(rawPassword);

            System.out.println("======= DEBUG =======");
            System.out.println("Raw password: qwerty");
            System.out.println("Encoded password: " + encodedPassword);
            System.out.println("====================");

            var adminEmail = "hexlet@example.com";
            if (!userRepository.findByEmail(adminEmail).isPresent()) {
                User admin = new User();
                admin.setEmail(adminEmail);
                admin.setPasswordDigest(encodedPassword);
                admin.setFirstName("Admin");
                admin.setLastName("System");
                userRepository.save(admin);
            }
        };
    }

}