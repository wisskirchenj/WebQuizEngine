package de.cofinpro.webquizengine;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.cofinpro.webquizengine.restapi.model.QuizPatchRequestBody;
import de.cofinpro.webquizengine.restapi.model.QuizRequestBody;
import de.cofinpro.webquizengine.restapi.model.QuizSolveResponse;
import de.cofinpro.webquizengine.restapi.service.QuizService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
class WebQuizEngineApplicationTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    JwtDecoder jwtDecoder;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void servletContextProvidesController() {
        assertNotNull(webApplicationContext.getBean("webQuizController"));
    }

    @Test
    void postApiQuizSolveSuccess() throws Exception {
        String postBody = "{\"answer\": [2]}";
        mockMvc.perform(post("/api/quiz/1000/solve").with(jwt()).content(postBody)
                        .contentType("application/json;charset=UTF-8"))
                .andExpect(content().json(toJson(QuizSolveResponse.correct())));
    }

    @Test
    void postApiQuizSolveFailure() throws Exception {
        String postBody = "{\"answer\": [0,1]}";
        mockMvc.perform(post("/api/quiz/1000/solve").with(jwt()).content(postBody)
                        .contentType("application/json;charset=UTF-8"))
                .andExpect(content().json(toJson(QuizSolveResponse.incorrect())));
    }

    @ParameterizedTest
    @ValueSource(strings = {"{\"answer\": \"\"}", "{\"answer\": null}", "{}"})
    void postApiQuizSolveInvalid(String postBody) throws Exception {
        mockMvc.perform(post("/api/quiz/1000/solve").with(jwt()).content(postBody)
                        .contentType("application/json;charset=UTF-8"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET api/quiz & create load tested")
    void postApiCreateQuizConcurrentLoadTest() throws Exception {
        final int numberOfThreads = 4;
        final int createsPerThread = 25;
        Thread[] clients = new Thread[numberOfThreads];
        Arrays.setAll(clients, i -> new QuizCreateRequesterThread(mockMvc, createsPerThread));
        Arrays.stream(clients).forEach(Thread::start);
        for (Thread client: clients) {
            client.join();
        }

        int pages = numberOfThreads * createsPerThread / QuizService.QUIZ_PAGE_SIZE;
        ResultHandler resultHandler = new CreateLoadTestResultHandler(QuizService.QUIZ_PAGE_SIZE + 1);
        mockMvc.perform(get("/api/quiz?page=%d".formatted(pages - 1)).with(jwt())).andDo(resultHandler);

        resultHandler = new CreateLoadTestResultHandler(1);
        mockMvc.perform(get("/api/quiz?page=%d".formatted(pages + 1)).with(jwt())).andDo(resultHandler);
        // to satisfy Sonar - there are "real asserts" in the resultHandler
        assertNotNull(resultHandler);
    }

    @Test
    void deleteQuizValid() throws Exception {
        QuizRequestBody quizRequest =
                new QuizRequestBody("quiz title", "the question", List.of("opt1","opt2"), List.of());

        MockHttpServletResponse newQuizResponse = mockMvc.perform(post("/api/quiz")
                        .content(toJson(quizRequest)).with(jwt()).contentType("application/json;charset=UTF-8"))
                .andExpect(content().string(containsString("the question")))
                .andReturn().getResponse();

        mockMvc.perform(delete("/api/quiz/%d".formatted(quizIdFromResponse(newQuizResponse)))
                        .with(jwt())).andExpect(status().is(204));
    }

    @Test
    void deleteQuizInvalidNotOwner() throws Exception {
        mockMvc.perform(delete("/api/quiz/1000")
                        .with(jwt())).andExpect(status().is(403));
    }

    // valid patchQuiz and invalid request format tests already done in restapi.controller package !
    @Test
    void patchQuizInvalidNotOwner() throws Exception {
        QuizPatchRequestBody patchRequest =
                new QuizPatchRequestBody("new java quiz",  "question", List.of("opt1","opt2"), List.of());

        mockMvc.perform(patch("/api/quiz/1000")
                        .content(toJson(patchRequest)).with(jwt()).contentType("application/json;charset=UTF-8"))
                .andExpect(status().is(403));
    }

    private String toJson(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

    private long quizIdFromResponse(MockHttpServletResponse response) throws Exception {
        Matcher regexMatcher = Pattern.compile("id\\s*\"\\s*:\\s*([0-9]+)\\s*,")
                .matcher(response.getContentAsString());
        assertTrue(regexMatcher.find());
        return Integer.parseInt(regexMatcher.group(1));
    }
}
