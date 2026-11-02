package com.placement.portal.mapper;

import com.placement.portal.model.Student;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentRowMapper implements RowMapper<Student> {

    @Override
    public Student mapRow(ResultSet rs, int rowNum) throws SQLException {

        Student student = new Student();

        student.setRollNo(rs.getString("roll_no"));
        student.setName(rs.getString("name"));
        student.setCgpa(rs.getDouble("cgpa"));
        student.setActiveBacklogs(rs.getInt("active_backlogs"));
        student.setPlacementStatus(rs.getString("placement_status"));

        return student;
    }
}