package hexlet.code.service.impl;

import hexlet.code.dto.User.UserCreateDTO;
import hexlet.code.dto.User.UserDTO;
import hexlet.code.dto.User.UserUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.UserMapper;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private UserMapper userMapper;

    @Override
    public List<UserDTO> getAll() {
        var users = userRepository.findAll();
        return users.stream()
                .map(userMapper::map)
                .toList();
    }

    @Override
    public UserDTO getById(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
        return userMapper.map(user);
    }

    @Override
    public UserDTO create(UserCreateDTO createDTO) {
        var user = userMapper.map(createDTO);
        user = userRepository.save(user);
        return userMapper.map(user);
    }

    @Override
    public UserDTO update(Long id, UserUpdateDTO updateDTO) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
        userMapper.update(updateDTO, user);
        user = userRepository.save(user);
        return userMapper.map(user);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
