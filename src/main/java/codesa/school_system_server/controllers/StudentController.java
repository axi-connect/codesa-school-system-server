package codesa.school_system_server.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.validation.Valid;
import codesa.school_system_server.services.StudentService;
import codesa.school_system_server.error.dto.ResponseMessage;
import codesa.school_system_server.models.dto.StudentRelationDTO;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public ResponseMessage getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    public ResponseMessage getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }

    @PostMapping
    public ResponseMessage createStudent(@Valid @RequestBody StudentRelationDTO dto) {
        return studentService.createStudent(dto);
    }

    @PutMapping("/{id}")
    public ResponseMessage updateStudent(@PathVariable Long id, @Valid @RequestBody StudentRelationDTO dto) {
        return studentService.updateStudent(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseMessage deleteStudent(@PathVariable Long id) {
        return studentService.deleteStudent(id);
    }
}
