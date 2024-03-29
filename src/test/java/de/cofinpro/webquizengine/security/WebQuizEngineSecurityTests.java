package de.cofinpro.webquizengine.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class WebQuizEngineSecurityTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getApiQuizzesUnauthenticated() throws Exception {
        mockMvc.perform(get("/api/quiz")).andDo(print())
                .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    @WithMockUser
    void getApiQuizzesAuthenticated() throws Exception {
        mockMvc.perform(get("/api/quiz")).andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()));
    }
}
