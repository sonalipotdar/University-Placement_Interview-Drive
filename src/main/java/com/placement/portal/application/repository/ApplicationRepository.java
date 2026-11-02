package com.placement.portal.application.repository;

import com.placement.portal.application.entity.Application;
import com.placement.portal.application.mapper.ApplicationRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class ApplicationRepository {

    private static final String SELECT_COLUMNS =
            "SELECT app_id, roll_no, company_id, interview_status FROM applications ";

    private final JdbcTemplate jdbcTemplate;
    private final ApplicationRowMapper rowMapper = new ApplicationRowMapper();

    public ApplicationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Application save(Application application) {
        String sql = "INSERT INTO applications (roll_no, company_id, interview_status) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        PreparedStatementCreator statementCreator = (Connection connection) -> {
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, application.getRollNo());
            statement.setInt(2, application.getCompanyId());
            statement.setString(3, application.getInterviewStatus());
            return statement;
        };

        jdbcTemplate.update(statementCreator, keyHolder);
        Number generatedId = keyHolder.getKey();
        if (generatedId == null) {
            throw new IllegalStateException("The database did not return an application ID.");
        }

        application.setAppId(generatedId.intValue());
        return application;
    }

    public List<Application> findAll() {
        return jdbcTemplate.query(SELECT_COLUMNS + "ORDER BY app_id", rowMapper);
    }

    public Optional<Application> findById(int appId) {
        List<Application> applications = jdbcTemplate.query(
                SELECT_COLUMNS + "WHERE app_id = ?",
                rowMapper,
                appId
        );
        return applications.stream().findFirst();
    }

    public List<Application> findByStudentRollNo(String rollNo) {
        return jdbcTemplate.query(
                SELECT_COLUMNS + "WHERE roll_no = ? ORDER BY app_id",
                rowMapper,
                rollNo
        );
    }

    public boolean updateStatus(int appId, String interviewStatus) {
        int updatedRows = jdbcTemplate.update(
                "UPDATE applications SET interview_status = ? WHERE app_id = ?",
                interviewStatus,
                appId
        );
        return updatedRows > 0;
    }

    public boolean applicationExists(String rollNo, int companyId) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM applications WHERE roll_no = ? AND company_id = ?",
                Integer.class,
                rollNo,
                companyId
        );
        return count != null && count > 0;
    }

    public boolean studentExists(String rollNo) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM students WHERE roll_no = ?",
                Integer.class,
                rollNo
        );
        return count != null && count > 0;
    }

    public boolean companyExists(int companyId) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM companies WHERE company_id = ?",
                Integer.class,
                companyId
        );
        return count != null && count > 0;
    }

    public boolean studentIsEligible(String rollNo, int companyId) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) " +
                        "FROM students s " +
                        "JOIN companies c ON c.company_id = ? " +
                        "WHERE s.roll_no = ? " +
                        "AND s.cgpa >= c.min_cgpa " +
                        "AND s.active_backlogs <= c.max_backlogs " +
                        "AND s.placement_status = 'UNPLACED'",
                Integer.class,
                companyId,
                rollNo
        );
        return count != null && count > 0;
    }
}
