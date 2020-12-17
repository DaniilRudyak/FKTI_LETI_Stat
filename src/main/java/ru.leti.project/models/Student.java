package ru.leti.project.models;


import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.sql.Date;

@Data
public class Student {
    private int id;

    @NotEmpty(message = "Full name should not be empty")
    @Size(min = 10, max = 40, message = "Full name should be between 10 and 40 characters")
    private String fullName;

   // @Size(min = 1, message = "Number of group should be only 4 characters")
    private int numberGroup;

    @Min(value = 100000,message = "Number of student card should be only 6 characters")
    @Max(value = 999999,message = "Number of student card should be only 6 characters")
    private int numberStudentCard;

    private Date begStud;

    private Date endStud;

    public Student(int id, String fullName, int numberGroup, int numberStudentCard, Date begStud, Date endStud) {
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
