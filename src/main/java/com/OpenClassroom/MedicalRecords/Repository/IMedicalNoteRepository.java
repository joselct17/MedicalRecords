package com.OpenClassroom.MedicalRecords.Repository;

import com.OpenClassroom.MedicalRecords.Model.MedicalNoteEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IMedicalNoteRepository extends MongoRepository<MedicalNoteEntity, Integer> {
    List<MedicalNoteEntity> findByPatientId(Integer patientId);

    List<MedicalNoteEntity> findByPatientLastName(String patientLastName);
}
