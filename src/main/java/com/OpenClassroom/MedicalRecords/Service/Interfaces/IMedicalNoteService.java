package com.OpenClassroom.MedicalRecords.Service.Interfaces;

import com.OpenClassroom.MedicalRecords.Model.MedicalNoteEntity;

import java.util.List;

public interface IMedicalNoteService {

    List<MedicalNoteEntity> getPatientAllNotesByPatientId(Integer id);

    List<MedicalNoteEntity> getPatientAllNotesByPatientLastName(String patient_lastName);

    MedicalNoteEntity saveNote(MedicalNoteEntity medicalNoteEntity);

    void deleteById(Integer id);

    List<MedicalNoteEntity> getAllNotes();

    MedicalNoteEntity getNoteById(Integer id);

    MedicalNoteEntity updateNoteById(Integer id, MedicalNoteEntity medicalNoteEntity);

    MedicalNoteEntity addNoteByPatient_Id(Integer patient_id, String note);


}
