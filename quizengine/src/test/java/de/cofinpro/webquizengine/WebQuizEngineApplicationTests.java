package de.cofinpro.webquizengine;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.cofinpro.webquizengine.configuration.WebQuizConfiguration;
import de.cofinpro.webquizengine.restapi.model.QuizPatchRequestBody;
import de.cofinpro.webquizengine.restapi.model.QuizRequestBody;
import de.cofinpro.webquizengine.restapi.model.QuizSolveResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class WebQuizEngineApplicationTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;
    private final HttpHeaders header = new HttpHeaders();
    private final ObjectMapper objectMapper = new ObjectMapper();

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
        header.setBasicAuth(WebQuizConfiguration.ADMIN_COFINPRO, WebQuizConfiguration.ADMIN_PASSWORD);
    }

    @Test
    void servletContextProvidesController() {
        ServletContext servletContext = webApplicationContext.getServletContext();

        assertNotNull(servletContext);
        assertTrue(servletContext instanceof MockServletContext);
        assertNotNull(webApplicationContext.getBean("webQuizController"));
    }

    @Test
    void getApiQuiz() throws Exception {
        mockMvc.perform(get("/api/quiz").headers(header))
                .andExpect(content().json("{\"title\":\"The Java Logo\"," +
                        "\"text\":\"What is depicted on the Java logo?\"," +
                        "\"options\":[\"Robot\",\"Tea leaf\",\"Cup of coffee\",\"Bug\"]}"));
    }

    @Test
    void postApiQuizSolveSuccess() throws Exception {
        String postBody = "{\"answer\": [2]}";
        mockMvc.perform(post("/api/quizzes/0/solve").headers(header).content(postBody)
                        .contentType("application/json;charset=UTF-8"))
                .andExpect(content().json(toJson(QuizSolveResponse.correct())));
    }

    @Test
    void postApiQuizSolveFailure() throws Exception {
        String postBody = "{\"answer\": [0,1]}";
        mockMvc.perform(post("/api/quizzes/0/solve").headers(header).content(postBody)
                        .contentType("application/json;charset=UTF-8"))
                .andExpect(content().json(toJson(QuizSolveResponse.incorrect())));
    }

    @ParameterizedTest
    @ValueSource(strings = {"{\"answer\": \"\"}", "{\"answer\": null}", "{}"})
    void postApiQuizSolveInvalid(String postBody) throws Exception {
        mockMvc.perform(post("/api/quizzes/0/solve").headers(header).content(postBody)
                        .contentType("application/json;charset=UTF-8"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET api/quizzes & create load tested")
    void postApiCreateQuizConcurrentLoadTest() throws Exception {
        final int numberOfThreads = 4;
        final int createsPerThread = 25;
        Thread[] clients = new Thread[numberOfThreads];
        Arrays.setAll(clients, i -> new QuizCreateRequesterThread(mockMvc, createsPerThread, header));
        Arrays.stream(clients).forEach(Thread::start);
        for (Thread client: clients) {
            client.join();
        }

        int pages = numberOfThreads * createsPerThread / WebQuizConfiguration.QUIZ_PAGE_SIZE;
        ResultHandler resultHandler = new CreateLoadTestResultHandler(WebQuizConfiguration.QUIZ_PAGE_SIZE + 1);
        mockMvc.perform(get("/api/quizzes?page=%d".formatted(pages - 1)).headers(header)).andDo(resultHandler);

        resultHandler = new CreateLoadTestResultHandler(1);
        mockMvc.perform(get("/api/quizzes?page=%d".formatted(pages + 1)).headers(header)).andDo(resultHandler);
        // to satisfy Sonar - there are "real asserts" in the resultHandler
        assertNotNull(resultHandler);
    }

    @Test
    void deleteQuizValid() throws Exception {
        QuizRequestBody quizRequest =
                new QuizRequestBody("quiz title", "the question", List.of("opt1","opt2"), List.of());

        MockHttpServletResponse newQuizResponse = mockMvc.perform(post("/api/quizzes")
                        .content(toJson(quizRequest)).headers(header).contentType("application/json;charset=UTF-8"))
                .andExpect(content().string(containsString("the question")))
                .andReturn().getResponse();

        mockMvc.perform(delete("/api/quizzes/%d".formatted(quizIdFromResponse(newQuizResponse)))
                        .headers(header)).andExpect(status().is(204));
    }

    @Test
    void deleteQuizInvalidNotOwner() throws Exception {
        QuizRequestBody quizRequest = new QuizRequestBody("quiz title", "the question", List.of("opt1","opt2"), List.of());

        MockHttpServletResponse newQuizResponse = mockMvc.perform(post("/api/quizzes")
                        .content(toJson(quizRequest)).headers(header).contentType("application/json;charset=UTF-8"))
                .andExpect(content().string(containsString("the question")))
                .andReturn().getResponse();

        header.setBasicAuth(WebQuizConfiguration.USER_COFINPRO, WebQuizConfiguration.USER_PASSWORD);
        mockMvc.perform(delete("/api/quizzes/%d".formatted(quizIdFromResponse(newQuizResponse)))
                        .headers(header)).andExpect(status().is(403));
    }

    // valid patchQuiz and invalid request format tests already done in restapi.controller package !
    @Test
    void patchQuizInvalidNotOwner() throws Exception {
        QuizPatchRequestBody patchRequest =
                new QuizPatchRequestBody("new java quiz",  "question", List.of("opt1","opt2"), List.of());

        header.setBasicAuth(WebQuizConfiguration.USER_COFINPRO, WebQuizConfiguration.USER_PASSWORD);
        mockMvc.perform(patch("/api/quizzes/0")
                        .content(toJson(patchRequest)).headers(header).contentType("application/json;charset=UTF-8"))
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
