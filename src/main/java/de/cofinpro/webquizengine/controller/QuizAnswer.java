package de.cofinpro.webquizengine.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO object representing a quiz answer as returned on a client's solution attempt.
 * Instances are created in the QuizService.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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
