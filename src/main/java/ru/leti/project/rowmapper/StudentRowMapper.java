package ru.leti.project.rowmapper;

import org.springframework.jdbc.core.RowMapper;
import ru.leti.project.models.Student;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentRowMapper implements RowMapper<Student> {
    @Override
    public Student mapRow(ResultSet resultSet, int i) throws SQLException {
        Student student = new Student();

        student.setId(resultSet.getInt("id"));
        student.setFullName(resultSet.getString("fullname"));
        student.setNumberGroup(resultSet.getInt("number_group"));
        student.setNumberStudentCard(resultSet.getInt("number_student_card"));
        student.setBegStud(resultSet.getDate("beg_stud"));
        student.setEndStud(resultSet.getDate("end_stud"));

        return student;
    }
}
