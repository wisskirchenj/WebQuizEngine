package de.cofinpro.webquizengine;

import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class QuizCreateRequesterThread extends Thread {

    private final MockMvc mockMvc;
    private final int numberOfCreates;
    private final HttpHeaders header;

    QuizCreateRequesterThread(MockMvc mockMvc, int numberOfCreates, HttpHeaders header) {
        this.numberOfCreates = numberOfCreates;
        this.mockMvc = mockMvc;
        this.header = header;
    }

    @Override
    public void run() {
        String postBody;
        try {
            for (int i = 0; i < numberOfCreates; i++) {
                postBody = String.format("{\"title\": \"Title%1$04d\"," +
                        "\"text\": \"Text%1$04d\",\"options\": [\"o\",\"p\",\"q\"], \"answer\":[%2$d]}", i, this.getId() % 3);
                mockMvc.perform(post("/api/quizzes").headers(header).content(postBody).contentType("application/json"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
