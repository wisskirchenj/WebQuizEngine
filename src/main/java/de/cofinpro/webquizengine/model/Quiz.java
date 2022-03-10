package de.cofinpro.webquizengine.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * DTO object representing a quiz.
 * The solution is not displayed to clients who attempt to solve this quiz,
 * therefore it is marked @JsonIgnore.
 * Instances of quizzes are only created by the QuizGenerator.
 */
@NoArgsConstructor
@Getter
@Setter
public class Quiz {

    private int id;
    private String title;
    private String text;
    private String[] options;
    @JsonIgnore
    private int solution;

    Quiz(int id, QuizRequestBody quizRequestBody) {
        this.id = id;
        this.title = quizRequestBody.getTitle();
        this.text = quizRequestBody.getText();
        this.options = quizRequestBody.getOptions();
        this.solution = quizRequestBody.getAnswer();
    }
}
