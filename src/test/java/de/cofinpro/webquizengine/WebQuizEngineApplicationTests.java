package de.cofinpro.webquizengine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

//@ContextConfiguration
@SpringBootTest
@AutoConfigureMockMvc
class WebQuizEngineApplicationTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;
    private final boolean doLoadTest = true;

    @BeforeEach
    public void setup() {
        //this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    void contextLoads() {
    }

    @Test
    public void servletContextProvidesController() {
        ServletContext servletContext = webApplicationContext.getServletContext();

        assertNotNull(servletContext);
        assertTrue(servletContext instanceof MockServletContext);
        assertNotNull(webApplicationContext.getBean("webQuizController"));
    }

    @Test
    public void getApiQuiz() throws Exception {
        this.mockMvc.perform(get("/api/quiz")).andDo(print())
                .andExpect(content().json("{\"title\":\"The Java Logo\"," +
                        "\"text\":\"What is depicted on the Java logo?\"," +
                        "\"options\":[\"Robot\",\"Tea leaf\",\"Cup of coffee\",\"Bug\"]}"));
    }

    @Test
    public void postApiQuizSuccess() throws Exception {
        String postBody = "{\"answer\": [2]}";
        this.mockMvc.perform(post("/api/quizzes/0/solve").content(postBody)
                        .contentType("application/json;charset=UTF-8"))
                .andExpect(content().json("{\"success\":true,\"feedback\":\"Cooooooorrect, oider!\"}"));
    }

    @Test
    public void getApiQuizFailures() throws Exception {
        String postBody = "{\"answer\": []}";
        this.mockMvc.perform(post("/api/quizzes/0/solve").content(postBody)
                        .contentType("application/json;charset=UTF-8"))
                .andExpect(content().json("{\"success\":false,\"feedback\":\"Nöööööö !\"}"));
    }

    @Test
    public void postApiCreateQuiz() throws Exception {
        if (doLoadTest) return;
        String postBody = String.format("{\"title\": \"Title%1$04d\"," +
                "\"text\": \"Text%1$04d\",\"options\": [\"0\",\"1\",\"2\"], \"solution\":%1$d}", 1);
        mockMvc.perform(post("/api/quizzes").content(postBody).contentType("application/json;charset=UTF-8"))
                .andDo(print()).andExpect(content()
                        .json("{\"id\":1,\"title\":\"Title0001\",\"text\":\"Text0001\",\"options\":[\"0\",\"1\",\"2\"]}"));
    }

    @Test
    public void postApiCreateQuizConcurrentLoadTest() throws Exception {
        if (!doLoadTest) return;
        final int numberOfThreads = 4;
        final int createsPerThread = 200;
        Thread[] clients = new Thread[numberOfThreads];
        Arrays.setAll(clients, i -> new QuizCreateRequesterThread(mockMvc, createsPerThread));
        Arrays.stream(clients).forEach(Thread::start);
        for (Thread client: clients) {
            client.join();
        }

        ResultHandler resultHandler = new CreateLoadTestResultHandler(numberOfThreads * createsPerThread);
        this.mockMvc.perform(get("/api/quizzes")).andDo(resultHandler);
    }
}
