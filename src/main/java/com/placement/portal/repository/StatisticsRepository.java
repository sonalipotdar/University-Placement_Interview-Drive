

package com.placement.portal.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class StatisticsRepository {

    private final JdbcTemplate jdbcTemplate;

    // Constructor injection
    public StatisticsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 1. Get total number of students
    public int getTotalStudents() {
        String sql = "SELECT COUNT(*) FROM students";

        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    // 2. Get total number of companies
    public int getTotalCompanies() {
        String sql = "SELECT COUNT(*) FROM companies";

        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    // 3. Get total number of placed students
    public int getTotalPlacedStudents() {
        String sql = """
                SELECT COUNT(*)
                FROM students
                WHERE placement_status = 'PLACED'
                """;

        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    // 4. Get total number of offers
    public int getTotalOffers() {
        String sql = """
                SELECT COUNT(*)
                FROM applications
                WHERE interview_status = 'OFFERED'
                """;

        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    // 5. Get average placement package
    public double getAveragePackage() {
        String sql = """
                SELECT AVG(c.package_lpa)
                FROM applications a
                JOIN companies c
                ON a.company_id = c.company_id
                WHERE a.interview_status = 'OFFERED'
                """;

        Double result = jdbcTemplate.queryForObject(
                sql, Double.class);

        return result == null ? 0.0 : result;
    }
}
