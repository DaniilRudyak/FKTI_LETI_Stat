package ru.leti.project.auth;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import ru.leti.project.rowmapper.ApplicationUserRowMapper;

import java.util.List;
import java.util.Optional;



@Repository("userRep")
public class ApplicationUserDaoImpl implements ApplicationUserDao{

    private final JdbcTemplate jdbcTemplate;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    @Autowired
    public ApplicationUserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<ApplicationUser> selectApplicationUserByUsername(String username) {
        return getApplicationUsers()
                .stream()
                .filter(applicationUser -> username.equals(applicationUser.getUsername()))
                .findFirst();
    }

    public int save (ApplicationUser applicationUser){
        boolean userNotFound = jdbcTemplate.query("SELECT * FROM users WHERE username=?"
                ,new Object[]{applicationUser.getUsername()},
                new ApplicationUserRowMapper()).isEmpty();
        if (userNotFound==true)
        {
            jdbcTemplate.update("INSERT INTO users (username,password)VALUES(?,?)",
                    applicationUser.getUsername(),passwordEncoder.encode(applicationUser.getPassword()));
            return 1;
        }
        else
            return 0;
    }

    private List<ApplicationUser> getApplicationUsers(){
     return jdbcTemplate.query("SELECT * FROM users" ,new ApplicationUserRowMapper());
    }
}
