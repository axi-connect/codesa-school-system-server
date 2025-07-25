package codesa.school_system_server.services;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import codesa.school_system_server.models.Student;
import org.springframework.stereotype.Service;
import codesa.school_system_server.repositories.StudentRepository;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Map<String, Object> createEstudiante(Student estudiante) {
        Map<String, Object> response = new HashMap<>();
        try {
            Student saved = studentRepository.save(estudiante);
            response.put("successful", true);
            response.put("data", saved);
            response.put("message", "Estudiante creado exitosamente");
        } catch (Exception e) {
            response.put("successful", false);
            response.put("data", null);
            response.put("message", "Error al crear estudiante: " + e.getMessage());
        }
        return response;
    }

    public Map<String, Object> getEstudianteById(Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Student estudiante = studentRepository.findById(id).orElse(null);
            response.put("successful", estudiante != null);
            response.put("data", estudiante);
            response.put("message", estudiante != null ? "Estudiante encontrado" : "Estudiante no encontrado");
        } catch (Exception e) {
            response.put("successful", false);
            response.put("data", null);
            response.put("message", "Error al buscar estudiante: " + e.getMessage());
        }
        return response;
    }

    public Map<String, Object> getAllEstudiantes() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Student> estudiantes = studentRepository.findAll();
            response.put("successful", true);
            response.put("data", estudiantes);
            response.put("message", "Lista de estudiantes obtenida correctamente");
        } catch (Exception e) {
            response.put("successful", false);
            response.put("data", null);
            response.put("message", "Error al obtener estudiantes: " + e.getMessage());
        }
        return response;
    }
} 