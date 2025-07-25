package codesa.school_system_server.services;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import org.springframework.stereotype.Service;
import codesa.school_system_server.models.Teacher;
import codesa.school_system_server.repositories.TeacherRepository;

@Service
public class TeacherService {
    private final TeacherRepository teacherRepository;

    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    public Map<String, Object> createProfesor(Teacher profesor) {
        Map<String, Object> response = new HashMap<>();
        try {
            Teacher saved = teacherRepository.save(profesor);
            response.put("successful", true);
            response.put("data", saved);
            response.put("message", "Profesor creado exitosamente");
        } catch (Exception e) {
            response.put("successful", false);
            response.put("data", null);
            response.put("message", "Error al crear profesor: " + e.getMessage());
        }
        return response;
    }

    public Map<String, Object> getProfesorById(Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Teacher profesor = teacherRepository.findById(id).orElse(null);
            response.put("successful", profesor != null);
            response.put("data", profesor);
            response.put("message", profesor != null ? "Profesor encontrado" : "Profesor no encontrado");
        } catch (Exception e) {
            response.put("successful", false);
            response.put("data", null);
            response.put("message", "Error al buscar profesor: " + e.getMessage());
        }
        return response;
    }

    public Map<String, Object> getAllProfesores() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Teacher> profesores = teacherRepository.findAll();
            response.put("successful", true);
            response.put("data", profesores);
            response.put("message", "Lista de profesores obtenida correctamente");
        } catch (Exception e) {
            response.put("successful", false);
            response.put("data", null);
            response.put("message", "Error al obtener profesores: " + e.getMessage());
        }
        return response;
    }
} 