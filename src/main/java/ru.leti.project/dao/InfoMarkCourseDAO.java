package ru.leti.project.dao;

import au.com.bytecode.opencsv.CSVWriter;
import org.springframework.stereotype.Component;
import ru.leti.project.config.ConfigConnection;
import ru.leti.project.models.CourseInfo;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class InfoMarkCourseDAO {
    Connection connection = ConfigConnection.connection;

    public List<CourseInfo> index(int id, String course, int year) {
        List<CourseInfo> courseInfos = new ArrayList<>();

        try {

            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM enum_of_groups WHERE id=?");

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();

            PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT * FROM grade_sheet WHERE number_group = ? AND course = ? AND year_of_certification = ? ");

            preparedStatement1.setInt(1, resultSet.getInt("number_group"));
            preparedStatement1.setString(2, course);
            preparedStatement1.setInt(3, year);
            ResultSet resultSet1 = preparedStatement1.executeQuery();

            String SQL = "SELECT fullname FROM list_all_teacher WHERE course = '" + course + "' AND " + "year_of_study = " + year;
            Statement statement = connection.createStatement();
            ResultSet resultSet2 = statement.executeQuery(SQL);
            resultSet2.next();


            while (resultSet1.next()) {
                CourseInfo courseInfo = new CourseInfo();

                courseInfo.setId(resultSet1.getInt("id"));
                courseInfo.setNameStudent(resultSet1.getString("fullname"));
                courseInfo.setNumberGroup(resultSet1.getInt("number_group"));
                courseInfo.setNameCourse(course);
                courseInfo.setNameTeacher(resultSet2.getString("fullname"));
                courseInfo.setYearOfCertification(year);
                courseInfo.setMark(resultSet1.getInt("mark"));
                courseInfo.setNumberStudentCard(resultSet1.getInt("number_student_card"));

                courseInfos.add(courseInfo);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return courseInfos;
    }

    public CourseInfo show(int id, int number, String course, int year) {
        CourseInfo courseInfo = null;

        try {
            String SQL = "SELECT * FROM enum_of_groups WHERE id = " + id;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL);
            resultSet.next();

            String SQL1 = "SELECT * FROM grade_sheet WHERE number_group = " + resultSet.getInt("number_group")
                    + " AND " + "course = '" + course
                    + "' AND year_of_certification = " + year
                    + " AND number_student_card = " + number;

            Statement statement1 = connection.createStatement();
            ResultSet resultSet1 = statement1.executeQuery(SQL1);
            resultSet1.next();


            String SQL2 = "SELECT fullname FROM list_all_teacher WHERE course = '" + course + "' AND " + "year_of_study = " + year + " AND teaching_group_number = " + resultSet1.getInt("number_group");
            Statement statement2 = connection.createStatement();
            ResultSet resultSet2 = statement2.executeQuery(SQL2);
            resultSet2.next();


            courseInfo = new CourseInfo();

            courseInfo.setId(resultSet1.getInt("id"));
            courseInfo.setNumberGroup(resultSet1.getInt("number_group"));
            courseInfo.setNameCourse(course);
            courseInfo.setNameStudent(resultSet1.getString("fullname"));
            courseInfo.setYearOfCertification(year);
            courseInfo.setNameTeacher(resultSet2.getString("fullname"));
            courseInfo.setMark(resultSet1.getInt("mark"));
            courseInfo.setNumberStudentCard(number);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return courseInfo;
    }

    public void update(CourseInfo courseInfo) {
        try {
            String SQL = "UPDATE grade_sheet SET mark = " + courseInfo.getMark()
                    + " WHERE fullname = '" + courseInfo.getNameStudent() + "' AND "
                    + "number_group = " + courseInfo.getNumberGroup() + " AND "
                    + "course = '" + courseInfo.getNameCourse() + "' AND "
                    + "year_of_certification = " + courseInfo.getYearOfCertification() + " AND "
                    + "number_student_card = " + courseInfo.getNumberStudentCard();
            Statement statement = connection.createStatement();
            ;
            statement.executeUpdate(SQL);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void createCSVFile(String path, int id, String course, int year) {
        ResultSet resultSet = null;
        ResultSet resultSet1 = null;

        try {
            String SQL = "SELECT number_group FROM enum_of_groups WHERE id = " + id;
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(SQL);
            resultSet.next();

            String SQL1 = "SELECT * FROM grade_sheet WHERE " +
                    "number_group = " + resultSet.getInt("number_group") + " AND "
                    + "course = '" + course + "' AND "
                    + "year_of_certification = " + year;
            Statement statement1 = connection.createStatement();
            resultSet1 = statement1.executeQuery(SQL1);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        CSVWriter writer = null;
        try {


            String csv = course + "_group_" + resultSet.getInt("number_group") + '_' + year + " .csv";
            writer = new CSVWriter(new FileWriter(path + '/' + csv), ';');
            //Create record
            writer.writeAll(resultSet1, true);
            writer.close();
            //Write the record to file

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
