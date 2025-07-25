package codesa.school_system_server.config;

import java.io.IOException;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import codesa.school_system_server.models.Token;
import codesa.school_system_server.models.User;
import codesa.school_system_server.repositories.TokenRepository;
import codesa.school_system_server.repositories.UserRepository;
import codesa.school_system_server.services.JwtService;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {


    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final UserDetailsService userDetailsService;

    public JwtAuthFilter(JwtService jwtService, TokenRepository tokenRepository, UserDetailsService userDetailsService, UserRepository userRepository){
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        
        if (request.getServletPath().contains("/api/auth")){
            filterChain.doFilter(request, response);
            return;
        }
        
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);
        String userEmail = jwtService.extractUserEmail(jwt);
        
        if(userEmail == null){
            return;
        }

        final Token token = tokenRepository.findByToken(jwt);

        if(token == null || token.isExpired()){
            filterChain.doFilter(request, response);
            return;
        }
        
        final UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
        final Optional<User> user = this.userRepository.findByEmail(userDetails.getUsername());

        if(user.isEmpty()){
            filterChain.doFilter(request, response);
            return;
        }

        if(!jwtService.isTokenValid(jwt, user.get())){
            return;
        }

        final var authToken = new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.getAuthorities()
        );

        authToken.setDetails(
            new WebAuthenticationDetailsSource().buildDetails(request)
        );

        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);
    }
}
