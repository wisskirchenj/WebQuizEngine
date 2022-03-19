package de.cofinpro.webquizengine;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class WebQuizEngineApplicationTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;
    private final boolean doLoadTest = true;
    private HttpHeaders header;

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
        header = new HttpHeaders();
        header.setBasicAuth("admin@cofinpro.de", "topsecret");
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
        mockMvc.perform(post("/api/quizzes/1/solve").headers(header).content(postBody)
                        .contentType("application/json;charset=UTF-8"))
                .andExpect(content().json("{\"success\":true,\"feedback\":\"Cooooooorrect, oider!\"}"));
    }

    @Test
    void postApiQuizSolveFailure() throws Exception {
        String postBody = "{\"answer\": [0,1]}";
        mockMvc.perform(post("/api/quizzes/1/solve").headers(header).content(postBody)
                        .contentType("application/json;charset=UTF-8"))
                .andExpect(content().json("{\"success\":false,\"feedback\":\"Nöööööö !\"}"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"{\"answer\": \"\"}", "{\"answer\": null}", "{}"})
    void postApiQuizSolveInvalid(String postBody) throws Exception {
        mockMvc.perform(post("/api/quizzes/1/solve").headers(header).content(postBody)
                        .contentType("application/json;charset=UTF-8"))
                .andExpect(status().isBadRequest());
    }

    @DisabledIfEnvironmentVariable(named = "doLoadTest", matches = "false")
    void postApiCreateQuiz() throws Exception {
        if (doLoadTest) return;
        String postBody = String.format("{\"title\": \"Title%1$04d\"," +
                "\"text\": \"Text%1$04d\",\"options\": [\"0\",\"1\",\"2\"], \"solution\":%1$d}", 1);
        mockMvc.perform(post("/api/quizzes").content(postBody).headers(header).contentType("application/json;charset=UTF-8"))
                .andDo(print()).andExpect(content()
                        .json("{\"title\":\"Title0001\",\"text\":\"Text0001\",\"options\":[\"0\",\"1\",\"2\"]}"));
    }

    @Test
    void postApiCreateQuizConcurrentLoadTest() throws Exception {
        if (!doLoadTest) return;
        final int numberOfThreads = 4;
        final int createsPerThread = 20;
        Thread[] clients = new Thread[numberOfThreads];
        Arrays.setAll(clients, i -> new QuizCreateRequesterThread(mockMvc, createsPerThread, header));
        Arrays.stream(clients).forEach(Thread::start);
        for (Thread client: clients) {
            client.join();
        }

        ResultHandler resultHandler = new CreateLoadTestResultHandler(numberOfThreads * createsPerThread);
        mockMvc.perform(get("/api/quizzes").headers(header)).andDo(resultHandler);
        // to satisfy Sonar - there are "real asserts" in the resultHandler
        assertNotNull(resultHandler);
    }
}
