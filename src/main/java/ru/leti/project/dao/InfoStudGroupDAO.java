package ru.leti.project.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.leti.project.models.Group;

import java.util.List;

@Component
public class InfoStudGroupDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public InfoStudGroupDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Group> index() {
        return jdbcTemplate.query("SELECT * FROM enum_of_groups", new BeanPropertyRowMapper<>(Group.class));
    }

    public int save(Group group) {

        if (group.getBegStud().after(group.getEndStud()))
            return 1;

        jdbcTemplate.update("INSERT INTO enum_of_groups(number_group , beg_stud, end_stud) VALUES(?, ?, ?)"
                , group.getNumberGroup(), group.getBegStud(), group.getEndStud());

        return 0;
    }

    public void delete(int id) {
        Group group = jdbcTemplate.query("SELECT * FROM enum_of_groups WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Group.class))
                .stream().findAny().orElse(null);

        jdbcTemplate.update("DELETE FROM list_all_teacher WHERE teaching_group_number = ? AND year_of_study >= ? AND year_of_study <= ?"
                , group.getNumberGroup(), group.getBegStud(), group.getEndStud());

        jdbcTemplate.update("DELETE FROM list_all_student WHERE number_group = ? AND beg_stud = ? AND end_stud = ?"
                , group.getNumberGroup(), group.getBegStud(), group.getEndStud());

        jdbcTemplate.update("DELETE FROM grade_sheet WHERE number_group = ? AND year_of_certification >= ? AND year_of_certification <= ?"
                , group.getNumberGroup(), group.getBegStud(), group.getEndStud());

        jdbcTemplate.update("DELETE FROM enum_of_groups WHERE id=?"
                , id);
    }

    public Group show(int idGroup) {
        return jdbcTemplate.query("SELECT * FROM enum_of_groups WHERE id = ?", new Object[]{idGroup}, new BeanPropertyRowMapper<>(Group.class))
                .stream().findAny().orElse(null);
    }

    public void update(Group newGroup) {
        Group oldGroup = jdbcTemplate.query("SELECT * FROM enum_of_groups WHERE id = ?",new Object[]{newGroup.getId()}, new BeanPropertyRowMapper<>(Group.class))
                .stream().findAny().orElse(null);

        jdbcTemplate.update("UPDATE enum_of_groups SET number_group = ?, beg_stud = ?, end_stud = ?" +
                        "WHERE number_group = ? AND beg_stud = ? AND end_stud = ?",
                new Object[]{newGroup.getNumberGroup(), newGroup.getBegStud(), newGroup.getEndStud(),
                        oldGroup.getNumberGroup(),oldGroup.getBegStud(),oldGroup.getEndStud()});

        jdbcTemplate.update("UPDATE list_all_student SET number_group = ?, beg_stud = ?, end_stud = ? " +
                "WHERE number_group = ? AND beg_stud = ? AND end_stud = ?",
                new Object[]{newGroup.getNumberGroup(),newGroup.getBegStud(),newGroup.getEndStud(),
                        oldGroup.getNumberGroup(),oldGroup.getBegStud(),oldGroup.getEndStud()});

        jdbcTemplate.update("UPDATE grade_sheet SET number_group = ? " +
                        "WHERE number_group = ? AND year_of_certification >= ? AND year_of_certification <= ?",
                new Object[]{newGroup.getNumberGroup(),
                        oldGroup.getNumberGroup(),oldGroup.getBegStud(),oldGroup.getEndStud()});

        jdbcTemplate.update("UPDATE list_all_teacher SET teaching_group_number = ? " +
                        "WHERE teaching_group_number = ? AND year_of_study >= ? AND year_of_study <= ?",
                new Object[]{newGroup.getNumberGroup(),
                        oldGroup.getNumberGroup(),oldGroup.getBegStud(),oldGroup.getEndStud()});
    }
}
