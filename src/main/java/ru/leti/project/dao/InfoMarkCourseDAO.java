package ru.leti.project.dao;

import au.com.bytecode.opencsv.CSVWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.leti.project.config.SpringConfig;
import ru.leti.project.models.CourseInfo;
import ru.leti.project.models.Group;
import ru.leti.project.rowmapper.InfoMarkCourseRowMapper;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.List;

@Component
public class InfoMarkCourseDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public InfoMarkCourseDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<CourseInfo> index(int id, String course, Date year) {

        Group group = jdbcTemplate.query("SELECT * FROM enum_of_groups WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Group.class))
                .stream().findAny().orElse(null);

        List<CourseInfo> courseInfoList = jdbcTemplate.query("SELECT * FROM grade_sheet WHERE number_group = ? AND course = ? AND year_of_certification = ? "
                , new Object[]{group.getNumberGroup(), course, year}, new InfoMarkCourseRowMapper());

        String fullname = jdbcTemplate.queryForObject("SELECT fullname FROM list_all_teacher WHERE course = ? AND year_of_study = ?"
                , new Object[]{course, year}, String.class); //это должно стать с джойнами

        for (CourseInfo courseInfo : courseInfoList) {
            courseInfo.setNameTeacher(fullname);
        }

        return courseInfoList;
    }

    public CourseInfo show(int id, int number, String course, Date year) {
        Group group = jdbcTemplate.query("SELECT * FROM enum_of_groups WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Group.class))
                .stream().findAny().orElse(null);

        CourseInfo courseInfo = jdbcTemplate.query("SELECT * FROM grade_sheet WHERE number_group = ? AND course = ? AND year_of_certification = ? AND number_student_card = ?"
                , new Object[]{group.getNumberGroup(), course, year, number}, new InfoMarkCourseRowMapper())
                .stream().findAny().orElse(null);

        String fullNameTeacher = jdbcTemplate.queryForObject("SELECT fullname FROM list_all_teacher WHERE course = ? AND year_of_study = ? AND teaching_group_number = ?"
                , new Object[]{course, year, group.getNumberGroup()}, String.class);

        courseInfo.setNameTeacher(fullNameTeacher);

        return courseInfo;
    }

    public void update(CourseInfo courseInfo) {
        jdbcTemplate.update("UPDATE grade_sheet SET mark = ? WHERE fullname = ? AND number_group = ? AND course = ? AND year_of_certification = ? AND number_student_card = ?",
                courseInfo.getMark(), courseInfo.getNameStudent(), courseInfo.getNumberGroup(), courseInfo.getNameCourse(), courseInfo.getYearOfCertification(), courseInfo.getNumberStudentCard());
    }

    public void createCSVFile(String path, int id, String course, Date year) {//делал один не темплейт по причине того чтобы легче было конвертить в csv
        Group group = jdbcTemplate.query("SELECT * FROM enum_of_groups WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Group.class))
                .stream().findAny().orElse(null);

        CSVWriter writer = null;
        try {
            String SQL1 = "SELECT * FROM grade_sheet WHERE " +
                    "number_group = " + group.getNumberGroup() + " AND "
                    + "course = '" + course + "' AND "
                    + "year_of_certification = " + "'" + year + "'";
            Statement statement1 = (new SpringConfig(null)).dataSource().getConnection().createStatement();
            ResultSet resultSet = statement1.executeQuery(SQL1);
            String csv = course + "_group_" + group.getNumberGroup() + '_' + "'" + year + "'" + " .csv";
            writer = new CSVWriter(new FileWriter(path + '/' + csv), ';');
            //Create record
            writer.writeAll(resultSet, true);
            writer.close();
            //Write the record to file

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
