package codesa.school_system_server.models;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.MapsId;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.NotBlank;
@Entity
@Table(name = "students")
public class Student {
    @Id
    private Long id;

    @OneToOne
    @JoinColumn(name = "id_user")
    @MapsId
    private User user;

    @NotBlank(message = "El número de matrícula es obligatorio")
    private String numero_matricula;

    @NotBlank(message = "El grado es obligatorio")
    private String grado;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

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