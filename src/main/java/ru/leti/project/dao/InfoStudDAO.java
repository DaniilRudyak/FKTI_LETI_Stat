package ru.leti.project.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.leti.project.config.ConfigConnection;
import ru.leti.project.models.Group;
import ru.leti.project.models.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Component
public class InfoStudDAO {
    Connection connection = ConfigConnection.connection;
    private final JdbcTemplate jdbcTemplate;

    public InfoStudDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public List<Student> index(int id) {
        Group group = jdbcTemplate.query("SELECT * FROM enum_of_groups WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Group.class))
                .stream().findAny().orElse(null);

        return jdbcTemplate.query("SELECT * FROM list_all_student WHERE number_group = ? AND beg_stud = ? AND end_stud = ?"
                , new Object[]{group.getNumberGroup(), group.getBegStud(), group.getEndStud()}, new BeanPropertyRowMapper<>(Student.class));
    }

    public void delete(int id, int number) {
        Group group = jdbcTemplate.query("SELECT * FROM enum_of_groups WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Group.class))
                .stream().findAny().orElse(null);

        String fullname = jdbcTemplate.query("SELECT fullname FROM list_all_student WHERE number_student_card = ? AND number_group = ? AND beg_stud = ? AND end_stud = ?"
                , new Object[]{number, group.getNumberGroup(), group.getBegStud(), group.getEndStud()}, new BeanPropertyRowMapper<>(String.class))
                .stream().findAny().orElse(null);

        jdbcTemplate.update("DELETE FROM list_all_student WHERE fullname = ? AND number_group = ? AND beg_stud = ? AND end_stud = ? AND number_student_card = ?",
                fullname, group.getNumberGroup(), group.getBegStud(), group.getEndStud(), number);

        jdbcTemplate.update("DELETE FROM grade_sheet WHERE fullname = ? AND number_group = ? AND year_of_certification >= ? AND year_of_certification <= ? AND number_student_card = ?",
                fullname, group.getNumberGroup(), group.getBegStud(), group.getEndStud(), number);
    }

    public int save(Student student, int id) {

        Group group = jdbcTemplate.query("SELECT * FROM enum_of_groups WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Group.class))
                .stream().findAny().orElse(null);

        Student studentCheck = jdbcTemplate.query("SELECT * FROM list_all_student WHERE number_group = ? AND number_student_card = ? AND beg_stud = ? AND end_stud = ?",
                new Object[]{group.getNumberGroup(), student.getNumberStudentCard(), group.getBegStud(), group.getEndStud()}, new BeanPropertyRowMapper<>(Student.class))
                .stream().findAny().orElse(null);

        if (studentCheck != null)
            return 1;

        jdbcTemplate.update("INSERT INTO list_all_student(fullname, number_group, number_student_card , beg_stud, end_stud) VALUES(?, ?, ?, ?, ?)"
                , student.getFullName(), group.getNumberGroup(), student.getNumberStudentCard(), group.getBegStud(), group.getEndStud());

        List<Map<String, Object>> listHelper = jdbcTemplate.queryForList("SELECT course,year_of_study FROM list_all_teacher WHERE teaching_group_number = ? AND year_of_study >= ? AND year_of_study <= ?"
                , group.getNumberGroup(), group.getBegStud(), group.getEndStud());

        for (Map<String, Object> cur : listHelper) {
            Set<String> keySet = cur.keySet();
            for (String key : keySet)
                jdbcTemplate.update("INSERT INTO grade_sheet(fullname, number_group, course, year_of_certification, number_student_card ) VALUES(?, ?, ?, ?, ?)"
                        , student.getFullName(), group.getNumberGroup(), key, ((Integer) (cur.get(key))).intValue(), student.getNumberStudentCard());
        }
        return 0;
    }
}