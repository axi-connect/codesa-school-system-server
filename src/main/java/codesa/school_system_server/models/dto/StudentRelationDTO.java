package codesa.school_system_server.models.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

public class StudentRelationDTO {
    @NotNull(message = "El id del usuario es obligatorio")
    private Long id_user;

    @NotBlank(message = "El número de matrícula es obligatorio")
    private String numero_matricula;

    @NotBlank(message = "El grado es obligatorio")
    private String grado;

    public Long getId_user() { return id_user; }
    public void setId_user(Long id_user) { this.id_user = id_user; }
    public String getNumero_matricula() { return numero_matricula; }
    public void setNumero_matricula(String numero_matricula) { this.numero_matricula = numero_matricula; }
    public String getGrado() { return grado; }
    public void setGrado(String grado) { this.grado = grado; }
} 