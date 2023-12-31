package com.OpenClassroom.MedicalRecords.Controller;

import com.OpenClassroom.MedicalRecords.Model.MedicalNoteEntity;
import com.OpenClassroom.MedicalRecords.Service.Implementations.MedicalNoteServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import java.time.LocalDateTime;

import java.util.List;


import static java.lang.Long.valueOf;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MedicalNoteController.class)
class NoteControllerTest {

    @Autowired
   private MockMvc mockMvc;

    @MockBean
    private MedicalNoteServiceImpl medicalNoteService;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void testGetAllNotes() throws Exception {
        // Given
        List<MedicalNoteEntity> expectedNotes;
        MedicalNoteEntity note1 = new MedicalNoteEntity(valueOf(1),1, "Jose", "Comment 1", LocalDateTime.now());
        MedicalNoteEntity note2 = new MedicalNoteEntity( valueOf(2),2, "Luis", "Comment 2", LocalDateTime.now());

        expectedNotes = List.of(note1, note2);
        when(medicalNoteService.getAllNotes()).thenReturn(expectedNotes);
        // When
        mockMvc.perform(get("/api/notes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].patientLastName", is("Jose")))
                .andReturn();
        // Then
        verify(medicalNoteService).getAllNotes();
    }

    @Test
    void testGetAllNotesOfPatientByPatId() throws Exception {
        // Given
        List<MedicalNoteEntity> expectedNotes;
        MedicalNoteEntity note1 = new MedicalNoteEntity(valueOf(2),2 ,"Jose", "Comment 1", LocalDateTime.now());
        MedicalNoteEntity note2 = new MedicalNoteEntity(valueOf(3),3, "Jose", "Comment 2", LocalDateTime.now());

        expectedNotes = List.of(note1, note2);
        when(medicalNoteService.getPatientAllNotesByPatientId(anyInt())).thenReturn(expectedNotes);

        // When
        mockMvc.perform(get("/api/notes/by-patientId/{patientId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[1].patientLastName", is("Jose")))
                .andExpect(jsonPath("$.length()", is(2)))
                .andReturn();

        verify(medicalNoteService).getPatientAllNotesByPatientId(anyInt());
    }

    @Test
    void testGetAllNotesOfPatientLastName() throws Exception {
        // Given
        List<MedicalNoteEntity> expectedNotes;
        MedicalNoteEntity note1 = new MedicalNoteEntity(valueOf(2), 2 ,"Doe", "Comment 1", LocalDateTime.now());
        MedicalNoteEntity note2 = new MedicalNoteEntity( valueOf(3),3, "Doe", "Comment 2", LocalDateTime.now());
        expectedNotes = List.of(note1, note2);

        when(medicalNoteService.getPatientAllNotesByPatientLastName(anyString())).thenReturn(expectedNotes);

        // When
        mockMvc.perform(get("/api/notes/by-lastName/{patientLastName}", "Doe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].patientLastName", is("Doe")))
                .andExpect(jsonPath("$.length()", is(2)))
                .andReturn();

        verify(medicalNoteService).getPatientAllNotesByPatientLastName(anyString());
    }



    @Test
    void testAddNote() throws Exception {
        // Given
        MedicalNoteEntity newNoteToSaved = new MedicalNoteEntity( valueOf(25),25, "Jose", "Comment 1", LocalDateTime.now());

        when(medicalNoteService.saveNote(newNoteToSaved)).thenReturn(newNoteToSaved);

        // When
        mockMvc.perform(post("/api/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newNoteToSaved)))
                .andExpect(status().isCreated())
                .andReturn();
        // Then
        verify(medicalNoteService).saveNote(any(MedicalNoteEntity.class));
    }


    @Test
    void testUpdateById() throws Exception {
        // Given
        Integer noteId = 3;
        MedicalNoteEntity updatedNote = new MedicalNoteEntity(valueOf(3), 3 ,"Jean", "Comment original", LocalDateTime.now());
        updatedNote.setPatientId(noteId);
        MedicalNoteEntity noteUpdated = new MedicalNoteEntity( valueOf(3),3, "Jean", "Comment updated", LocalDateTime.now());
        noteUpdated.setPatientId(noteId);
        when(medicalNoteService.updateNoteById(noteId, updatedNote)).thenReturn(noteUpdated);

        // When
        mockMvc.perform(put("/api/notes/{id}", noteId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedNote)))
                .andExpect(status().isOk())
                .andReturn();
        // Then
        verify(medicalNoteService).updateNoteById(anyInt(), any(MedicalNoteEntity.class));
    }



    @Test
    void testDeleteById() throws Exception {
        // Given
        Integer noteId = 5;
        MedicalNoteEntity existingNote = new MedicalNoteEntity( valueOf(3),3, "Joe", "Comment before", LocalDateTime.now());
        existingNote.setPatientId(noteId);
        when(medicalNoteService.getNoteById(anyInt())).thenReturn(existingNote);
        doNothing().when(medicalNoteService).deleteNoteById(noteId);

        // When
        mockMvc.perform(delete("/api/notes/{id}", noteId))
                .andExpect(status().isOk());

        // Then
        verify(medicalNoteService).deleteNoteById(anyInt());

    }

    @Test
    public void testDeleteById_NotFound() throws Exception {
        Integer id = 123;

        doThrow(new RuntimeException("No notes found with id:" + id))
                .when(medicalNoteService).deleteNoteById(id);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/notes/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(medicalNoteService).deleteNoteById(id);
    }

    @Test
    void testGetNoteById() throws Exception {
        // Given
        Integer noteId = 3;
        MedicalNoteEntity existingNote = new MedicalNoteEntity(valueOf(20), 20, "BigBoss", "Comment before", LocalDateTime.now());
        existingNote.setPatientId(noteId);
        when(medicalNoteService.getNoteById(anyInt())).thenReturn(existingNote);

        // when
        mockMvc.perform(get("/api/notes/{id}", noteId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patientLastName", is("BigBoss")));
    }


    @Test
    void testAddNoteToPatientByPatId() throws Exception {
        // Given
        Integer noteId = 54;
        MedicalNoteEntity existingNote = new MedicalNoteEntity(valueOf(4),4, "Marc", "Comment before", LocalDateTime.now());
        existingNote.setPatientId(noteId);

        String newComment = "Fat as a cow,";
        MedicalNoteEntity noteSaved = new MedicalNoteEntity( valueOf(25), 25, "Marc", newComment, LocalDateTime.now());
        existingNote.setPatientId(noteId);

        when(medicalNoteService.addNoteByPatient_Id(22, newComment)).thenReturn(noteSaved);

        // When
        mockMvc.perform(post("/api/notes/{patientId}", 22)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("note",newComment))
                .andExpect(status().isCreated())
                .andReturn();

        // Then
        verify(medicalNoteService).addNoteByPatient_Id(anyInt(), anyString());

    }

    @Test
    public void testDeleteByPatientId() throws Exception {
        Integer patientId = 123;

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/notes/by-patientId/{patientId}", patientId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(medicalNoteService).deleteNoteByPatientId(patientId);
    }

    @Test
    public void testDeleteByPatientId_NotFound() throws Exception {
        Integer patientId = 123;

        doThrow(new RuntimeException("No notes found with patientId:" + patientId))
                .when(medicalNoteService).deleteNoteByPatientId(patientId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/notes/by-patientId/{patientId}", patientId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(medicalNoteService).deleteNoteByPatientId(patientId);
    }

}
