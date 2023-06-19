package com.OpenClassroom.MedicalRecords.Service.Interfaces;

import com.OpenClassroom.MedicalRecords.Model.MedicalNoteEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IMedicalNoteService {

    List<MedicalNoteEntity> getPatientAllNotesByPatientId(Integer patient_id);

    List<MedicalNoteEntity> getPatientAllNotesByPatientLastName(String patient_lastName);

    MedicalNoteEntity saveNote(MedicalNoteEntity medicalNoteEntity);

    void deleteNoteById(Integer id);

    Iterable<MedicalNoteEntity> getAllNotes();

    MedicalNoteEntity getNoteById(Integer id);

    MedicalNoteEntity updateNoteById(Integer id, MedicalNoteEntity medicalNoteEntity);

    MedicalNoteEntity addNoteByPatient_Id(Integer patient_id, String note);


}
