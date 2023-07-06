package com.OpenClassroom.MedicalRecords.Services;



import com.OpenClassroom.MedicalRecords.Model.MedicalNoteEntity;
import com.OpenClassroom.MedicalRecords.Repository.IMedicalNoteRepository;
import com.OpenClassroom.MedicalRecords.Service.Implementations.MedicalNoteServiceImpl;
import com.OpenClassroom.MedicalRecords.Service.Implementations.SequenceGeneratorService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.lang.Long.valueOf;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class MedicalNotesServiceTest {


    @Mock
    private IMedicalNoteRepository medicalNoteRepository;

    @Mock
    private SequenceGeneratorService sequenceGeneratorService;

    @InjectMocks
    private MedicalNoteServiceImpl noteService;

    @Test
    public void testGetPatientAllNotesByPatientId() {
        // Créer des données de test
        Integer patientId = 123;
        List<MedicalNoteEntity> mockNotes = Arrays.asList(
                new MedicalNoteEntity(valueOf(1),1,"Jose","Note 1", LocalDateTime.now()),
                new MedicalNoteEntity(valueOf(2),2,"Jose","Note 2", LocalDateTime.now()),
                new MedicalNoteEntity(valueOf(3),3,"Jose","Note 3", LocalDateTime.now())
        );

        // Définir le comportement du repository mock
        when(medicalNoteRepository.findByPatientId(patientId)).thenReturn(mockNotes);

        // Appeler la méthode à tester
        List<MedicalNoteEntity> result = noteService.getPatientAllNotesByPatientId(patientId);

        // Vérifier les résultats
        assertEquals(mockNotes.size(), result.size());
        assertEquals(mockNotes.get(0).getNote(), result.get(0).getNote());
        assertEquals(mockNotes.get(1).getNote(), result.get(1).getNote());
        assertEquals(mockNotes.get(2).getNote(), result.get(2).getNote());

        // Vérifier l'appel au repository
        verify(medicalNoteRepository, times(1)).findByPatientId(patientId);

    }

    @Test
    public void testGetPatientAllNotesByPatientLastName() {
        // Créer des données de test
        String patientLastName = "Doe";
        List<MedicalNoteEntity> mockNotes = Arrays.asList(
                new MedicalNoteEntity(valueOf(1),1,"Jose","Note 1", LocalDateTime.now()),
                new MedicalNoteEntity(valueOf(2),2,"Jose","Note 2", LocalDateTime.now()),
                new MedicalNoteEntity(valueOf(3),3,"Jose","Note 3", LocalDateTime.now())
        );

        // Définir le comportement du repository mock
        when(medicalNoteRepository.findByPatientLastName(patientLastName)).thenReturn(mockNotes);

        // Appeler la méthode à tester
        List<MedicalNoteEntity> result = noteService.getPatientAllNotesByPatientLastName(patientLastName);

        // Vérifier les résultats
        assertEquals(mockNotes.size(), result.size());
        assertEquals(mockNotes.get(0).getNote(), result.get(0).getNote());
        assertEquals(mockNotes.get(1).getNote(), result.get(1).getNote());
        assertEquals(mockNotes.get(2).getNote(), result.get(2).getNote());

        // Vérifier l'appel au repository
        verify(medicalNoteRepository, times(1)).findByPatientLastName(patientLastName);
    }


    @Test
    public void testSaveNote() {
        // Créer une instance de MedicalNoteEntity pour le test
        MedicalNoteEntity medicalNoteEntity = new MedicalNoteEntity();
        medicalNoteEntity.setPatientId(1);
        medicalNoteEntity.setNote("This is a medical note.");

        // Définir le comportement attendu de sequenceGeneratorService.generateSequence()
        when(sequenceGeneratorService.generateSequence(anyString())).thenReturn(1L);

        // Définir le comportement attendu de medicalNoteRepository.save()
        when(medicalNoteRepository.save(any(MedicalNoteEntity.class))).thenReturn(medicalNoteEntity);

        // Appeler la méthode saveNote
        MedicalNoteEntity savedNote = noteService.saveNote(medicalNoteEntity);

        // Vérifier que la séquence a été générée correctement
        verify(sequenceGeneratorService).generateSequence(medicalNoteEntity.SEQUENCE_NAME);

        // Vérifier que la méthode save a été appelée sur medicalNoteRepository avec les bonnes valeurs
        verify(medicalNoteRepository).save(medicalNoteEntity);

        // Vérifier que la note retournée par la méthode saveNote est la même que celle renvoyée par medicalNoteRepository.save()
        assertEquals(medicalNoteEntity, savedNote);
    }

    @Test
    public void testDeleteNoteById_ExistingNote() {
        // Créer un ID de note de test
        Integer noteId = 1;

        // Créer une note mock pour le test
        MedicalNoteEntity mockNote = new MedicalNoteEntity();
        mockNote.setPatientId(noteId);
        mockNote.setNote("Test Note");

        // Définir le comportement du repository mock
        when(medicalNoteRepository.findById(noteId)).thenReturn(Optional.of(mockNote));

        // Appeler la méthode à tester
        assertDoesNotThrow(() -> noteService.deleteNoteById(noteId));

        // Vérifier l'appel au repository
        verify(medicalNoteRepository, times(1)).findById(noteId);
        verify(medicalNoteRepository, times(1)).deleteById(noteId);
    }

    @Test
    public void testDeleteNoteById_NonExistingNote() {
        // Créer un ID de note de test
        Integer noteId = 1;

        // Définir le comportement du repository mock pour une note inexistante
        when(medicalNoteRepository.findById(noteId)).thenReturn(Optional.empty());

        // Appeler la méthode à tester et vérifier que cela lance une exception
        assertThrows(RuntimeException.class, () -> noteService.deleteNoteById(noteId));

        // Vérifier l'appel au repository
        verify(medicalNoteRepository, times(1)).findById(noteId);
        verify(medicalNoteRepository, never()).deleteById(anyInt());
    }


    @Test
    public void testGetAllNotes() {
        // Créer une liste de notes de test
        List<MedicalNoteEntity> mockNotes = new ArrayList<>();
        mockNotes.add(new MedicalNoteEntity( valueOf(1),1, "Jose", "Note 1", LocalDateTime.now()));
        mockNotes.add(new MedicalNoteEntity(valueOf(2),2, "Nina", "Note 2", LocalDateTime.now()));
        mockNotes.add(new MedicalNoteEntity(valueOf(3), 3, "Nona",  "Note 3", LocalDateTime.now()));

        // Définir le comportement du repository mock
        when(medicalNoteRepository.findAll()).thenReturn(mockNotes);

        // Appeler la méthode à tester
        Iterable<MedicalNoteEntity> result = noteService.getAllNotes();

        // Vérifier les résultats
        assertEquals(mockNotes, result);

        // Vérifier l'appel au repository
        verify(medicalNoteRepository, times(1)).findAll();
    }

    @Test
    public void testGetNoteById_ExistingNote() {
        // Créer un ID de note de test
        Integer noteId = 1;

        // Créer une note mock pour le test
        MedicalNoteEntity mockNote = new MedicalNoteEntity();
        mockNote.setPatientId(noteId);
        mockNote.setNote("Test Note");

        // Définir le comportement du repository mock
        when(medicalNoteRepository.findById(noteId)).thenReturn(Optional.of(mockNote));

        // Appeler la méthode à tester
        MedicalNoteEntity result = noteService.getNoteById(noteId);

        // Vérifier les résultats
        assertEquals(mockNote, result);

        // Vérifier l'appel au repository
        verify(medicalNoteRepository, times(1)).findById(noteId);
    }

    @Test
    public void testGetNoteById_NonExistingNote() {
        // Créer un ID de note de test
        Integer noteId = 1;

        // Définir le comportement du repository mock pour une note inexistante
        when(medicalNoteRepository.findById(noteId)).thenReturn(Optional.empty());

        // Appeler la méthode à tester et vérifier que cela lance une exception
        assertThrows(RuntimeException.class, () -> noteService.getNoteById(noteId));

        // Vérifier l'appel au repository
        verify(medicalNoteRepository, times(1)).findById(noteId);
    }

    @Test
    public void testUpdateNoteById_ExistingNote() {
        // Créer un ID de note de test
        Integer noteId = 1;

        // Créer une note mock pour le test
        MedicalNoteEntity existingNote = new MedicalNoteEntity();
        existingNote.setPatientId(1);
        existingNote.setPatientLastName("Doe");
        existingNote.setNote("Existing Note");
        existingNote.setDateTimeAtCreation(LocalDateTime.now());

        // Créer une note de mise à jour de test
        MedicalNoteEntity updatedNote = new MedicalNoteEntity();
        updatedNote.setPatientId(1);
        updatedNote.setPatientLastName("Doe");
        updatedNote.setNote("Updated Note");
        updatedNote.setDateTimeAtCreation(LocalDateTime.now());

        // Définir le comportement du repository mock
        when(medicalNoteRepository.findById(noteId)).thenReturn(Optional.of(existingNote));
        when(medicalNoteRepository.save(existingNote)).thenReturn(existingNote);

        // Appeler la méthode à tester
        MedicalNoteEntity result = noteService.updateNoteById(noteId, updatedNote);

        // Vérifier les résultats
        assertEquals(existingNote, result);
        assertEquals(updatedNote.getPatientId(), existingNote.getPatientId());
        assertEquals(updatedNote.getPatientLastName(), existingNote.getPatientLastName());
        assertEquals(updatedNote.getNote(), existingNote.getNote());
        assertEquals(updatedNote.getDateTimeAtCreation(), existingNote.getDateTimeAtCreation());

        // Vérifier l'appel au repository
        verify(medicalNoteRepository, times(1)).findById(noteId);
        verify(medicalNoteRepository, times(1)).save(existingNote);
    }

    @Test
    public void testUpdateNoteById_NonExistingNote() {
        // Créer un ID de note de test
        Integer noteId = 1;

        // Définir le comportement du repository mock pour une note inexistante
        when(medicalNoteRepository.findById(noteId)).thenReturn(Optional.empty());

        // Créer une note de mise à jour de test
        MedicalNoteEntity updatedNote = new MedicalNoteEntity();
        updatedNote.setPatientId(2);
        updatedNote.setPatientLastName("Smith");
        updatedNote.setNote("Updated Note");
        updatedNote.setDateTimeAtCreation(LocalDateTime.now());

        // Appeler la méthode à tester et vérifier que cela lance une exception
        assertThrows(RuntimeException.class, () -> noteService.updateNoteById(noteId, updatedNote));

        // Vérifier l'appel au repository
        verify(medicalNoteRepository, times(1)).findById(noteId);
        verify(medicalNoteRepository, never()).save(any());
    }

    @Test
    public void testAddNoteByPatient_Id_ExistingPatient() {
        // Créer un ID de patient de test
        Integer patientId = 1;

        // Créer une liste de notes de test pour le patient existant
        List<MedicalNoteEntity> existingNotes = new ArrayList<>();
        existingNotes.add(new MedicalNoteEntity( valueOf(1),1, "Doe", "Note 1", LocalDateTime.now()));
        existingNotes.add(new MedicalNoteEntity(valueOf(1), 1, "Doe", "Note 2", LocalDateTime.now()));

        // Définir le comportement du repository mock
        when(medicalNoteRepository.findByPatientId(patientId)).thenReturn(existingNotes);
        when(medicalNoteRepository.save(any())).thenReturn(new MedicalNoteEntity( valueOf(1),1, "Doe", "New Note", LocalDateTime.now()));

        // Appeler la méthode à tester
        MedicalNoteEntity result = noteService.addNoteByPatient_Id(patientId, "New Note");

        // Vérifier les résultats
        assertNotNull(result);
        assertEquals(existingNotes.get(0).getPatientLastName(), result.getPatientLastName());
        assertEquals("New Note", result.getNote());

        // Vérifier l'appel au repository
        verify(medicalNoteRepository, times(1)).findByPatientId(patientId);
        verify(medicalNoteRepository, times(1)).save(any());
    }

    @Test
    public void testAddNoteByPatient_Id_NonExistingPatient() {
        // Créer un ID de patient de test
        Integer patientId = 1;

        // Définir le comportement du repository mock pour un patient inexistant
        when(medicalNoteRepository.findByPatientId(patientId)).thenReturn(new ArrayList<>());

        // Appeler la méthode à tester et vérifier que cela lance une exception
        assertThrows(RuntimeException.class, () -> noteService.addNoteByPatient_Id(patientId, "New Note"));

        // Vérifier l'appel au repository
        verify(medicalNoteRepository, times(1)).findByPatientId(patientId);
        verify(medicalNoteRepository, never()).save(any());
    }


    @Test
    public void testDeleteNoteByPatientId() {
        // Créer une liste de MedicalNoteEntity pour le test
        List<MedicalNoteEntity> notes = new ArrayList<>();
        notes.add(new MedicalNoteEntity());
        notes.add(new MedicalNoteEntity());

        // Définir le comportement attendu de medicalNoteRepository.findByPatientId()
        when(medicalNoteRepository.findByPatientId(any(Integer.class))).thenReturn(notes);

        // Appeler la méthode deleteNoteByPatientId
        noteService.deleteNoteByPatientId(1);

        // Vérifier que la méthode findByPatientId a été appelée sur medicalNoteRepository avec la bonne valeur
        verify(medicalNoteRepository).findByPatientId(1);

        // Vérifier que la méthode deleteAll a été appelée sur medicalNoteRepository avec la liste de notes
        verify(medicalNoteRepository).deleteAll(notes);
    }

    @Test
    public void testDeleteNoteByPatientId_NoNotesFound() {
        // Définir le comportement attendu de medicalNoteRepository.findByPatientId() pour renvoyer une liste vide
        when(medicalNoteRepository.findByPatientId(any(Integer.class))).thenReturn(new ArrayList<>());

        // Appeler la méthode deleteNoteByPatientId et vérifier qu'une exception est lancée
        assertThrows(RuntimeException.class, () -> noteService.deleteNoteByPatientId(1));

        // Vérifier que la méthode findByPatientId a été appelée sur medicalNoteRepository avec la bonne valeur
        verify(medicalNoteRepository).findByPatientId(1);

        // Vérifier qu'il n'y a pas d'interactions supplémentaires avec medicalNoteRepository
        verifyNoMoreInteractions(medicalNoteRepository);
    }


}