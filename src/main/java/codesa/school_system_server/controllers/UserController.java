package codesa.school_system_server.controllers;

import jakarta.validation.Valid;
import codesa.school_system_server.error.dto.ResponseMessage;
import codesa.school_system_server.models.User;
import org.springframework.web.bind.annotation.*;
import codesa.school_system_server.services.UserService;

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
    public ResponseMessage createUser(@Valid @RequestBody User user){
        return userService.createUser(user);
    }

    @GetMapping("/{id}")
    public ResponseMessage getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseMessage deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }
}
