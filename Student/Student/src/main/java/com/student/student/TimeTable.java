package com.student.student;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = ("TB_TIMETABLE"))
@Data
public class TimeTable {
    @Id
    private Long ID;
    @Column(name = "SUBJECT_TITLE")
    private String subjectTitle;
}
