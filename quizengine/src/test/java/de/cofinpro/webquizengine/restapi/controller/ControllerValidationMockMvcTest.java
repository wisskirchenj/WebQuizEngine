package de.cofinpro.webquizengine.restapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.cofinpro.webquizengine.configuration.WebQuizConfiguration;
import de.cofinpro.webquizengine.restapi.model.QuizPatchRequestBody;
import de.cofinpro.webquizengine.restapi.model.QuizRequestBody;
import de.cofinpro.webquizengine.restapi.model.QuizResponse;
import de.cofinpro.webquizengine.restapi.model.UserRequestBody;
import de.cofinpro.webquizengine.restapi.service.QuizNotFoundException;
import de.cofinpro.webquizengine.restapi.service.UserAlreadyExistsException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class ControllerValidationMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpHeaders header = new HttpHeaders();

    @BeforeAll
    static void cleanDB() {
        try {
            Files.deleteIfExists(Path.of("./src/test/resources/quizDB.mv.db"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    public void setup() {
        header.setBasicAuth(WebQuizConfiguration.USER_COFINPRO, "secret");
    }

    @Test
    void registerUserValidRequest() throws Exception {
        UserRequestBody userRequest = new UserRequestBody("dummy@company.org", "password");
        mockMvc.perform(post("/api/register").headers(header)
                        .contentType(MediaType.APPLICATION_JSON).content(toJson(userRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void registerUserInvalidEmail() throws Exception {
        UserRequestBody userRequest = new UserRequestBody("dummycompany.org", "password");
        mockMvc.perform(post("/api/register").headers(header)
                        .contentType(MediaType.APPLICATION_JSON).content(toJson(userRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerUserInvalidDoublyRegistered() throws Exception {
        UserRequestBody userRequest = new UserRequestBody("sbdy@google.com", "verysecret");
        mockMvc.perform(post("/api/register").headers(header)
                        .contentType(MediaType.APPLICATION_JSON).content(toJson(userRequest)))
                .andExpect(status().isOk());
        MvcResult postResult = mockMvc.perform(post("/api/register").headers(header)
                        .contentType(MediaType.APPLICATION_JSON).content(toJson(userRequest)))
                .andExpect(status().isBadRequest()).andReturn();
        assertTrue(postResult.getResolvedException() instanceof UserAlreadyExistsException);
    }

    @Test
    void createQuizValidRequest() throws Exception {
        QuizRequestBody quizRequest = new QuizRequestBody("The quiz tile",
                "The text - Which are correct?",
                List.of("option 1", "option 2", "option 3"),  List.of(0,2));
        MockHttpServletResponse response = mockMvc.perform(post("/api/quizzes").headers(header)
                        .contentType(MediaType.APPLICATION_JSON).content(toJson(quizRequest)))
                .andExpect(status().isOk()).andReturn().getResponse();
        Matcher regexMatcher = Pattern.compile("id\\s*\"\\s*:\\s*([0-9]+)\\s*,").matcher(response.getContentAsString());
        assertTrue(regexMatcher.find());
        long newQuizId = Integer.parseInt(regexMatcher.group(1));
        assertEquals(toJson(QuizResponse.fromQuiz(quizRequest.toQuiz().setId(newQuizId))),
                response.getContentAsString());
    }

    @Test
    void createQuizInvalidRequest() throws Exception {
        QuizRequestBody quizRequest = new QuizRequestBody("The quiz tile",
                "The text - Which are correct?",
                List.of("option 1"),  List.of(0,2));
        mockMvc.perform(post("/api/quizzes").headers(header)
                        .contentType(MediaType.APPLICATION_JSON).content(toJson(quizRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void patchQuizValidRequest() throws Exception  {
        header.setBasicAuth(WebQuizConfiguration.ADMIN_COFINPRO, "topsecret");
        List<String> newOptions = List.of("new option 1", "new option 2", "new option 3");
        QuizPatchRequestBody patchRequest = new QuizPatchRequestBody("New Java Logo",
                "New Question?", newOptions,  List.of(2));
        mockMvc.perform(patch("/api/quizzes/0").headers(header)
                        .contentType(MediaType.APPLICATION_JSON).content(toJson(patchRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(toJson(
                        new QuizResponse(0, "New Java Logo", "New Question?", newOptions))));
    }

    @Test
    void patchQuizInvalidRequest() throws Exception {
        QuizRequestBody quizRequest = new QuizRequestBody("The quiz tile",
                "The text - Which are correct?",
                List.of("option 1"),  List.of(0,2));
        mockMvc.perform(patch("/api/quizzes/1").headers(header)
                        .contentType(MediaType.APPLICATION_JSON).content(toJson(quizRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getQuizInvalidWrongId() throws Exception {
        QuizRequestBody quizRequest = new QuizRequestBody("The quiz tile",
                "The text - Which are correct?",
                List.of("option 1", "option 2"),  List.of(0,2));
        MvcResult postResult = mockMvc.perform(get("/api/quizzes/11111").headers(header)
                        .contentType(MediaType.APPLICATION_JSON).content(toJson(quizRequest)))
                .andExpect(status().isNotFound()).andReturn();
        assertTrue(postResult.getResolvedException() instanceof QuizNotFoundException);
    }

    private String toJson(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }
}