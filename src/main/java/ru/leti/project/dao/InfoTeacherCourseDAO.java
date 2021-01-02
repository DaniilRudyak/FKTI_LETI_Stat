package ru.leti.project.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.leti.project.models.Group;
import ru.leti.project.models.Student;
import ru.leti.project.models.Teacher;
import ru.leti.project.rowmapper.InfoTeacherCourseRowMapper;
import ru.leti.project.rowmapper.StudentRowMapper;

import java.sql.*;
import java.util.List;

@Component
public class InfoTeacherCourseDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public InfoTeacherCourseDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Teacher> index(int id) {
        Group group = jdbcTemplate.query("SELECT * FROM enum_of_groups WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Group.class))
                .stream().findAny().orElse(null);

        List<Teacher> teachers = jdbcTemplate.query("SELECT * FROM list_all_teacher WHERE teaching_group_number = ? AND year_of_study >= ? AND year_of_study <= ?"
                , new Object[]{group.getNumberGroup(), group.getBegStud(), group.getEndStud()}, new InfoTeacherCourseRowMapper());
        return teachers;
    }

    public int save(Teacher teacher, int id) {
        Group group = jdbcTemplate.query("SELECT * FROM enum_of_groups WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Group.class))
                .stream().findAny().orElse(null);
        Teacher teacherCheck = jdbcTemplate.query("SELECT * FROM list_all_teacher WHERE teaching_group_number = ? AND course = ? AND year_of_study = ?"
                , new Object[]{group.getNumberGroup(), teacher.getNameCourse(), teacher.getYearsStudy()}, new InfoTeacherCourseRowMapper())
                .stream().findAny().orElse(null);

        if (teacherCheck != null
                || (!(teacher.getYearsStudy().after(group.getBegStud()) && teacher.getYearsStudy().before(group.getEndStud())))
                && (!(teacher.getYearsStudy().equals(group.getBegStud()) && teacher.getYearsStudy().before(group.getEndStud())))
                && (!(teacher.getYearsStudy().after(group.getBegStud()) && teacher.getYearsStudy().equals(group.getEndStud()))))//данный курс уже присутствует или год выставления за диапазоном обучения
            return 1;

        teacher.setNumberInGroupCourse(group.getNumberGroup());

        List<Student> studentList = jdbcTemplate.query("SELECT * FROM list_all_student WHERE number_group = ? AND beg_stud <= ? AND end_stud >= ?"
                , new Object[]{teacher.getNumberInGroupCourse(), teacher.getYearsStudy(), teacher.getYearsStudy()}, new StudentRowMapper());

        jdbcTemplate.update("INSERT INTO list_all_teacher(fullname, teaching_group_number, course , year_of_study) VALUES(?, ?, ?, ?)"
                , teacher.getFullName(), teacher.getNumberInGroupCourse(), teacher.getNameCourse(), teacher.getYearsStudy());

        for (Student student : studentList) {
            jdbcTemplate.update("INSERT INTO grade_sheet(fullname, number_group, course, year_of_certification, number_student_card) VALUES(?, ?, ?, ?, ?)"
                    , student.getFullName(), student.getNumberGroup(), teacher.getNameCourse(), teacher.getYearsStudy(), student.getNumberStudentCard());
        }

        return 0;
    }

    public void delete(int id, String course, Date year) {
        Group group = jdbcTemplate.query("SELECT * FROM enum_of_groups WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Group.class))
                .stream().findAny().orElse(null);

        jdbcTemplate.update("DELETE FROM grade_sheet WHERE number_group = ? AND course = ? AND year_of_certification = ?"
                , group.getNumberGroup(), course, year);

        jdbcTemplate.update("DELETE FROM list_all_teacher WHERE teaching_group_number = ? AND course = ? AND year_of_study = ?"
                , group.getNumberGroup(), course, year);
    }
}