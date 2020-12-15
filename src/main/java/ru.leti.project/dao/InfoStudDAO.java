package ru.leti.project.dao;

import org.springframework.stereotype.Component;
import ru.leti.project.config.ConfigConnection;
import ru.leti.project.models.Student;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class InfoStudDAO {
    Connection connection = ConfigConnection.connection;

    public List<Student> index(int id) {
        List<Student> students = new ArrayList<>();

        try {

            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM enum_of_groups WHERE id=?");

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();

            PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT * FROM list_all_student WHERE number_group = ? AND beg_stud = ? AND end_stud = ?");

            preparedStatement1.setInt(1, resultSet.getInt("number_group"));
            preparedStatement1.setInt(2, resultSet.getInt("beg_stud"));
            preparedStatement1.setInt(3, resultSet.getInt("end_stud"));
            ;
            ResultSet resultSet1 = preparedStatement1.executeQuery();

            while (resultSet1.next()) {
                Student student = new Student();

                student.setId(resultSet1.getInt("id"));
                student.setFullName(resultSet1.getString("fullname"));
                student.setNumberGroup(resultSet1.getInt("number_group"));
                student.setNumberStudentCard(resultSet1.getInt("number_student_card"));
                student.setBegStud(resultSet1.getInt("beg_stud"));
                student.setEndStud(resultSet1.getInt("end_stud"));

                students.add(student);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return students;
    }

    public void delete(int id, int number) {


        try {
            String SQL = "SELECT * FROM enum_of_groups WHERE id = " + id;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL);
            resultSet.next();

            String SQL1 = "SELECT fullname FROM list_all_student WHERE number_student_card = " + number
                    + " AND " + "number_group = " + resultSet.getInt("number_group")
                    + " AND " + "beg_stud = " + resultSet.getInt("beg_stud")
                    + " AND " + "end_stud = " + resultSet.getInt("end_stud");

            Statement statement1 = connection.createStatement();
            ResultSet resultSet1 = statement1.executeQuery(SQL1);
            resultSet1.next();

            PreparedStatement preparedStatement =
                    connection.prepareStatement("DELETE FROM list_all_student WHERE fullname = ? AND number_group = ? AND beg_stud = ? AND end_stud = ? AND number_student_card = ?");
            PreparedStatement preparedStatement1 =
                    connection.prepareStatement("DELETE FROM grade_sheet WHERE fullname = ? AND number_group = ? AND year_of_certification >= ? AND year_of_certification <= ? AND number_student_card = ?");


            preparedStatement.setString(1, resultSet1.getString("fullname"));
            preparedStatement.setInt(2, resultSet.getInt("number_group"));
            preparedStatement.setInt(3, resultSet.getInt("beg_stud"));
            preparedStatement.setInt(4, resultSet.getInt("end_stud"));
            preparedStatement.setInt(5, number);
            preparedStatement.executeUpdate();

            preparedStatement1.setString(1, resultSet1.getString("fullname"));
            preparedStatement1.setInt(2, resultSet.getInt("number_group"));
            preparedStatement1.setInt(3, resultSet.getInt("beg_stud"));
            preparedStatement1.setInt(4, resultSet.getInt("end_stud"));
            preparedStatement1.setInt(5, number);
            preparedStatement1.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public void save(Student student, int id) {
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("INSERT INTO list_all_student(fullname, number_group, number_student_card , beg_stud, end_stud) VALUES(?, ?, ?, ?, ?)");

            String SQL = "SELECT * FROM enum_of_groups WHERE id = " + id;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL);
            resultSet.next();

            preparedStatement.setString(1, student.getFullName());
            preparedStatement.setInt(2, resultSet.getInt("number_group"));
            preparedStatement.setInt(3, student.getNumberStudentCard());
            preparedStatement.setInt(4, resultSet.getInt("beg_stud"));
            preparedStatement.setInt(5, resultSet.getInt("end_stud"));
            preparedStatement.executeUpdate();

            PreparedStatement preparedStatement1 =
                    connection.prepareStatement("SELECT course,year_of_study FROM list_all_teacher WHERE teaching_group_number = ? AND year_of_study >= ? AND year_of_study <= ?");


            preparedStatement1.setInt(1, resultSet.getInt("number_group"));
            preparedStatement1.setInt(2, resultSet.getInt("beg_stud"));
            preparedStatement1.setInt(3, resultSet.getInt("end_stud"));
            ResultSet resultSet1 = preparedStatement1.executeQuery();
            while (resultSet1.next()) {

                PreparedStatement preparedStatement2 =
                        connection.prepareStatement("INSERT INTO grade_sheet(fullname, number_group, course, year_of_certification, number_student_card ) VALUES(?, ?, ?, ?, ?)");
                preparedStatement2.setString(1, student.getFullName());
                preparedStatement2.setInt(2, resultSet.getInt("number_group"));
                preparedStatement2.setString(3, resultSet1.getString("course"));
                preparedStatement2.setInt(4, resultSet1.getInt("year_of_study"));
                preparedStatement2.setInt(5, student.getNumberStudentCard());

                preparedStatement2.executeUpdate();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}