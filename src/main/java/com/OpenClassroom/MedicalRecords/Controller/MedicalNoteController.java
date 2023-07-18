package com.OpenClassroom.MedicalRecords.Controller;

import com.OpenClassroom.MedicalRecords.Model.MedicalNoteEntity;
import com.OpenClassroom.MedicalRecords.Service.Implementations.MedicalNoteServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * MedicalNotesController handles all HTTP requests related to medical notes.
 * It exposes the notes-related APIs to the clients.
 */
@RestController
@RequestMapping("api/notes")
public class MedicalNoteController {

    @Autowired
    MedicalNoteServiceImpl medicalNoteService;
    private final static Logger logger = LoggerFactory.getLogger(MedicalNoteController.class);

    @Operation(summary = "Obtenir toutes les notes")
    @GetMapping
    public Iterable<MedicalNoteEntity> getAllNotes() {
        logger.info("GET:/notes/");
        Iterable<MedicalNoteEntity> notes = medicalNoteService.getAllNotes();
        return notes;
    }

    @Operation(summary = "Obtenir toutes les notes d'un patient par ID de patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notes du patient récupérées avec succès"),
            @ApiResponse(responseCode = "404", description = "Patient non trouvé")
    })
    @GetMapping("/by-patientId/{patientId}")
    public ResponseEntity<List<MedicalNoteEntity>> getAllNotesOfPatientPatId(@Parameter(description = "ID du patient") @PathVariable Integer patientId) {
        logger.debug("getAllNotesOfPatientByPatId starts here, from MedicalNoteController");
        List<MedicalNoteEntity> notes = medicalNoteService.getPatientAllNotesByPatientId(patientId);
        logger.info("All Notes of this patient with patientId:{%d} have been retrieved from DB!".formatted(patientId));
        return ResponseEntity.ok(notes);
    }

    @Operation(summary = "Obtenir toutes les notes d'un patient par nom de famille du patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notes du patient récupérées avec succès"),
            @ApiResponse(responseCode = "404", description = "Patient non trouvé")
    })
    @GetMapping("/by-lastName/{patientLastName}")
    public ResponseEntity<List<MedicalNoteEntity>> getAllNotesOfPatientPatId( @Parameter(description = "Nom de famille du patient") @PathVariable String patientLastName) {
        logger.debug("getAllNotesOfPatientByPatId starts here, from MedicalNoteController");
        List<MedicalNoteEntity> notes = medicalNoteService.getPatientAllNotesByPatientLastName(patientLastName);
        logger.info("All Notes of this patient with patientId:{%s} have been retrieved from DB!".formatted(patientLastName));
        return ResponseEntity.ok(notes);
    }

    @Operation(summary = "Ajouter une note")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Note ajoutée avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide")
    })
    @PostMapping
    public ResponseEntity<MedicalNoteEntity> addNote( @Parameter(description = "Entité de la note médicale") @RequestBody @Valid MedicalNoteEntity medicalNoteEntity) {
        logger.debug("addNote starts here, from MedicalNoteController");
        MedicalNoteEntity noteSaved = medicalNoteService.saveNote(medicalNoteEntity);
        logger.info("New note with pathPatId:{} and lastName:{} has been successfully saved, from MedicalNoteController", medicalNoteEntity.getPatientId(), medicalNoteEntity.getPatientLastName());
        return new ResponseEntity<>(noteSaved, HttpStatus.CREATED);
    }

    @Operation(summary = "Mettre à jour une note par ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Note mise à jour avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "404", description = "Note non trouvée")
    })
    @PutMapping("/{id}")
    public ResponseEntity<MedicalNoteEntity> updateById( @Parameter(description = "ID de la note") @PathVariable Integer id, @Parameter(description = "Entité de la note médicale") @RequestBody @Valid MedicalNoteEntity medicalNote) {
        logger.debug("updateById starts here, from MedicalNoteController");
        MedicalNoteEntity noteUpdated = medicalNoteService.updateNoteById(id, medicalNote);
        logger.info("Note with id:{} has been successfully updated, from MedicalNoteController", id);
        return ResponseEntity.ok(noteUpdated);
    }

    @Operation(summary = "Supprimer une note par ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Note supprimée avec succès"),
            @ApiResponse(responseCode = "204", description = "Note non trouvée")
    })
    @DeleteMapping("{id}")
    public ResponseEntity<HttpStatus> deleteById( @Parameter(description = "ID de la note") @PathVariable Integer id) {
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

    @Operation(summary = "Supprimer toutes les notes d'un patient par ID de patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notes du patient supprimées avec succès"),
            @ApiResponse(responseCode = "204", description = "Patient non trouvé")
    })
    @DeleteMapping("/by-patientId/{patientId}")
    public ResponseEntity<HttpStatus> deleteByPatientId( @Parameter(description = "ID du patient") @PathVariable Integer patientId) {
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

    @Operation(summary = "Obtenir une note par son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Note récupérée avec succès"),
            @ApiResponse(responseCode = "404", description = "Note non trouvée")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MedicalNoteEntity> getNoteById( @Parameter(description = "ID de la note") @PathVariable Integer id) {
        logger.debug("getNoteById method starts here, from MedicalNoteController");
        MedicalNoteEntity noteById = medicalNoteService.getNoteById(id);
        logger.info("Note with id:{} has been successfully retrieved, from MedicalNoteController", id);
        return ResponseEntity.ok(noteById);
    }

    @Operation(summary = "Ajouter une note à un patient par ID de patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Note ajoutée avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "404", description = "Patient non trouvé")
    })
    @PostMapping("/{patientId}")
    public ResponseEntity<MedicalNoteEntity> addNoteToPatientByPatId(  @Parameter(description = "ID du patient") @PathVariable Integer patientId, @Parameter(description = "Contenu de la note") @RequestParam String note) {
        logger.debug("addNoteToPatientByPatId starts here, from MedicalNoteController");
        MedicalNoteEntity noteSaved = medicalNoteService.addNoteByPatient_Id(patientId, note);
        logger.info("New note with pathPatId:{} and lastName:{} has been successfully saved, from NoteController", patientId, noteSaved.getPatientLastName());
        return new ResponseEntity<>(noteSaved, HttpStatus.CREATED);
    }





}
