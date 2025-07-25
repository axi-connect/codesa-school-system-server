package codesa.school_system_server.services;

import java.time.Instant;
import java.util.Date;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import codesa.school_system_server.models.User;
import codesa.school_system_server.models.Token;
import codesa.school_system_server.models.LoginRequest;
import codesa.school_system_server.models.TokenResponse;
import codesa.school_system_server.error.dto.ResponseMessage;
import codesa.school_system_server.repositories.UserRepository;
import codesa.school_system_server.repositories.TokenRepository;
import codesa.school_system_server.error.RestResponseExceptionHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Service
public class AuthService {
    private final SecretKey secretKey;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;  
    private final AuthenticationManager authenticatorManager;
    private final RestResponseExceptionHandler restResponseExceptionHandler;

    public AuthService(
        SecretKey secretKey,
        UserRepository userRepository, 
        TokenRepository tokenRepository, 
        AuthenticationManager authenticatorManager,
        RestResponseExceptionHandler restResponseExceptionHandler
    ) {
        this.secretKey = secretKey;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.authenticatorManager = authenticatorManager;
        this.restResponseExceptionHandler = restResponseExceptionHandler;
    }

    public ResponseMessage authenticate(LoginRequest request) {
        try {
            if (request.email() == null || request.password() == null) {
                return restResponseExceptionHandler.handleCustomException("Email y contraseña son obligatorios", HttpStatus.BAD_REQUEST).getBody();
            }

            authenticatorManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.email(),
                    request.password()
                )
            );

            User user = userRepository.findByEmail(request.email()).get();
            String jwtToken = generateToken(user);
            saveUserToken(user,jwtToken);
            
            ResponseMessage response = new ResponseMessage();
            TokenResponse data = new TokenResponse(jwtToken, "Bearer");
            
            response.setData(data);
            response.setStatus(HttpStatus.OK);
            response.setSuccessful(true);
            response.setMessage("Login exitoso");

            return response;
        } catch (Exception e) {
            return restResponseExceptionHandler.handleCustomException("Error al autenticar usuario: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR).getBody();
        }
    }

    public String generateToken(User user) {
        return Jwts.builder()
        .id(user.getId_user().toString())
        .claim("name", user.getNombre())
        .subject(user.getEmail())
        .signWith(secretKey)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(Date.from(Instant.now().plusMillis(86400000)))
        .compact();
    }

    public boolean isTokenValid(final String token, final User user) {
        final String email = extractUserEmail(token);
        return (email.equals(user.getEmail())) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String extractUserEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private void saveUserToken(User user, String jwtToken){
        Token token = new Token(jwtToken, user);
        this.tokenRepository.save(token);
    }
}