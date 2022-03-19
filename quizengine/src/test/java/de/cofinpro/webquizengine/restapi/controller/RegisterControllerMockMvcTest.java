package de.cofinpro.webquizengine.restapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.cofinpro.webquizengine.restapi.model.UserRequestBody;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class RegisterControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpHeaders header = new HttpHeaders();

    @BeforeAll
    static void cleanDB() throws Exception {
        try {
            Files.deleteIfExists(Path.of("./src/test/resources/quizDB.mv.db"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    public void setup() {
        header.setBasicAuth("admin@cofinpro.de", "topsecret");
    }

    @Test
    void registerUserValidRequest() throws Exception {
        UserRequestBody userRequest = new UserRequestBody("dummy@company.org", "password");
        mockMvc.perform(post("/api/register").headers(header)
                        .contentType(MediaType.APPLICATION_JSON).content(toJson(userRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void registerUserInValidEmail() throws Exception {
        UserRequestBody userRequest = new UserRequestBody("dummycompany.org", "password");
        mockMvc.perform(post("/api/register").headers(header)
                        .contentType(MediaType.APPLICATION_JSON).content(toJson(userRequest)))
                .andExpect(status().isBadRequest());
    }

    private String toJson(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }
}