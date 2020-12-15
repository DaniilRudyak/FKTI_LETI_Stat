package ru.leti.project.models;

import lombok.Data;
import org.hibernate.validator.internal.util.StringHelper;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class Teacher {
    private int id;

    @NotEmpty(message = "Full name should not be empty")
    @Size(min = 10, max = 40, message = "Full name should be between 10 and 40 characters")
    private String fullName;

    @Size(min = 1, message = "Number of group can't be less than 4 characters")
    private int nunberInGroupCourse;

    @NotEmpty(message = "Name course should not be empty")
    @Size(min = 3, max = 20, message = "Name course should be between 3 and 20 characters")
    private String nameCourse;

    @Size(min = 1886, max = 2077, message = "Year can't be less than 1886 because you can't learn in the university while it's not even existing or more than 2077 (cause Cyberpunk 2077) ")
    private int yearsStudy;
}
