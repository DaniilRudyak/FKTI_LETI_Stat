package ru.leti.project.dao;

import org.springframework.stereotype.Component;
import ru.leti.project.config.ConfigConnection;
import ru.leti.project.models.Group;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
                group.setBegStud(resultSet.getInt("beg_stud"));
                group.setEndStud(resultSet.getInt("end_stud"));

                groups.add(group);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return groups;
    }



    public void save(Group group) {
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("INSERT INTO enum_of_groups(number_group , beg_stud, end_stud) VALUES(?, ?, ?)");

            preparedStatement.setInt(1, group.getNumberGroup());
            preparedStatement.setInt(2, group.getBegStud());
            preparedStatement.setInt(3, group.getEndStud());


            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }



    public void delete(int id) {// ДОБАВИТЬ ПРОВЕРКУ НА ТО ЧТО А ЕСТЬ ЛИ ДАННЫЕ В ТАБЛИЦЕ ВООБЩЕ ПРИ УДАЛЕНИИ!!!!!!!!!!!


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
            preparedStatement5.setInt(2, resultSet.getInt("beg_stud"));
            preparedStatement5.setInt(3, resultSet.getInt("end_stud"));
            preparedStatement5.executeUpdate();

            preparedStatement4.setInt(1, resultSet.getInt("number_group"));
            preparedStatement4.setInt(2, resultSet.getInt("beg_stud"));
            preparedStatement4.setInt(3, resultSet.getInt("end_stud"));
            preparedStatement4.executeUpdate();

            preparedStatement3.setInt(1, resultSet.getInt("number_group"));
            preparedStatement3.setInt(2, resultSet.getInt("beg_stud"));
            preparedStatement3.setInt(3, resultSet.getInt("end_stud"));
            preparedStatement3.executeUpdate();

            preparedStatement2.setInt(1, id);
            preparedStatement2.executeUpdate();


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}

