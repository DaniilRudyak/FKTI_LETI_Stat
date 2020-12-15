package ru.leti.project.models;


import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class Student {
    private int id;

    @NotEmpty(message = "Full name should not be empty")
    @Size(min = 10, max = 40, message = "Full name should be between 10 and 40 characters")
    private String fullName;

    @Size(min = 1, message = "Number of group should be only 4 characters")
    private int numberGroup;

    @Size(min = 1, message = "Number of group should be only 6 characters")
    private int numberStudentCard;

    private int begStud;

    private int endStud;

    public Student(int id, String fullName, int numberGroup, int numberStudentCard, int begStud, int endStud) {
        this.id = id;
        this.fullName = fullName;
        this.numberGroup = numberGroup;
        this.numberStudentCard = numberStudentCard;
        this.begStud = begStud;
        this.endStud = endStud;
    }

    public Student() {
    }

    public Student(int id, String fullName, int numberGroup, int numberStudentCard) {
        this.id = id;
        this.fullName = fullName;
        this.numberGroup = numberGroup;
        this.numberStudentCard = numberStudentCard;
    }
}
