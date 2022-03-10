package de.cofinpro.webquizengine.model;

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

    private boolean success;
    private String feedback;
}
