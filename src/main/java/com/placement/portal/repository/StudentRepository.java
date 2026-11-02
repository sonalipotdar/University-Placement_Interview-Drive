package com.placement.portal.repository;

import com.placement.portal.mapper.StudentRowMapper;
import com.placement.portal.model.Student;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class StudentRepository {

    private final JdbcTemplate jdbcTemplate;

    public StudentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Add a new student
    public int addStudent(Student student) {

        String sql = "INSERT INTO students " +
                "(roll_no, name, cgpa, active_backlogs, placement_status) " +
                "VALUES (?, ?, ?, ?, ?)";

        return jdbcTemplate.update(
                sql,
                student.getRollNo(),
                student.getName(),
                student.getCgpa(),
                student.getActiveBacklogs(),
                student.getPlacementStatus()
        );
    }

    // Get all students
    public List<Student> getAllStudents() {

        String sql = "SELECT * FROM students";

        return jdbcTemplate.query(sql, new StudentRowMapper());
    }

    // Find a student by roll number
    public Optional<Student> getStudentByRollNo(String rollNo) {

        String sql = "SELECT * FROM students WHERE roll_no = ?";

        try {
            Student student = jdbcTemplate.queryForObject(
                    sql,
                    new StudentRowMapper(),
                    rollNo
            );

            return Optional.ofNullable(student);

        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    // Update student details
    public int updateStudent(Student student) {

        String sql = "UPDATE students SET " +
                "name = ?, cgpa = ?, active_backlogs = ?, " +
                "placement_status = ? WHERE roll_no = ?";

        return jdbcTemplate.update(
                sql,
                student.getName(),
                student.getCgpa(),
                student.getActiveBacklogs(),
                student.getPlacementStatus(),
                student.getRollNo()
        );
    }

    // Delete a student by roll number
    public boolean deleteStudent(String rollNo) {

        String sql = "DELETE FROM students WHERE roll_no = ?";

        return jdbcTemplate.update(sql, rollNo) > 0;
    }
}
