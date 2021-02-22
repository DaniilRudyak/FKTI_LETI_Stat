package ru.leti.project.rowmapper;

import org.springframework.jdbc.core.RowMapper;
import ru.leti.project.auth.ApplicationUser;
import ru.leti.project.security.ApplicationUserRole;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ApplicationUserRowMapper implements RowMapper<ApplicationUser> {
    @Override
    public ApplicationUser mapRow(ResultSet resultSet, int i) throws SQLException {
       // String StringRole = resultSet.getString("role");

        ApplicationUser applicationUser = new ApplicationUser();

        applicationUser.setGrantedAuthorities( ApplicationUserRole.valueOf(resultSet.getString("role")).getGrantedAuthorities() );
        applicationUser.setUsername(resultSet.getString("username"));
        applicationUser.setPassword(resultSet.getString("password"));
        applicationUser.setRole(resultSet.getString("role"));
        applicationUser.setAccountNonExpired(resultSet.getBoolean("enabled"));
        applicationUser.setAccountNonLocked(resultSet.getBoolean("enabled"));
        applicationUser.setCredentialsNonExpired(resultSet.getBoolean("enabled"));
        applicationUser.setEnabled(resultSet.getBoolean("enabled"));


        return applicationUser;
    }
}
