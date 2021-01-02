package ru.leti.project.rowmapper;

import org.springframework.jdbc.core.RowMapper;
import ru.leti.project.models.Teacher;

import java.sql.ResultSet;
import java.sql.SQLException;


public class InfoTeacherCourseRowMapper implements RowMapper<Teacher> {


    @Override
    public Teacher mapRow(ResultSet resultSet, int i) throws SQLException {
        Teacher teacher = new Teacher();

        teacher.setId(resultSet.getInt("id"));
        teacher.setFullName(resultSet.getString("fullname"));
        teacher.setNumberInGroupCourse(resultSet.getInt("teaching_group_number"));
        teacher.setNameCourse(resultSet.getString("course"));
        teacher.setYearsStudy(resultSet.getDate("year_of_study"));

        return teacher;
    }
}
