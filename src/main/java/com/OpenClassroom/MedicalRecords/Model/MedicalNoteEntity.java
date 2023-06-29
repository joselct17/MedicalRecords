package com.OpenClassroom.MedicalRecords.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.print.attribute.standard.DateTimeAtCreation;
import java.time.LocalDateTime;


@Document
@Getter
@Setter
@NoArgsConstructor
public class MedicalNoteEntity {


    @Field("patientId")
    private Integer patientId;

    private String patientLastName;

    private String note;

    @GeneratedValue
    private LocalDateTime dateTimeAtCreation;

    public MedicalNoteEntity(Integer patient_id, String patientLastName, String note, LocalDateTime dateTimeAtCreation) {

        this.patientId = patientId;
        this.patientLastName = patientLastName;
        this.note = note;
        this.dateTimeAtCreation = dateTimeAtCreation;
    }
}
