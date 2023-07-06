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

    @Transient
    public static final String SEQUENCE_NAME = "MedicalNoteEntity_sequence";

    @Id
    private Long id;

    @Field("patientId")
    private Integer patientId;

    private String patientLastName;

    private String note;

    @GeneratedValue
    private LocalDateTime dateTimeAtCreation;

    public MedicalNoteEntity(Long id, Integer patientId, String patientLastName, String note, LocalDateTime dateTimeAtCreation) {
        this.id = id;
        this.patientId = patientId;
        this.patientLastName = patientLastName;
        this.note = note;
        this.dateTimeAtCreation = dateTimeAtCreation;
    }

}
