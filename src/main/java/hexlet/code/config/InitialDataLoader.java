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
            // Проверяем существование администратора
            var adminEmail = "hexlet@example.com";
            var adminExists = userRepository.findByEmail(adminEmail).isPresent();

            if (!adminExists) {
                User admin = new User();
                admin.setEmail(adminEmail);
                admin.setPassword(passwordEncoder.encode("qwerty"));

                admin.setFirstName("Admin");
                admin.setLastName("System");
                userRepository.save(admin);

                System.out.println("Created default admin user with email: " + adminEmail);
            }
        };
    }
}