package hexlet.code.service;

import hexlet.code.dto.UserDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDTO createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        return toDTO(savedUser);
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("User with id " + id + " not found"));
        return toDTO(user);
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    public UserDTO updateUser(Long id, User partialUser) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("User with id " + id + " not found"));
        if (partialUser.getFirstName() != null) {
            user.setFirstName(partialUser.getFirstName());
        }
        if (partialUser.getLastName() != null) {
            user.setLastName(partialUser.getLastName());
        }
        if (partialUser.getEmail() != null) {
            user.setEmail(partialUser.getEmail());
        }
        if (partialUser.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(partialUser.getPassword()));
        }
        User updatedUser = userRepository.save(user);
        return toDTO(updatedUser);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    private UserDTO toDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getCreatedAt()
        );
    }
}
