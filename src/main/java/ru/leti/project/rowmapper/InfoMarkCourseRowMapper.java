package ru.leti.project.rowmapper;

import org.springframework.jdbc.core.RowMapper;
import ru.leti.project.models.CourseInfo;

import java.sql.ResultSet;
import java.sql.SQLException;


public class InfoMarkCourseRowMapper implements RowMapper<CourseInfo> {

    @Override
    public CourseInfo mapRow(ResultSet resultSet, int i) throws SQLException {
        CourseInfo courseInfo = new CourseInfo();

        courseInfo.setId(resultSet.getInt("id"));
        courseInfo.setNameStudent(resultSet.getString("fullname"));
        courseInfo.setNumberGroup(resultSet.getInt("number_group"));
        courseInfo.setNameCourse(resultSet.getString("course"));
        courseInfo.setYearOfCertification(resultSet.getDate("year_of_certification"));
        courseInfo.setNumberStudentCard(resultSet.getInt("number_student_card"));
        courseInfo.setMark(resultSet.getInt("mark"));

        return courseInfo;
    }
}
