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
    SequenceGeneratorService sequenceGeneratorService;
    @Autowired
    IMedicalNoteRepository medicalNoteRepository;
    @Override
    public List<MedicalNoteEntity> getPatientAllNotesByPatientId(Integer patientId) {
        logger.debug("getPatientAllNotesByPatientId starts here, from NoteServiceImpl");
        List<MedicalNoteEntity> notes = medicalNoteRepository.findByPatientId(patientId);
        logger.info("AllNotes of the patient with patient_id:{} have been successfully retrieved, MedicalNoteServiceImpl", patientId);
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
        medicalNoteEntity.setId(sequenceGeneratorService.generateSequence(medicalNoteEntity.SEQUENCE_NAME));
        MedicalNoteEntity noteSaved = medicalNoteRepository.save(medicalNoteEntity);
        logger.info("Note with Id:{} has been successfully save", medicalNoteEntity.getPatientId());
        return noteSaved;

    }

    @Override
    public void deleteNoteById(Integer id) {
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
    public void deleteNoteByPatientId(Integer patientId) {
        logger.debug("deleteNoteByPatientId method starts here, from NoteServiceImpl");

        List<MedicalNoteEntity> notes = medicalNoteRepository.findByPatientId(patientId);

        if (!notes.isEmpty()) {
            logger.info("Notes with patientId:{} have been successfully deleted", patientId);
            medicalNoteRepository.deleteAll(notes);
        } else {
            logger.debug("No notes found with patientId:{}", patientId);
            throw new RuntimeException("No notes found with patientId:" + patientId);
        }
}


    @Override
    public Iterable<MedicalNoteEntity> getAllNotes() {
        logger.debug("getAllNotes starts here,  MedicalNoteServiceImpl");
      Iterable<MedicalNoteEntity> allNotes = medicalNoteRepository.findAll();
        logger.info("All notes have been successfully retrieved");
        return allNotes;
    }

    @Override
    public MedicalNoteEntity getNoteById(Integer id) {
        logger.debug("getNoteById method starts here, MedicalNoteServiceImpl");
        MedicalNoteEntity medicalNoteEntity = medicalNoteRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Note with id:{} not found in the data base, from MedicalNoteServiceImpl", id);
                    return new RuntimeException(String.format("Note with id:%d not found in DB!", id));
                });
        return medicalNoteEntity;
    }

    @Override
    public MedicalNoteEntity updateNoteById(Integer id, MedicalNoteEntity medicalNoteEntity) {
        logger.debug("updateNoteById method starts here, from MedicalNoteServiceImpl");
        Optional<MedicalNoteEntity> medicalNoteById  = medicalNoteRepository.findById(id);
        if (medicalNoteById.isPresent()) {
            MedicalNoteEntity noteUpdated = medicalNoteById.get();
            medicalNoteById.get().setPatientId(medicalNoteEntity.getPatientId());
            medicalNoteById.get().setNote(medicalNoteEntity.getNote());
            medicalNoteRepository.save(medicalNoteById.get());
            logger.info("Note with id:{} exist in DB!", id);
            return noteUpdated;
        }else {
            logger.debug("Any note exist with id:{} in DB!", id);
            throw new RuntimeException("Any note  exist with id:{%s}".formatted(id));
        }
    }

    @Override
    public MedicalNoteEntity addNoteByPatient_Id(Integer patientId, String note) {
        logger.debug("addNoteByPatId stats here, from NoteServiceImpl");
        List<MedicalNoteEntity> notesByPatient_Id = medicalNoteRepository.findByPatientId(patientId);

        if (notesByPatient_Id.isEmpty()) {
            logger.error("No notes with this patient_id:{%d}".formatted(patientId));
            throw new RuntimeException("No note with this patient_id:{%d}".formatted(patientId));
        }

        MedicalNoteEntity medicalNoteNew = new MedicalNoteEntity();
        medicalNoteNew.setPatientId(patientId);
        medicalNoteNew.setDateTimeAtCreation(LocalDateTime.now());
        medicalNoteNew.setNote(note);
        medicalNoteNew.setPatientLastName(notesByPatient_Id.get(0).getPatientLastName());

        MedicalNoteEntity newMedicalNoteSaved = medicalNoteRepository.save(medicalNoteNew);

        logger.info("New Note has been successfully added to the Patient Id:{%d}".formatted(patientId));

        return newMedicalNoteSaved;
    }


}
