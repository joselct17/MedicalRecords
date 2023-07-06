package com.OpenClassroom.MedicalRecords.Controller;

import com.OpenClassroom.MedicalRecords.Model.MedicalNoteEntity;
import com.OpenClassroom.MedicalRecords.Service.Implementations.MedicalNoteServiceImpl;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/notes")
public class MedicalNoteController {

    @Autowired
    MedicalNoteServiceImpl medicalNoteService;
    private final static Logger logger = LoggerFactory.getLogger(MedicalNoteController.class);


    @GetMapping
    public Iterable<MedicalNoteEntity> getAllNotes() {
        logger.info("GET:/notes/");
        Iterable<MedicalNoteEntity> notes = medicalNoteService.getAllNotes();
        return notes;
    }

    @GetMapping("/by-patientId/{patientId}")
    public ResponseEntity<List<MedicalNoteEntity>> getAllNotesOfPatientPatId(@PathVariable Integer patientId) {
        logger.debug("getAllNotesOfPatientByPatId starts here, from MedicalNoteController");
        List<MedicalNoteEntity> notes = medicalNoteService.getPatientAllNotesByPatientId(patientId);
        logger.info("All Notes of this patient with patientId:{%d} have been retrieved from DB!".formatted(patientId));
        return ResponseEntity.ok(notes);
    }

    @GetMapping("/by-lastName/{patientLastName}")
    public ResponseEntity<List<MedicalNoteEntity>> getAllNotesOfPatientPatId(@PathVariable String patientLastName) {
        logger.debug("getAllNotesOfPatientByPatId starts here, from MedicalNoteController");
        List<MedicalNoteEntity> notes = medicalNoteService.getPatientAllNotesByPatientLastName(patientLastName);
        logger.info("All Notes of this patient with patientId:{%s} have been retrieved from DB!".formatted(patientLastName));
        return ResponseEntity.ok(notes);
    }

    @PostMapping
    public ResponseEntity<MedicalNoteEntity> addNote(@RequestBody @Valid MedicalNoteEntity medicalNoteEntity) {
        logger.debug("addNote starts here, from MedicalNoteController");
        MedicalNoteEntity noteSaved = medicalNoteService.saveNote(medicalNoteEntity);
        logger.info("New note with pathPatId:{} and lastName:{} has been successfully saved, from MedicalNoteController", medicalNoteEntity.getPatientId(), medicalNoteEntity.getPatientLastName());
        return new ResponseEntity<>(noteSaved, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicalNoteEntity> updateById(@PathVariable Integer id, @RequestBody @Valid MedicalNoteEntity medicalNote) {
        logger.debug("updateById starts here, from MedicalNoteController");
        MedicalNoteEntity noteUpdated = medicalNoteService.updateNoteById(id, medicalNote);
        logger.info("Note with id:{} has been successfully updated, from MedicalNoteController", id);
        return ResponseEntity.ok(noteUpdated);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable Integer id) {
        logger.debug("deleteById starts here, from MedicalNoteController");
        try {
            medicalNoteService.deleteNoteById(id);
            logger.info("Note with id:{} has been successfully deleted from MedicalNoteController", id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Note with id:{} not found DB from MedicalNoteController", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @DeleteMapping("/by-patientId/{patientId}")
    public ResponseEntity<HttpStatus> deleteByPatientId(@PathVariable Integer patientId) {
        logger.debug("deleteById starts here, from MedicalNoteController");
        try {
            medicalNoteService.deleteNoteByPatientId(patientId);
            logger.info("Note with patientId:{} has been successfully deleted from MedicalNoteController", patientId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Note with patientId:{} not found DB from MedicalNoteController", patientId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalNoteEntity> getNoteById(@PathVariable Integer id) {
        logger.debug("getNoteById method starts here, from MedicalNoteController");
        MedicalNoteEntity noteById = medicalNoteService.getNoteById(id);
        logger.info("Note with id:{} has been successfully retrieved, from MedicalNoteController", id);
        return ResponseEntity.ok(noteById);
    }

    @PostMapping("/{patientId}")
    public ResponseEntity<MedicalNoteEntity> addNoteToPatientByPatId(@PathVariable Integer patientId, @RequestParam String note) {
        logger.debug("addNoteToPatientByPatId starts here, from MedicalNoteController");
        MedicalNoteEntity noteSaved = medicalNoteService.addNoteByPatient_Id(patientId, note);
        logger.info("New note with pathPatId:{} and lastName:{} has been successfully saved, from NoteController", patientId, noteSaved.getPatientLastName());
        return new ResponseEntity<>(noteSaved, HttpStatus.CREATED);
    }





}
