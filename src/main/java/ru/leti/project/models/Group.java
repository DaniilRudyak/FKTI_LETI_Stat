package ru.leti.project.models;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.sql.Date;

@Data
public class Group {
    private int id;
    @Min(value = 1000, message = "Group number should be only 4 characters")
    @Max(value = 9999, message = "Group number card should be only 4 characters")
    private int numberGroup;

    private Date begStud;

    private Date endStud;

    public Group() {
    }

    public Group(int numberGroup, Date begStud, Date endStud) {
        this.numberGroup = numberGroup;
        this.begStud = begStud;
        this.endStud = endStud;
    }

    public Group(int id, int numberGroup, Date begStud, Date endStud) {
        this.id = id;
        this.numberGroup = numberGroup;
        this.begStud = begStud;
        this.endStud = endStud;
    }

}
