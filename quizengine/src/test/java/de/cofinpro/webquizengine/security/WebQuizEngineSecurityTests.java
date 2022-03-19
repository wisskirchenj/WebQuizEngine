package de.cofinpro.webquizengine.security;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
class WebQuizEngineSecurityTests {

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    static void cleanDB() {
        try {
            Files.deleteIfExists(Path.of("./quizengine/src/test/resources/quizDB.mv.db"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getApiQuizzesUnauthenticated() throws Exception {
        mockMvc.perform(get("/api/quizzes")).andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    void getApiQuizzesAuthenticated() throws Exception {
        HttpHeaders header = new HttpHeaders();
        header.setBasicAuth("admin@cofinpro.de", "topsecret");
        mockMvc.perform(get("/api/quizzes").headers(header)).andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()));
    }

    @Test
    void registerHasPublicAccess() throws Exception {
        String postBody = "{\"email\": \"abc@def.gh\", \"password\": \"secre\"}";
        mockMvc.perform(post("/api/register").content(postBody)
                .contentType("application/json;charset=UTF-8")).andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()));
    }

    @Test
    void adminUnauthenticated() throws Exception {
        mockMvc.perform(get("/admin.html")).andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    void adminUnauthorized() throws Exception {
        HttpHeaders header = new HttpHeaders();
        header.setBasicAuth("user@cofinpro.de", "secret");
        mockMvc.perform(get("/admin.html").headers(header)).andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    void adminAuthorized() throws Exception {
        HttpHeaders header = new HttpHeaders();
        header.setBasicAuth("admin@cofinpro.de", "topsecret");
        mockMvc.perform(get("/admin.html").headers(header)).andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()));
    }

    //@Test
    void shutdownForEverybody() throws Exception {
        mockMvc.perform(post("/actuator/shutdown")).andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()));
    }
}
