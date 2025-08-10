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
            if (userRepository.findByEmail("hexlet@example.com").isEmpty()) {
                User admin = new User();
                admin.setEmail("hexlet@example.com");
                admin.setPassword(passwordEncoder.encode("qwerty"));
                userRepository.save(admin);
            }
        };
    }
}
