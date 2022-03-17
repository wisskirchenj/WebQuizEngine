package de.cofinpro.webquizengine.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
class WebQuizEngineSecurityTests {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        //this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    void getApiQuizzesUnauthenticated() throws Exception {
        mockMvc.perform(get("/api/quizzes")).andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    void getApiQuizzesAuthenticated() throws Exception {
        HttpHeaders header = new HttpHeaders();
        header.setBasicAuth("juergen", "secret");
        mockMvc.perform(get("/api/quizzes").headers(header)).andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()));
    }

    @Test
    void registerHasPublicAccess() throws Exception {
        this.mockMvc.perform(get("/register.html")).andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()));
    }

    @Test
    void adminUnauthenticated() throws Exception {
        this.mockMvc.perform(get("/admin.html")).andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    void adminUnauthorized() throws Exception {
        HttpHeaders header = new HttpHeaders();
        header.setBasicAuth("juergen", "secret");
        this.mockMvc.perform(get("/admin.html").headers(header)).andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    void adminAuthorized() throws Exception {
        HttpHeaders header = new HttpHeaders();
        header.setBasicAuth("jonas", "topsecret");
        this.mockMvc.perform(get("/admin.html").headers(header)).andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()));
    }


    @Test
    void shutdownForEverybody() throws Exception {
        this.mockMvc.perform(post("/actuator/shutdown")).andDo(print())
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()));
    }
}
