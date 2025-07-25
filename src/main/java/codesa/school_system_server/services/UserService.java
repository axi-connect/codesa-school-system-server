package codesa.school_system_server.services;

import java.util.List;
import org.springframework.stereotype.Service;
import codesa.school_system_server.models.User;
import codesa.school_system_server.repositories.UserRepository;
import codesa.school_system_server.error.RestResponseExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import codesa.school_system_server.error.dto.ResponseMessage;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RestResponseExceptionHandler exceptionHandler;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RestResponseExceptionHandler exceptionHandler, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.exceptionHandler = exceptionHandler;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseMessage createUser(User user) {
        try {
            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                return exceptionHandler.handleCustomException("El email ya está registrado", HttpStatus.BAD_REQUEST).getBody();
            }
            
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User saved = userRepository.save(user);
            ResponseMessage response = new ResponseMessage();
            saved.setPassword(null);
            response.setData(saved);
            response.setSuccessful(true);
            response.setMessage("Usuario creado exitosamente");
            response.setStatus(HttpStatus.CREATED);
            return response;
        } catch (Exception e) {
            return exceptionHandler.handleCustomException("Error al crear usuario: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR).getBody();
        }
    }

    public ResponseMessage getUserById(Long id) {
        try {
            User user = userRepository.findById(id).orElse(null);
            ResponseMessage response = new ResponseMessage();
            response.setData(user);
            response.setSuccessful(user != null);
            response.setMessage(user != null ? "Usuario encontrado" : "Usuario no encontrado");
            response.setStatus(user != null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
            return response;
        } catch (Exception e) {
            return exceptionHandler.handleCustomException("Error al buscar usuario: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR).getBody();
        }
    }

    public ResponseMessage getAllUsers() {
        try {
            List<User> users = userRepository.findAll();
            ResponseMessage response = new ResponseMessage();
            response.setData(users);
            response.setSuccessful(true);
            response.setMessage("Lista de usuarios obtenida correctamente");
            response.setStatus(HttpStatus.OK);
            return response;
        } catch (Exception e) {
            return exceptionHandler.handleCustomException("Error al obtener usuarios: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR).getBody();
        }
    }

    public ResponseMessage deleteUser(Long id) {
        try {
            ResponseMessage response = new ResponseMessage();
            if (userRepository.existsById(id)) {
                userRepository.deleteById(id);
                response.setSuccessful(true);
                response.setData(null);
                response.setMessage("Usuario eliminado exitosamente");
                response.setStatus(HttpStatus.OK);
            } else {
                response.setSuccessful(false);
                response.setData(null);
                response.setMessage("Usuario no encontrado");
                response.setStatus(HttpStatus.NOT_FOUND);
            }
            return response;
        } catch (Exception e) {
            return exceptionHandler.handleCustomException("Error al eliminar usuario: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR).getBody();
        }
    }
}
