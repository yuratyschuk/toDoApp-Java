package com.example.demo.model.dto;

import com.example.demo.model.Project;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
public class TaskDto {

    private int id;

    private String title;

    private String description;

    private String createDate;

    private String finishDate;

    private boolean isActive;

    private int priority;

    private Project project;

    public LocalDateTime getCreateDateAsDate() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        if(createDate != null) {
            return LocalDateTime.parse(createDate, dateTimeFormatter);
        }

        return null;
    }

    public LocalDateTime getFinishDateAsDate() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        if(finishDate != null) {
            return LocalDateTime.parse(finishDate, dateTimeFormatter);
        }

        return null;
    }
}
