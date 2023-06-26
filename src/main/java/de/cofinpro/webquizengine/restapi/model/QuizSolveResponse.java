package de.cofinpro.webquizengine.restapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO object representing a quiz answer as returned on a client's solution attempt.
 * Instances are created in the QuizService.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class QuizSolveResponse {

    public static QuizSolveResponse correct() {
        return new QuizSolveResponse(true, "Cooooooorrect, oider!");
    }

    public static QuizSolveResponse incorrect() {
        return new QuizSolveResponse(false, "Nöööööö !");
    }

    private boolean success;
    private String feedback;
}
