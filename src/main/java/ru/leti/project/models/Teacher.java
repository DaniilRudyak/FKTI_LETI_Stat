package ru.leti.project.models;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.sql.Date;

@Data
public class Teacher {
    private int id;

    @NotEmpty(message = "Full name should not be empty")
    @Size(min = 10, max = 40, message = "Full name should be between 10 and 40 characters")
    private String fullName;

    private int numberInGroupCourse;

    @NotEmpty(message = "Course name course should not be empty")
    @Size(min = 2, max = 20, message = "Course name should be between 2 and 20 characters")
    private String nameCourse;

    private Date yearsStudy;
}
