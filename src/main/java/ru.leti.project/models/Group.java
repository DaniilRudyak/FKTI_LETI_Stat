package ru.leti.project.models;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class Group {
    private int id;
    @Size(min = 1, message = "Number of group should be only 4 characters")
    private int numberGroup;

    private int begStud;

    private int endStud;

    public Group() {
    }

    public Group( int numberGroup, int begStud, int endStud) {
        this.numberGroup = numberGroup;
        this.begStud = begStud;
        this.endStud = endStud;
    }

    public Group(int id, int numberGroup, int begStud, int endStud) {
        this.id = id;
        this.numberGroup = numberGroup;
        this.begStud = begStud;
        this.endStud = endStud;
    }

}
