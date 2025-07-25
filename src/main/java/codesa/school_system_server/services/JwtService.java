package codesa.school_system_server.services;

import java.util.Date;
import java.time.Instant;
import io.jsonwebtoken.Jwts;
import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import codesa.school_system_server.models.User;

@Service
public class JwtService {

    private final SecretKey secretKey;

    public JwtService(SecretKey secretKey){
        this.secretKey = secretKey;
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

    private Date extractExpiration(String token) {
        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getExpiration();
    }

    public String extractUserEmail(String token) {
        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();
    }
}
