package codesa.school_system_server.models.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class TeacherRelationDTO {
    @NotNull(message = "El id del usuario es obligatorio")
    private Long id_user;

    @NotNull(message = "La especialidad es obligatoria")
    private String especialidad;

    @NotNull(message = "La fecha de contratación es obligatoria")
    private LocalDate fecha_contratacion;

    public Long getId_user() { return id_user; }
    public void setId_user(Long id_user) { this.id_user = id_user; }
    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }
    public LocalDate getFecha_contratacion() { return fecha_contratacion; }
    public void setFecha_contratacion(LocalDate fecha_contratacion) { this.fecha_contratacion = fecha_contratacion; }
} 