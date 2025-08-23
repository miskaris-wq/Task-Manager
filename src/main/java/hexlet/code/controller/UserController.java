package hexlet.code.controller;

import hexlet.code.dto.User.UserCreateDTO;
import hexlet.code.dto.User.UserDTO;
import hexlet.code.dto.User.UserUpdateDTO;
import hexlet.code.service.interfaces.UserService;
import hexlet.code.utils.UserUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserUtils userUtils;

    private static final String ONLY_OWNER_BY_ID = """
                @userUtils.getCurrentUser().getId() == #id
            """;

    @GetMapping
    ResponseEntity<List<UserDTO>> index() {
        var users = userService.getAll();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(users.size()))
                .body(users);
    }

    @GetMapping(path = "/{id}")
    public UserDTO show(@PathVariable("id") long id) {
        return userService.getById(id);
    }

    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO create(@Valid @RequestBody UserCreateDTO data) {
        return userService.create(data);
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize(ONLY_OWNER_BY_ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable("id") long id) {
        userService.delete(id);
    }

    @PutMapping(path = "/{id}")
    @PreAuthorize(ONLY_OWNER_BY_ID)
    public UserDTO update(@PathVariable("id") long id, @RequestBody @Valid UserUpdateDTO data) {
        return userService.update(id, data);
    }
}
