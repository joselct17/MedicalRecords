package com.OpenClassroom.MedicalRecords.Controller;

import com.OpenClassroom.MedicalRecords.Model.MedicalNoteEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;


import com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MedicalNoteControllerTest {

    @RunWith(SpringRunner.class)
    @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
    @AutoConfigureMockMvc
    public class MedicalNoteControllerIntegrationTest {

        @Autowired
        private MockMvc mockMvc;

        @Test
        public void testGetAllNotes() throws Exception {
            mockMvc.perform(get("/notes"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(2))); // Remplacer "2" par le nombre attendu de notes dans la réponse
        }

        @Test
        public void testGetAllNotesOfPatientByPatId() throws Exception {
            mockMvc.perform(get("/notes/by-patId/{patient_id}", 1))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(2))); // Remplacer "2" par le nombre attendu de notes dans la réponse
        }

        @Test
        public void testGetAllNotesOfPatientByLastName() throws Exception {
            mockMvc.perform(get("/notes/by-lastName/{patientLastName}", "Smith"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(2))); // Remplacer "2" par le nombre attendu de notes dans la réponse
        }

        @Test
        public void testAddNote() throws Exception {
            MedicalNoteEntity medicalNoteEntity = new MedicalNoteEntity();
            // Définir les propriétés de medicalNoteEntity

            mockMvc.perform(post("/notes")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(medicalNoteEntity)))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id", notNullValue())); // Vérifier que l'ID de la note créée n'est pas nul
        }

        @Test
        public void testUpdateById() throws Exception {
            MedicalNoteEntity medicalNoteEntity = new MedicalNoteEntity();
            // Définir les propriétés de medicalNoteEntity

            mockMvc.perform(put("/notes/{id}", 1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(medicalNoteEntity)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id", is(1))); // Vérifier que l'ID de la note mise à jour correspond à celui spécifié
        }

        @Test
        public void testDeleteById() throws Exception {
            mockMvc.perform(delete("/notes/{id}", 1))
                    .andExpect(status().isOk());
            // Ajoutez d'autres assertions si nécessaire
        }

        @Test
        public void testGetNoteById() throws Exception {
            mockMvc.perform(get("/notes/{id}", 1))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id", is(1))); // Vérifier que l'ID de la note récupérée correspond à celui spécifié
        }

        @Test
        public void testAddNoteToPatientByPatId() throws Exception {
            mockMvc.perform(post("/notes/{patId}", 1)
                            .param("note", "Sample note"))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id", notNullValue())); // Vérifier que l'ID de la note créée n'est pas nul
        }

        // Méthode utilitaire pour convertir un objet en chaîne JSON
        private static String asJsonString(Object obj) {
            try {
                return new ObjectMapper().writeValueAsString(obj);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


}
