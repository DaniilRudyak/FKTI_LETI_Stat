package ru.leti.project.models;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class CourseInfo {

    private int id;
    private String nameStudent;
    private int numberGroup;
    private String nameTeacher;

    @NotEmpty(message = "Name course should not be empty")
    @Size(min = 3, max = 20, message = "Name course should be between 3 and 20 characters")
    private String nameCourse;

    private int yearOfCertification;
    private int numberStudentCard;

    @Size(min = 2, max = 5, message = "The mark can't be less than 2 or more than 5")
    private int mark;

    public CourseInfo() {
    }


    public CourseInfo(int id, String nameStudent, int numberGroup, String nameTeacher, String nameCourse,int yearOfCertification, int numberStudentCard, int mark) {
        this.id = id;
        this.nameStudent = nameStudent;
        this.numberGroup = numberGroup;
        this.nameTeacher = nameTeacher;
        this.nameCourse = nameCourse;
        this.yearOfCertification = yearOfCertification;
        this.numberStudentCard = numberStudentCard;
        this.mark = mark;
    }
}
