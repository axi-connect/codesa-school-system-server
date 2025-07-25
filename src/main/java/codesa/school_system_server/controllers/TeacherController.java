package codesa.school_system_server.controllers;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import codesa.school_system_server.services.TeacherService;
import codesa.school_system_server.error.dto.ResponseMessage;
import codesa.school_system_server.models.dto.TeacherRelationDTO;

@RestController
@RequestMapping("/api/teachers")

public class TeacherController {
    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping
    public ResponseMessage getAllTeachers() {
        return teacherService.getAllTeachers();
    }

    @GetMapping("/{id}")
    public ResponseMessage getTeacherById(@PathVariable Long id) {
        return teacherService.getProfesorById(id);
    }

    @PostMapping
    public ResponseMessage createTeacherRelation(@Valid @RequestBody TeacherRelationDTO dto) {
        return teacherService.createTeacherRelation(dto);
    }

    @PutMapping("/{id}")
    public ResponseMessage updateTeacher(@PathVariable Long id, @Valid @RequestBody TeacherRelationDTO dto) {
        return teacherService.updateTeacher(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseMessage deleteTeacher(@PathVariable Long id) {
        return teacherService.deleteTeacher(id);
    }
}
