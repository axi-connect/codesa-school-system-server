package codesa.school_system_server.models.dto;

import codesa.school_system_server.models.User;

public class UserUpdateDTO extends User {
    private String password;

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
} 