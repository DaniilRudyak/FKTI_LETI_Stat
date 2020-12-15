package ru.leti.project.dao;

import org.springframework.stereotype.Component;
import ru.leti.project.config.ConfigConnection;
import ru.leti.project.models.Teacher;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class InfoTeacherCourseDAO {
    Connection connection = ConfigConnection.connection;

    public List<Teacher> index(int id) {
        List<Teacher> teachers = new ArrayList<>();

        try {

            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM enum_of_groups WHERE id=?");

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();

            PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT * FROM list_all_teacher WHERE teaching_group_number = ? AND year_of_study >= ? AND year_of_study <= ?");

            preparedStatement1.setInt(1, resultSet.getInt("number_group"));
            preparedStatement1.setInt(2, resultSet.getInt("beg_stud"));
            preparedStatement1.setInt(3, resultSet.getInt("end_stud"));
            ;
            ResultSet resultSet1 = preparedStatement1.executeQuery();

            while (resultSet1.next()) {
                Teacher teacher = new Teacher();

                teacher.setId(resultSet1.getInt("id"));
                teacher.setFullName(resultSet1.getString("fullname"));
                teacher.setNumberInGroupCourse(resultSet1.getInt("teaching_group_number"));
                teacher.setNameCourse(resultSet1.getString("course"));
                teacher.setYearsStudy(resultSet1.getInt("year_of_study"));


                teachers.add(teacher);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return teachers;
    }


    public int save(Teacher teacher, int id) {
        try {
            Statement statement = connection.createStatement();
            String SQL = "SELECT * FROM enum_of_groups WHERE id = " + id;
            ResultSet resultSet = statement.executeQuery(SQL);
            resultSet.next();
            PreparedStatement checkPreparedStatement =
                    connection.prepareStatement("SELECT * FROM list_all_teacher WHERE fullname = ? AND teaching_group_number = ? AND course = ? AND year_of_study = ?");
            checkPreparedStatement.setString(1, teacher.getFullName());
            checkPreparedStatement.setInt(2, resultSet.getInt("number_group"));
            checkPreparedStatement.setString(3, teacher.getNameCourse());
            checkPreparedStatement.setInt(4, teacher.getYearsStudy());
            ResultSet checkResultSet = checkPreparedStatement.executeQuery();

            if(checkResultSet.next()
                    ||(!(teacher.getYearsStudy()>=resultSet.getInt("beg_stud")&&teacher.getYearsStudy()<=resultSet.getInt("end_stud"))))//данный курс уже присутствует
                return 1;



            teacher.setNumberInGroupCourse(resultSet.getInt("number_group"));
            PreparedStatement preparedStatement =
                    connection.prepareStatement("INSERT INTO list_all_teacher(fullname, teaching_group_number, course , year_of_study) VALUES(?, ?, ?, ?)");

            String SQL1 = "SELECT * FROM list_all_student WHERE number_group = " + teacher.getNumberInGroupCourse()
                    + " AND beg_stud <= " + teacher.getYearsStudy()
                    + " AND end_stud >= " + teacher.getYearsStudy();
            Statement statement1 = connection.createStatement();
            ResultSet resultSet1 = statement1.executeQuery(SQL1);

            while (resultSet1.next()) {
                PreparedStatement preparedStatement1 =
                        connection.prepareStatement("INSERT INTO grade_sheet(fullname, number_group, course, year_of_certification, number_student_card) VALUES(?, ?, ?, ?, ?)");

                preparedStatement1.setString(1, resultSet1.getString("fullname"));
                preparedStatement1.setInt(2, resultSet1.getInt("number_group"));
                preparedStatement1.setString(3, teacher.getNameCourse());
                preparedStatement1.setInt(4, teacher.getYearsStudy());
                preparedStatement1.setInt(5, resultSet1.getInt("number_student_card"));
                preparedStatement1.executeUpdate();

            }


            preparedStatement.setString(1, teacher.getFullName());
            preparedStatement.setInt(2, teacher.getNumberInGroupCourse());
            preparedStatement.setString(3, teacher.getNameCourse());
            preparedStatement.setInt(4, teacher.getYearsStudy());

            preparedStatement.executeUpdate();


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }


    public void delete(int id, String course, int year) {


        try {
            PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT * FROM enum_of_groups WHERE id=?");

            PreparedStatement preparedStatement2 = connection.prepareStatement("DELETE FROM grade_sheet WHERE number_group = ? AND course = ? AND year_of_certification = ?");

            PreparedStatement preparedStatement3 = connection.prepareStatement("DELETE FROM list_all_teacher WHERE teaching_group_number = ? AND course = ? AND year_of_study = ?");

            preparedStatement1.setInt(1, id);
            ResultSet resultSet = preparedStatement1.executeQuery();
            resultSet.next();

            preparedStatement2.setInt(1, resultSet.getInt("number_group"));
            preparedStatement2.setString(2, course);
            preparedStatement2.setInt(3, year);

            preparedStatement2.executeUpdate();


            preparedStatement3.setInt(1, resultSet.getInt("number_group"));

            preparedStatement3.setString(2, course);
            preparedStatement3.setInt(3, year);
            preparedStatement3.executeUpdate();


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}
