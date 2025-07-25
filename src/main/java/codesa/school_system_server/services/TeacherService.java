package codesa.school_system_server.services;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import codesa.school_system_server.models.User;
import codesa.school_system_server.models.Teacher;
import codesa.school_system_server.error.dto.ResponseMessage;
import codesa.school_system_server.repositories.UserRepository;
import codesa.school_system_server.models.dto.TeacherRelationDTO;
import codesa.school_system_server.repositories.TeacherRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public TeacherService(TeacherRepository teacherRepository, UserRepository userRepository) {
        this.teacherRepository = teacherRepository;
        this.userRepository = userRepository;
    }

    public ResponseMessage getProfesorById(Long id) {
        try {
            Teacher profesor = teacherRepository.findById(id).orElse(null);
            ResponseMessage response = new ResponseMessage();
            response.setSuccessful(profesor != null);
            response.setData(profesor);
            response.setMessage(profesor != null ? "Profesor encontrado" : "Profesor no encontrado");
            response.setStatus(profesor != null ? HttpStatus.OK : HttpStatus.NOT_FOUND);
            return response;
        } catch (Exception e) {
            ResponseMessage response = new ResponseMessage();
            response.setSuccessful(false);
            response.setData(null);
            response.setMessage("Error al buscar profesor: " + e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            return response;
        }
    }

    // Método utilitario para aplanar Teacher+User
    private java.util.Map<String, Object> mapTeacherToFlatMap(Teacher t) {
        java.util.Map<String, Object> map = new java.util.HashMap<>();
        if (t.getUser() != null) {
            map.put("id_user", t.getUser().getId_user());
            map.put("email", t.getUser().getEmail());
            map.put("nombre", t.getUser().getNombre());
            map.put("apellido", t.getUser().getApellido());
            map.put("telefono", t.getUser().getTelefono());
            map.put("fecha_nacimiento", t.getUser().getFecha_nacimiento());
            map.put("password", null); // Nunca exponer la contraseña
            // Calcular edad
            int edad = 0;
            if (t.getUser().getFecha_nacimiento() != null) {
                edad = java.time.Period.between(t.getUser().getFecha_nacimiento(), java.time.LocalDate.now()).getYears();
            }
            map.put("edad", edad);
        }
        map.put("id", t.getId());
        map.put("especialidad", t.getEspecialidad());
        map.put("fecha_contratacion", t.getFecha_contratacion());
        // Calcular experiencia profesional
        int experiencia = 0;
        if (t.getFecha_contratacion() != null) {
            experiencia = java.time.Period.between(t.getFecha_contratacion(), java.time.LocalDate.now()).getYears();
        }
        map.put("experiencia_profesional", experiencia);
        return map;
    }

    public ResponseMessage getAllTeachers() {
        try {
            List<Teacher> profesores = teacherRepository.findAll();
            List<java.util.Map<String, Object>> result = new java.util.ArrayList<>();
            for (Teacher t : profesores) {
                result.add(mapTeacherToFlatMap(t));
            }
            ResponseMessage response = new ResponseMessage();
            response.setSuccessful(true);
            response.setData(result);
            response.setMessage("Lista de profesores obtenida correctamente");
            response.setStatus(HttpStatus.OK);
            return response;
        } catch (Exception e) {
            ResponseMessage response = new ResponseMessage();
            response.setSuccessful(false);
            response.setData(null);
            response.setMessage("Error al obtener profesores: " + e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            return response;
        }
    }

    public ResponseMessage createTeacherRelation(TeacherRelationDTO dto) {
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

            if (teacherRepository.existsById(user.getId_user())) {
                ResponseMessage response = new ResponseMessage();
                response.setSuccessful(false);
                response.setData(null);
                response.setMessage("Ya existe un profesor asociado a este usuario");
                response.setStatus(HttpStatus.CONFLICT);
                return response;
            }

            Teacher teacher = new Teacher();
            teacher.setUser(user);
            teacher.setEspecialidad(dto.getEspecialidad());
            teacher.setFecha_contratacion(dto.getFecha_contratacion());

            Teacher saved = teacherRepository.save(teacher);
            java.util.Map<String, Object> flat = mapTeacherToFlatMap(saved);
            ResponseMessage response = new ResponseMessage();
            response.setSuccessful(true);
            response.setData(flat);
            response.setMessage("Profesor creado y asociado exitosamente");
            response.setStatus(HttpStatus.CREATED);
            return response;
        } catch (Exception e) {
            ResponseMessage response = new ResponseMessage();
            response.setSuccessful(false);
            response.setData(null);
            response.setMessage("Error al crear relación profesor: " + e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            return response;
        }
    }

    public ResponseMessage updateTeacher(Long id, TeacherRelationDTO dto) {
        try {
            Teacher teacher = teacherRepository.findById(id).orElse(null);
            if (teacher == null) {
                ResponseMessage response = new ResponseMessage();
                response.setSuccessful(false);
                response.setData(null);
                response.setMessage("Profesor no encontrado");
                response.setStatus(HttpStatus.NOT_FOUND);
                return response;
            }
            User user = teacher.getUser();
            if (user == null) {
                ResponseMessage response = new ResponseMessage();
                response.setSuccessful(false);
                response.setData(null);
                response.setMessage("Usuario asociado no encontrado");
                response.setStatus(HttpStatus.NOT_FOUND);
                return response;
            }
            // Actualizar campos de Teacher
            teacher.setEspecialidad(dto.getEspecialidad());
            teacher.setFecha_contratacion(dto.getFecha_contratacion());

            teacherRepository.save(teacher);
            userRepository.save(user);
            java.util.Map<String, Object> flat = mapTeacherToFlatMap(teacher);
            ResponseMessage response = new ResponseMessage();
            response.setSuccessful(true);
            response.setData(flat);
            response.setMessage("Profesor actualizado exitosamente");
            response.setStatus(HttpStatus.OK);
            return response;
        } catch (Exception e) {
            ResponseMessage response = new ResponseMessage();
            response.setSuccessful(false);
            response.setData(null);
            response.setMessage("Error al actualizar profesor: " + e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            return response;
        }
    }

    public ResponseMessage deleteTeacher(Long id) {
        try {
            Teacher teacher = teacherRepository.findById(id).orElse(null);
            if (teacher == null) {
                ResponseMessage response = new ResponseMessage();
                response.setSuccessful(false);
                response.setData(null);
                response.setMessage("Profesor no encontrado");
                response.setStatus(HttpStatus.NOT_FOUND);
                return response;
            }
            teacherRepository.deleteById(id);
            ResponseMessage response = new ResponseMessage();
            response.setSuccessful(true);
            response.setData(null);
            response.setMessage("Profesor eliminado exitosamente");
            response.setStatus(HttpStatus.OK);
            return response;
        } catch (Exception e) {
            ResponseMessage response = new ResponseMessage();
            response.setSuccessful(false);
            response.setData(null);
            response.setMessage("Error al eliminar profesor: " + e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            return response;
        }
    }
} 