package de.cofinpro.webquizengine;

import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateLoadTestResultHandler implements ResultHandler {

    private final int expectedTokens;

    public CreateLoadTestResultHandler(int expectedTokens) {
        this.expectedTokens = expectedTokens;
    }

    /**
     * takes the response as Json-string and splits it in tokens with the (for tests unique) options
     * value, that all threads use for their quizzes. This splits the string in one token more, than
     * quizzes generated by the load test.
     * @param result the response to GET api/quizzes as MvcResult
     * @throws Exception on getContentAsString()
     */
    @Override
    public void handle(MvcResult result) throws Exception {
        String[] tokens = result.getResponse().getContentAsString().split("\\[\"o\",\"p\",\"q\"]");
        assertEquals(expectedTokens, tokens.length);
    }
}
