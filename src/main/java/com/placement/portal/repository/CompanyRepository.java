

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

    // 1. Add a company
    public int addCompany(Company company) {

        String sql = "INSERT INTO companies " +
                "(name, min_cgpa, max_backlogs, package_lpa) " +
                "VALUES (?, ?, ?, ?)";

        return jdbcTemplate.update(
                sql,
                company.getCompanyName(),
                company.getMinCgpa(),
                company.getMaxActiveBacklogs(),
                company.getPackageOffered()
        );
    }

    // 2. Get all companies
    public List<Company> getAllCompanies() {

        String sql = "SELECT * FROM companies";

        return jdbcTemplate.query(sql, new CompanyRowMapper());
    }

    // 3. Get company by ID
    public Optional<Company> getCompanyById(int companyId) {

        String sql = "SELECT * FROM companies WHERE company_id = ?";

        List<Company> companies = jdbcTemplate.query(
                sql,
                new CompanyRowMapper(),
                companyId
        );

        return companies.stream().findFirst();
    }

    // 4. Update company details
    public int updateCompany(Company company) {

        String sql = "UPDATE companies SET " +
                "name = ?, min_cgpa = ?, max_backlogs = ?, " +
                "package_lpa = ? WHERE company_id = ?";

        return jdbcTemplate.update(
                sql,
                company.getCompanyName(),
                company.getMinCgpa(),
                company.getMaxActiveBacklogs(),
                company.getPackageOffered(),
                company.getCompanyId()
        );
    }

    // 5. Delete company
    public int deleteCompany(int companyId) {

        String sql = "DELETE FROM companies WHERE company_id = ?";

        return jdbcTemplate.update(sql, companyId);
    }
}
