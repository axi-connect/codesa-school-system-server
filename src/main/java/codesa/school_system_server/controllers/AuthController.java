package codesa.school_system_server.controllers;

import codesa.school_system_server.models.LoginRequest;
import codesa.school_system_server.services.AuthService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import codesa.school_system_server.error.dto.ResponseMessage;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("login")
    public ResponseMessage authenticate (@RequestBody final LoginRequest request) {
        final ResponseMessage token = authService.authenticate(request);
        return token;
    }
}
