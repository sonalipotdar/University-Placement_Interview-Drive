
package com.placement.portal.controller;

import com.placement.portal.model.Student;
import com.placement.portal.repository.StudentRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // Get all students
    @GetMapping
    public List<Student> getAllStudents() {
        return studentRepository.getAllStudents();
    }

    // Get student by roll number
    @GetMapping("/{rollNo}")
    public ResponseEntity<Student> getStudentByRollNo(
            @PathVariable String rollNo) {

        return studentRepository.getStudentByRollNo(rollNo)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Add a student
    @PostMapping
    public ResponseEntity<Student> addStudent(
            @RequestBody Student student) {

        studentRepository.addStudent(student);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(student);
    }

    // Update student
    @PutMapping("/{rollNo}")
    public ResponseEntity<Student> updateStudent(
            @PathVariable String rollNo,
            @RequestBody Student student) {

        student.setRollNo(rollNo);

        int updated = studentRepository.updateStudent(student);

        if (updated == 0) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(student);
    }

    // Delete student
    @DeleteMapping("/{rollNo}")
    public ResponseEntity<Void> deleteStudent(
            @PathVariable String rollNo) {

        boolean deleted = studentRepository.deleteStudent(rollNo);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}
