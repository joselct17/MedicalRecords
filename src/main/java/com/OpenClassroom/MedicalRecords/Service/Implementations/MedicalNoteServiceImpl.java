package com.OpenClassroom.MedicalRecords.Service.Implementations;

import com.OpenClassroom.MedicalRecords.Model.MedicalNoteEntity;
import com.OpenClassroom.MedicalRecords.Repository.IMedicalNoteRepository;
import com.OpenClassroom.MedicalRecords.Service.Interfaces.IMedicalNoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class MedicalNoteServiceImpl implements IMedicalNoteService {

    private Logger logger = LoggerFactory.getLogger(MedicalNoteServiceImpl.class);
    @Autowired
    IMedicalNoteRepository medicalNoteRepository;
    @Override
    public List<MedicalNoteEntity> getPatientAllNotesByPatientId(Integer patient_id) {
        logger.debug("getPatientAllNotesByPatientId starts here, from NoteServiceImpl");
        List<MedicalNoteEntity> notes = medicalNoteRepository.findByPatientId(patient_id);
        logger.info("AllNotes of the patient with patient_id:{} have been successfully retrieved, MedicalNoteServiceImpl", patient_id);
        return notes;

    }

    @Override
    public List<MedicalNoteEntity> getPatientAllNotesByPatientLastName(String patientLastName) {
        logger.debug("getPatientAllNotesByPatientLastName starts here, from NoteServiceImpl");
        List<MedicalNoteEntity> notes = medicalNoteRepository.findByPatientLastName(patientLastName);
        logger.info("AllNotes of the patient with patId:{%s} have been successfully retrieved, from NoteServiceImpl".formatted(patientLastName));
        return notes;
    }


    @Override
    public MedicalNoteEntity saveNote(MedicalNoteEntity medicalNoteEntity) {
        logger.debug("saveNote method starts here, from MedicalNoteServiceImpl");
        MedicalNoteEntity noteSaved = medicalNoteRepository.save(medicalNoteEntity);
        logger.info("Note with Id:{} has been successfully save", medicalNoteEntity.getId());
        return noteSaved;

    }

    @Override
    public void deleteById(Integer id) {
        logger.debug("deleteById method starts here, from NoteServiceImpl");
        Optional<MedicalNoteEntity> noteById = medicalNoteRepository.findById(id);

        if(noteById.isPresent()) {
            logger.info("Note with id:{} has been successfully deleted", id);
            medicalNoteRepository.deleteById(id);
        } else {
            logger.debug("Note with id:{} doesn't in DB!", id);
            throw new RuntimeException("Note with id:{%s} doesn't exist in DB!");
        }

    }

    @Override
    public List<MedicalNoteEntity> getAllNotes() {
        logger.debug("getAllNotes starts here,  MedicalNoteServiceImpl");
      List<MedicalNoteEntity> allNotes = medicalNoteRepository.findAll();
        logger.info("All notes have been successfully retrieved");
        return allNotes;
    }

    @Override
    public MedicalNoteEntity getNoteById(Integer id) {
        logger.debug("getNoteById method starts here, MedicalNoteServiceImpl");
        Optional<MedicalNoteEntity> medicalNoteEntityById = medicalNoteRepository.findById(id);
        if (medicalNoteEntityById.isPresent()) {
            logger.info("Note with id:{%s} has been not found in de data base, MedicalNoteServiceImpl");
            return medicalNoteEntityById.get();
        }else {
            logger.error("Note with id:{} not found in the data base, from MedicalNoteServiceImpl", id);
            throw new RuntimeException("Note with id:{%s} not found in DB!");
        }
    }

    @Override
    public MedicalNoteEntity updateNoteById(Integer id, MedicalNoteEntity medicalNoteEntity) {
        logger.debug("updateNoteById method starts here, from MedicalNoteServiceImpl");
        Optional<MedicalNoteEntity> medicalNoteById  = medicalNoteRepository.findById(id);
        if (medicalNoteById.isPresent()) {
            MedicalNoteEntity noteUpdated = medicalNoteById.get();
            medicalNoteById.get().setPatient_id(medicalNoteEntity.getPatient_id());
            medicalNoteById.get().setPatientLastName(medicalNoteEntity.getPatientLastName());
            medicalNoteById.get().setNote(medicalNoteEntity.getNote());
            medicalNoteById.get().setDateTimeAtCreation(medicalNoteEntity.getDateTimeAtCreation());
            medicalNoteRepository.save(medicalNoteById.get());
            logger.info("Note with id:{} doesn't exist in DB!", id);
            return noteUpdated;
        }else {
            logger.debug("Any note doesn't exist with id:{} in DB!", id);
            throw new RuntimeException("Any note doesn't exist with id:{%s}".formatted(id));
        }
    }

    @Override
    public MedicalNoteEntity addNoteByPatient_Id(Integer patient_id, String note) {
        logger.debug("addNoteByPatId stats here, from NoteServiceImpl");
        List<MedicalNoteEntity> notesByPatient_Id = medicalNoteRepository.findByPatientId(patient_id);

        if (notesByPatient_Id.isEmpty()) {
            logger.error("No notes with this patient_id:{%d}".formatted(patient_id));
            throw new RuntimeException("No note with this patient_id:{%d}".formatted(patient_id));
        }

        MedicalNoteEntity medicalNoteNew = new MedicalNoteEntity();
        medicalNoteNew.setPatient_id(patient_id);
        medicalNoteNew.setDateTimeAtCreation(LocalDateTime.now());
        medicalNoteNew.setNote(note);
        medicalNoteNew.setPatientLastName(notesByPatient_Id.get(0).getPatientLastName());

        MedicalNoteEntity newMedicalNoteSaved = medicalNoteRepository.save(medicalNoteNew);

        logger.info("New Note has been successfully added to the Patient Id:{%d}".formatted(patient_id));

        return newMedicalNoteSaved;
    }


}
