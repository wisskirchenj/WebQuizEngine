package de.cofinpro.webquizengine;

import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateLoadTestResultHandler implements ResultHandler {

    private final int numberOfPosts;

    public CreateLoadTestResultHandler(int numberOfPosts) {
        this.numberOfPosts = numberOfPosts;
    }

    @Override
    public void handle(MvcResult result) throws Exception {
        String contentJson = result.getResponse().getContentAsString();
        System.out.println( contentJson.substring(contentJson.lastIndexOf("\"id\":")));
        assertTrue(contentJson.contains("\"id\":" + numberOfPosts));
        assertFalse(contentJson.contains("\"id\":" + (numberOfPosts + 1)));
    }
}
