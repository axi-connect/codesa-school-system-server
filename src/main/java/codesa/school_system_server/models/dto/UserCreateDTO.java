package codesa.school_system_server.models.dto;

import jakarta.validation.constraints.Size;
import codesa.school_system_server.models.User;
import jakarta.validation.constraints.NotBlank;

public class UserCreateDTO extends User {
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
} 