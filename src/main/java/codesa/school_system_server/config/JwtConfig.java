package codesa.school_system_server.config;

import javax.crypto.SecretKey;
import io.jsonwebtoken.security.Keys; 
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;

import codesa.school_system_server.models.User;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import codesa.school_system_server.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class JwtConfig {
    private final UserRepository userRepository;

    public JwtConfig(UserRepository userRepository){this.userRepository = userRepository;}

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Bean
    public SecretKey secretKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return email -> {
            final User user = userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException("Usuario no encontrado"));
            return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .build();
        };
    }
}