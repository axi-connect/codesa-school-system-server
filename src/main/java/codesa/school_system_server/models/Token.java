package codesa.school_system_server.models;

import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.GeneratedValue;

@Entity
@Table(name = "tokens")

public class Token {

    public enum TokenType {
        BEARER
    }
	
    @Id
    @GeneratedValue
    public Long id;
    
    @Column(unique = true)
    public String token;

    @ManyToOne
    @JoinColumn(name = "id_user", referencedColumnName = "id_user")
    private User user; 

    public boolean expired;
    public TokenType tokenType = TokenType.BEARER;

    public Token() {}

    public Token(String token, User user) {
        this.token = token;
        this.user = user;
        this.expired = false;
        this.tokenType = TokenType.BEARER;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }
}
