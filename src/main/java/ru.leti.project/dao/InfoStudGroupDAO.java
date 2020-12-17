package ru.leti.project.dao;

import org.springframework.stereotype.Component;
import ru.leti.project.config.ConfigConnection;
import ru.leti.project.models.Group;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;

@Component
public class InfoStudGroupDAO {


    Connection connection = ConfigConnection.connection;

    public List<Group> index() {
        List<Group> groups = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            String SQL = "SELECT * FROM enum_of_groups";
            ResultSet resultSet = statement.executeQuery(SQL);

            while (resultSet.next()) {
                Group group = new Group();

                group.setId(resultSet.getInt("id"));
                group.setNumberGroup(resultSet.getInt("number_group"));
                group.setBegStud(resultSet.getDate("beg_stud"));
                group.setEndStud(resultSet.getDate("end_stud"));

                groups.add(group);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return groups;
    }


    public int save(Group group) {
        try {
            if (group.getBegStud().after(group.getEndStud())) {
                return 1;
            }


            PreparedStatement preparedStatement =
                    connection.prepareStatement("INSERT INTO enum_of_groups(number_group , beg_stud, end_stud) VALUES(?, ?, ?)");

            preparedStatement.setInt(1, group.getNumberGroup());
            preparedStatement.setDate(2, group.getBegStud());
            preparedStatement.setDate(3, group.getEndStud());


            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }


    public void delete(int id) {


        try {
            PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT * FROM enum_of_groups WHERE id=?");
            PreparedStatement preparedStatement2 = connection.prepareStatement("DELETE FROM enum_of_groups WHERE id=?");
            PreparedStatement preparedStatement3 = connection.prepareStatement("DELETE FROM grade_sheet WHERE number_group = ? AND year_of_certification >= ? AND year_of_certification <= ?");
            PreparedStatement preparedStatement4 = connection.prepareStatement("DELETE FROM list_all_student WHERE number_group = ? AND beg_stud = ? AND end_stud = ?");
            PreparedStatement preparedStatement5 = connection.prepareStatement("DELETE FROM list_all_teacher WHERE teaching_group_number = ? AND year_of_study >= ? AND year_of_study <= ?");

            preparedStatement1.setInt(1, id);
            ResultSet resultSet = preparedStatement1.executeQuery();
            resultSet.next();

            preparedStatement5.setInt(1, resultSet.getInt("number_group"));
            preparedStatement5.setDate(2, resultSet.getDate("beg_stud"));
            preparedStatement5.setDate(3, resultSet.getDate("end_stud"));
            preparedStatement5.executeUpdate();

            preparedStatement4.setInt(1, resultSet.getInt("number_group"));
            preparedStatement4.setDate(2, resultSet.getDate("beg_stud"));
            preparedStatement4.setDate(3, resultSet.getDate("end_stud"));
            preparedStatement4.executeUpdate();

            preparedStatement3.setInt(1, resultSet.getInt("number_group"));
            preparedStatement3.setDate(2, resultSet.getDate("beg_stud"));
            preparedStatement3.setDate(3, resultSet.getDate("end_stud"));
            preparedStatement3.executeUpdate();

            preparedStatement2.setInt(1, id);
            preparedStatement2.executeUpdate();


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}

