package cn.benbenedu.sundial.account.model;

import lombok.Data;

import java.util.List;

@Data
public class StudyingStatus {

    private String sno;
    private String school;
    private String grade;
    private String executiveClass;
    private List<String> chosenSubjects;
    private String subjectClass;
    private List<String> tutors;
}
