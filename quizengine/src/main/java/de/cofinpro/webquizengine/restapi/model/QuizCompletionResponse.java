package de.cofinpro.webquizengine.restapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.cofinpro.webquizengine.persistence.QuizCompletion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO object representing a quiz completion response.
 * This object is returned by the GET "api/quizzes/completed" endpoint of the quiz controller.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class QuizCompletionResponse {

    @JsonProperty("id")
    private Long quizId;
    private LocalDateTime completedAt;

    public static QuizCompletionResponse fromQuizCompletion(QuizCompletion completion) {
        return new QuizCompletionResponse(completion.getQuiz().getId(), completion.getCompletedAt());
    }
}
