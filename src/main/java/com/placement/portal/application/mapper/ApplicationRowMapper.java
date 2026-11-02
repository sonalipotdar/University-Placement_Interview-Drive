package com.placement.portal.application.mapper;

import com.placement.portal.application.entity.Application;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ApplicationRowMapper implements RowMapper<Application> {

    @Override
    public Application mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new Application(
                resultSet.getInt("app_id"),
                resultSet.getString("roll_no"),
                resultSet.getInt("company_id"),
                resultSet.getString("interview_status")
        );
    }
}
