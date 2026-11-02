package com.placement.portal.web;

import com.placement.portal.model.Student;
import com.placement.portal.repository.StudentRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class StudentPageController {

    private final StudentRepository studentRepository;

    public StudentPageController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @GetMapping("/students-page")
    public String students(Model model) {
        model.addAttribute("students",
                studentRepository.getAllStudents());

        return "students/list";
    }

    @GetMapping("/students-page/add")
    public String addStudentPage(Model model) {
        model.addAttribute("student", new Student());
        return "students/add";
    }

    @PostMapping("/students-page/add")
    public String addStudent(
            @ModelAttribute Student student,
            RedirectAttributes redirectAttributes) {

        studentRepository.addStudent(student);

        redirectAttributes.addFlashAttribute(
                "success", "Student added successfully!");

        return "redirect:/students-page";
    }

    @GetMapping("/students-page/edit/{rollNo}")
    public String editStudentPage(
            @PathVariable String rollNo,
            Model model) {

        Student student = studentRepository
                .getStudentByRollNo(rollNo)
                .orElseThrow(() ->
                        new IllegalArgumentException("Student not found"));

        model.addAttribute("student", student);

        return "students/edit";
    }

    @PostMapping("/students-page/edit/{rollNo}")
    public String updateStudent(
            @PathVariable String rollNo,
            @ModelAttribute Student student,
            RedirectAttributes redirectAttributes) {

        student.setRollNo(rollNo);
        studentRepository.updateStudent(student);

        redirectAttributes.addFlashAttribute(
                "success", "Student updated successfully!");

        return "redirect:/students-page";
    }

    @PostMapping("/students-page/delete/{rollNo}")
    public String deleteStudent(
            @PathVariable String rollNo,
            RedirectAttributes redirectAttributes) {

        studentRepository.deleteStudent(rollNo);

        redirectAttributes.addFlashAttribute(
                "success", "Student deleted successfully!");

        return "redirect:/students-page";
    }
}