package ru.leti.project.auth;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import ru.leti.project.rowmapper.ApplicationUserRowMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static ru.leti.project.security.ApplicationUserRole.*;


@Repository("fake")
public class FakeApplicationUserDaoService implements ApplicationUserDao{

    private final JdbcTemplate jdbcTemplate;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    @Autowired
    public FakeApplicationUserDaoService(JdbcTemplate jdbcTemplate) {
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
//        List<ApplicationUser> list = null;
//        list = jdbcTemplate.query("SELECT * FROM users" ,new ApplicationUserRowMapper());
//        return list;
     return jdbcTemplate.query("SELECT * FROM users" ,new ApplicationUserRowMapper());

//        List<ApplicationUser> applicationUsers = Lists.newArrayList(
//                new ApplicationUser(
//                        STUDENT.getGrantedAuthorities(),
//                        passwordEncoder.encode("password"),
//                        "annasmith",
//                        "ADMIN",
//                        true,
//                        true,
//                        true,
//                        true
//                ),
//                new ApplicationUser(
//                        ADMIN.getGrantedAuthorities(),
//                        passwordEncoder.encode("password"),
//                        "linda",
//                        "STUDENT",
//                        true,
//                        true,
//                        true,
//                        true
//                ),
//                new ApplicationUser(
//                        ADMINTRAINEE.getGrantedAuthorities(),
//                        passwordEncoder.encode("password"),
//                        "tom",
//                        "ADMINTRAINEE",
//                        true,
//                        true,
//                        true,
//                        true
//                )
//
//        );
//        return applicationUsers;

    }
}
