package de.cofinpro.webquizengine.restapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.cofinpro.webquizengine.restapi.model.QuizPatchRequestBody;
import de.cofinpro.webquizengine.restapi.model.QuizRequestBody;
import de.cofinpro.webquizengine.restapi.model.QuizResponse;
import de.cofinpro.webquizengine.restapi.service.QuizNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = { "spring.datasource.url=jdbc:postgresql://localhost:5432/quiztest"})
@AutoConfigureMockMvc
@WithMockUser
class ControllerValidationMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    JwtDecoder jwtDecoder;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void createQuizValidRequest() throws Exception {
        QuizRequestBody quizRequest = new QuizRequestBody("The quiz tile",
                "The text - Which are correct?",
                List.of("option 1", "option 2", "option 3"),  List.of(0,2));
        MockHttpServletResponse response = mockMvc.perform(post("/api/quiz").with(jwt())
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
        mockMvc.perform(post("/api/quiz").with(jwt())
                        .contentType(MediaType.APPLICATION_JSON).content(toJson(quizRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void patchQuizValidRequest() throws Exception  {
        List<String> newOptions = List.of("new option 1", "new option 2", "new option 3");
        QuizPatchRequestBody patchRequest = new QuizPatchRequestBody("New Java Logo",
                "New Question?", newOptions,  List.of(2));
        mockMvc.perform(patch("/api/quiz/0").with(jwt())
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
        mockMvc.perform(patch("/api/quiz/1").with(jwt())
                        .contentType(MediaType.APPLICATION_JSON).content(toJson(quizRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getQuizInvalidWrongId() throws Exception {
        QuizRequestBody quizRequest = new QuizRequestBody("The quiz tile",
                "The text - Which are correct?",
                List.of("option 1", "option 2"),  List.of(0,2));
        MvcResult postResult = mockMvc.perform(get("/api/quiz/11111")
                        .contentType(MediaType.APPLICATION_JSON).content(toJson(quizRequest)))
                .andExpect(status().isNotFound()).andReturn();
        assertTrue(postResult.getResolvedException() instanceof QuizNotFoundException);
    }

    private String toJson(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }
}