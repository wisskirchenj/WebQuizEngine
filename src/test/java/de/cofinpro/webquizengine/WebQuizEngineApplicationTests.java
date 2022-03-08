package de.cofinpro.webquizengine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@ContextConfiguration
@SpringBootTest
class WebQuizEngineApplicationTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
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
        this.mockMvc.perform(post("/api/quiz?answer=2")).andDo(print())
                .andExpect(content().json("{\"success\":true,\"feedback\":\"Cooooooorrect, oider!\"}"));
    }

    @Test
    public void getApiQuizFailures() throws Exception {
        this.mockMvc.perform(post("/api/quiz?answer=1")).andDo(print())
                .andExpect(content().json("{\"success\":false,\"feedback\":\"Nöööööö !\"}"));
    }
}
