package de.cofinpro.webquizengine.restapi.model;

import de.cofinpro.webquizengine.persistence.Quiz;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO object representing a quiz response.
 * Different from Quiz objects, at this stage (before creation) no id has been assigned
 * and also different from GET requests the solution to the quiz has to be provided by client.
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
