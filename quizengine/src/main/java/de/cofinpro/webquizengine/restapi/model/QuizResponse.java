package de.cofinpro.webquizengine.restapi.model;

import de.cofinpro.webquizengine.persistence.Quiz;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO object representing a quiz response.
 * This object is returned by all GET endpoints of the quiz controller, that provide quiz data.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class QuizResponse {

    private long id;
    private String title;
    private String text;
    private List<String> options;

    public static QuizResponse fromQuiz(Quiz quiz) {
        return new QuizResponse(quiz.getId(), quiz.getTitle(), quiz.getText(), quiz.getOptions());
    }
}
