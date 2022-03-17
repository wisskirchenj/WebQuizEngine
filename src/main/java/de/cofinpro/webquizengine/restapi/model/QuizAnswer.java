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
public class QuizAnswer {

    public static QuizAnswer correct() {
        return new QuizAnswer(true, "Cooooooorrect, oider!");
    }

    public static QuizAnswer incorrect() {
        return new QuizAnswer(false, "Nöööööö !");
    }

    private boolean success;
    private String feedback;
}
