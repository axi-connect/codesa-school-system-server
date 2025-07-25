package codesa.school_system_server.services;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import codesa.school_system_server.models.Student;
import codesa.school_system_server.models.User;
import codesa.school_system_server.repositories.StudentRepository;
import codesa.school_system_server.repositories.UserRepository;
import codesa.school_system_server.models.dto.StudentRelationDTO;
import codesa.school_system_server.error.dto.ResponseMessage;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    public StudentService(StudentRepository studentRepository, UserRepository userRepository) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
    }

    // Utilitario para aplanar Student+User
    private Map<String, Object> mapStudentToFlatMap(Student s) {
        Map<String, Object> map = new HashMap<>();
        if (s.getUser() != null) {
            map.put("id_user", s.getUser().getId_user());
            map.put("email", s.getUser().getEmail());
            map.put("nombre", s.getUser().getNombre());
            map.put("apellido", s.getUser().getApellido());
            map.put("telefono", s.getUser().getTelefono());
            map.put("fecha_nacimiento", s.getUser().getFecha_nacimiento());
            map.put("password", null);
            // Calcular edad
            int edad = 0;
            if (s.getUser().getFecha_nacimiento() != null) {
                edad = java.time.Period.between(s.getUser().getFecha_nacimiento(), java.time.LocalDate.now()).getYears();
            }
            map.put("edad", edad);
        }
        map.put("id", s.getId());
        map.put("numero_matricula", s.getNumero_matricula());
        map.put("grado", s.getGrado());
        return map;
    }

    public ResponseMessage createStudent(StudentRelationDTO dto) {
        try {
            User user = userRepository.findById(dto.getId_user()).orElse(null);
            if (user == null) {
                ResponseMessage response = new ResponseMessage();
                response.setSuccessful(false);
                response.setData(null);
                response.setMessage("Usuario no encontrado");
                response.setStatus(HttpStatus.NOT_FOUND);
                return response;
            }
            if (studentRepository.existsById(user.getId_user())) {
                ResponseMessage response = new ResponseMessage();
                response.setSuccessful(false);
                response.setData(null);
                response.setMessage("Ya existe un estudiante asociado a este usuario");
                response.setStatus(HttpStatus.CONFLICT);
                return response;
            }
            Student student = new Student();
            student.setUser(user);
            student.setNumero_matricula(dto.getNumero_matricula());
            student.setGrado(dto.getGrado());
            Student saved = studentRepository.save(student);
            Map<String, Object> flat = mapStudentToFlatMap(saved);
            ResponseMessage response = new ResponseMessage();
            response.setSuccessful(true);
            response.setData(flat);
            response.setMessage("Estudiante creado y asociado exitosamente");
            response.setStatus(HttpStatus.CREATED);
            return response;
        } catch (Exception e) {
            ResponseMessage response = new ResponseMessage();
            response.setSuccessful(false);
            response.setData(null);
            response.setMessage("Error al crear estudiante: " + e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            return response;
        }
    }

    public ResponseMessage getStudentById(Long id) {
        try {
            Student student = studentRepository.findById(id).orElse(null);
            ResponseMessage response = new ResponseMessage();
            if (student == null) {
                response.setSuccessful(false);
                response.setData(null);
                response.setMessage("Estudiante no encontrado");
                response.setStatus(HttpStatus.NOT_FOUND);
            } else {
                response.setSuccessful(true);
                response.setData(mapStudentToFlatMap(student));
                response.setMessage("Estudiante encontrado");
                response.setStatus(HttpStatus.OK);
            }
            return response;
        } catch (Exception e) {
            ResponseMessage response = new ResponseMessage();
            response.setSuccessful(false);
            response.setData(null);
            response.setMessage("Error al buscar estudiante: " + e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            return response;
        }
    }

    public ResponseMessage getAllStudents() {
        try {
            List<Student> students = studentRepository.findAll();
            List<Map<String, Object>> result = new ArrayList<>();
            for (Student s : students) {
                result.add(mapStudentToFlatMap(s));
            }
            ResponseMessage response = new ResponseMessage();
            response.setSuccessful(true);
            response.setData(result);
            response.setMessage("Lista de estudiantes obtenida correctamente");
            response.setStatus(HttpStatus.OK);
            return response;
        } catch (Exception e) {
            ResponseMessage response = new ResponseMessage();
            response.setSuccessful(false);
            response.setData(null);
            response.setMessage("Error al obtener estudiantes: " + e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            return response;
        }
    }

    public ResponseMessage updateStudent(Long id, StudentRelationDTO dto) {
        try {
            Student student = studentRepository.findById(id).orElse(null);
            if (student == null) {
                ResponseMessage response = new ResponseMessage();
                response.setSuccessful(false);
                response.setData(null);
                response.setMessage("Estudiante no encontrado");
                response.setStatus(HttpStatus.NOT_FOUND);
                return response;
            }
            User user = student.getUser();
            if (user == null) {
                ResponseMessage response = new ResponseMessage();
                response.setSuccessful(false);
                response.setData(null);
                response.setMessage("Usuario asociado no encontrado");
                response.setStatus(HttpStatus.NOT_FOUND);
                return response;
            }
            student.setNumero_matricula(dto.getNumero_matricula());
            student.setGrado(dto.getGrado());
            studentRepository.save(student);
            userRepository.save(user);
            Map<String, Object> flat = mapStudentToFlatMap(student);
            ResponseMessage response = new ResponseMessage();
            response.setSuccessful(true);
            response.setData(flat);
            response.setMessage("Estudiante actualizado exitosamente");
            response.setStatus(HttpStatus.OK);
            return response;
        } catch (Exception e) {
            ResponseMessage response = new ResponseMessage();
            response.setSuccessful(false);
            response.setData(null);
            response.setMessage("Error al actualizar estudiante: " + e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            return response;
        }
    }

    public ResponseMessage deleteStudent(Long id) {
        try {
            Student student = studentRepository.findById(id).orElse(null);
            if (student == null) {
                ResponseMessage response = new ResponseMessage();
                response.setSuccessful(false);
                response.setData(null);
                response.setMessage("Estudiante no encontrado");
                response.setStatus(HttpStatus.NOT_FOUND);
                return response;
            }
            studentRepository.deleteById(id);
            ResponseMessage response = new ResponseMessage();
            response.setSuccessful(true);
            response.setData(null);
            response.setMessage("Estudiante eliminado exitosamente");
            response.setStatus(HttpStatus.OK);
            return response;
        } catch (Exception e) {
            ResponseMessage response = new ResponseMessage();
            response.setSuccessful(false);
            response.setData(null);
            response.setMessage("Error al eliminar estudiante: " + e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            return response;
        }
    }
} 