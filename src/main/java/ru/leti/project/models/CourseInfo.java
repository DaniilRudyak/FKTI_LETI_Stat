package ru.leti.project.models;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.sql.Date;

@Data
public class CourseInfo {

    private int id;

    @Size(min = 10, max = 40, message = "Student's name should be between 10 and 40 characters")
    private String nameStudent;

    @Min(value = 1000,message = "Group number should be only 4 characters")
    @Max(value = 9999,message = "Group number card should be only 4 characters")
    private int numberGroup;


    @NotEmpty(message = "Course name course should not be empty")
    @Size(min = 2, max = 20, message = "Course name should be between 2 and 20 characters")
    private String nameCourse;

    private Date yearOfCertification;

    @Min(value = 100000,message = "Number of student card should be only 6 characters")
    @Max(value = 999999,message = "Number of student card should be only 6 characters")
    private int numberStudentCard;

    @Range(min = 2,max = 5,message = "Mark should be between 2 and 5")
    private int mark;

    @Size(min = 10, max = 40, message = "Teacher's name should be between 10 and 40 characters")
    private String nameTeacher;
    public CourseInfo() {
    }


    public CourseInfo(int id, String nameStudent, int numberGroup, String nameCourse, Date yearOfCertification, int numberStudentCard, int mark, String nameTeacher) {
        this.id = id;
        this.nameStudent = nameStudent;
        this.numberGroup = numberGroup;
        this.nameCourse = nameCourse;
        this.yearOfCertification = yearOfCertification;
        this.numberStudentCard = numberStudentCard;
        this.mark = mark;
        this.nameTeacher = nameTeacher;
    }
}
