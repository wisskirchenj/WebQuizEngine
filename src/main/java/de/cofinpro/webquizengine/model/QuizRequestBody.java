package de.cofinpro.webquizengine.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO object representing a quiz creation request body.
 * Different from Quiz objects, at this stage (before creation) no id has been assigned
 * and also different from GET requests the solution to the quiz has to be provided by client.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class QuizRequestBody {

    private String title;
    private String text;
    private String[] options;
    private int answer;
}
