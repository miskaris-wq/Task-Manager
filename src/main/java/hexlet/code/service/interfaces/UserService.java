package hexlet.code.service.interfaces;

import hexlet.code.dto.User.UserCreateDTO;
import hexlet.code.dto.User.UserDTO;
import hexlet.code.dto.User.UserUpdateDTO;

import java.util.List;

public interface UserService {
    List<UserDTO> getAll();
    UserDTO getById(Long id);
    UserDTO create(UserCreateDTO createDTO);
    UserDTO update(Long id, UserUpdateDTO updateDTO);
    void delete(Long id);
}
