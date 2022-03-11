package de.cofinpro.webquizengine.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Arrays;

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
    private int[] correctOptions; // option indexes are sorted

    Quiz(int id, QuizRequestBody quizRequestBody) {
        this.id = id;
        this.title = quizRequestBody.getTitle();
        this.text = quizRequestBody.getText();
        this.options = quizRequestBody.getOptions();
        this.correctOptions = quizRequestBody.getAnswer();
        initCorrectOptions();
    }

    /**
     * since answer-field in RequestBody is optional (as specified), the  correctOptions
     * may get set to null, in which case, we want an empty array of options (meaning all answers are wrong).
     * Also, we sort the options at this point ascending for comparisons with answers.
     */
    private void initCorrectOptions() {
        if (correctOptions == null) {
            correctOptions = new int[] {};
        }
        Arrays.sort(correctOptions);
    }
}
