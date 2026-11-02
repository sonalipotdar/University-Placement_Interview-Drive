
package com.placement.portal.repository;

import com.placement.portal.mapper.CompanyRowMapper;
import com.placement.portal.model.Company;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CompanyRepository {

    private final JdbcTemplate jdbcTemplate;

    public CompanyRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Add company
    public int addCompany(Company company) {
        String sql = "INSERT INTO companies " +
                "(name, min_cgpa, max_backlogs, package_lpa) " +
                "VALUES (?, ?, ?, ?)";

        return jdbcTemplate.update(
                sql,
                company.getName(),
                company.getMinCgpa(),
                company.getMaxBacklogs(),
                company.getPackageLpa()
        );
    }

    // Get all companies
    public List<Company> getAllCompanies() {
        String sql = "SELECT * FROM companies";
        return jdbcTemplate.query(sql, new CompanyRowMapper());
    }

    // Find company by ID
    public Optional<Company> getCompanyById(int companyId) {
        String sql = "SELECT * FROM companies WHERE company_id = ?";

        List<Company> results = jdbcTemplate.query(
                sql, new CompanyRowMapper(), companyId
        );

        return results.stream().findFirst();
    }

    // Update company
    public int updateCompany(Company company) {
        String sql = "UPDATE companies SET " +
                "name = ?, min_cgpa = ?, max_backlogs = ?, " +
                "package_lpa = ? WHERE company_id = ?";

        return jdbcTemplate.update(
                sql,
                company.getName(),
                company.getMinCgpa(),
                company.getMaxBacklogs(),
                company.getPackageLpa(),
                company.getCompanyId()
        );
    }
}
