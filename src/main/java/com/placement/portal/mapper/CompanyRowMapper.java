
package com.placement.portal.mapper;

import com.placement.portal.model.Company;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CompanyRowMapper implements RowMapper<Company> {

    @Override
    public Company mapRow(ResultSet rs, int rowNum)
            throws SQLException {

        Company company = new Company();

        company.setCompanyId(rs.getInt("company_id"));
        company.setName(rs.getString("name"));
        company.setMinCgpa(rs.getDouble("min_cgpa"));
        company.setMaxBacklogs(rs.getInt("max_backlogs"));
        company.setPackageLpa(rs.getDouble("package_lpa"));

        return company;
    }
}
