package codesa.school_system_server.models;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "students")
@PrimaryKeyJoinColumn(name = "id_user")

public class Student extends User {
    @NotBlank(message = "El número de matrícula es obligatorio")
    private String numero_matricula;

    @NotBlank(message = "El grado es obligatorio")
    private String grado;

    public String getNumero_matricula() {
        return numero_matricula;
    }

    public void setNumero_matricula(String numero_matricula) {
        this.numero_matricula = numero_matricula;
    }

    public String getGrado() {
        return grado;
    }

    public void setGrado(String grado) {
        this.grado = grado;
    }
} 