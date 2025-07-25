package codesa.school_system_server.controllers;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import codesa.school_system_server.services.UserService;
import codesa.school_system_server.models.dto.UserUpdateDTO;
import codesa.school_system_server.models.dto.UserCreateDTO;
import codesa.school_system_server.error.dto.ResponseMessage;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseMessage getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public ResponseMessage createUser(@Valid @RequestBody UserCreateDTO userDto){
        return userService.createUser(userDto);
    }

    @GetMapping("/{id}")
    public ResponseMessage getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseMessage deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }

    @PutMapping("/{id}")
    public ResponseMessage updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateDTO userDto) {
        return userService.updateUser(id, userDto);
    }
}
