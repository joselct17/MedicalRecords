package com.OpenClassroom.MedicalRecords.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.print.attribute.standard.DateTimeAtCreation;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class MedicalNoteEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "notes_id")
    private Integer id;

    private Integer patientId;

    private String patientLastName;

    private String note;

    @GeneratedValue
    private LocalDateTime dateTimeAtCreation;

    public MedicalNoteEntity(Integer id, Integer patient_id, String patientLastName, String note, LocalDateTime dateTimeAtCreation) {
        this.id = id;
        this.patientId = patientId;
        this.patientLastName = patientLastName;
        this.note = note;
        this.dateTimeAtCreation = dateTimeAtCreation;
    }
}
