package de.cofinpro.webquizengine.persistence;

import de.cofinpro.webquizengine.restapi.model.QuizPatchRequestBody;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Persistence layer entity object representing a quiz.
 */
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Entity
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "quiz_generator")
    @SequenceGenerator(name = "quiz_generator", sequenceName = "quiz_sequence", initialValue = 0)
    private Long id;

    private String username;
    private String title;
    private String text;

    @ElementCollection
    private List<String> options;

    @ElementCollection
    private List<Integer> answer;

    @OneToMany(mappedBy = "quiz")
    private List<QuizCompletion> completions = new ArrayList<>();

    /**
     * apply a patch request received to this quiz
     * @param quizPatchRequest the patch request data to apply
     * @return reference to this Quiz object
     */
    public Quiz applyPatchRequest(QuizPatchRequestBody quizPatchRequest) {
        title = isNullOrBlank(quizPatchRequest.getTitle()) ? title : quizPatchRequest.getTitle();
        text = isNullOrBlank(quizPatchRequest.getText()) ? text : quizPatchRequest.getText();
        options = quizPatchRequest.getOptions() == null ? options : quizPatchRequest.getOptions();
        answer  = quizPatchRequest.getAnswer() == null ? answer : quizPatchRequest.getAnswer();
        return this;
    }

    /**
     * add a new completion to this quiz
     * @param quizCompletion the new completion to add to the quiz's completion list
     */
    public void addCompletion(QuizCompletion quizCompletion) {
       completions.add(quizCompletion);
    }

    private boolean isNullOrBlank(String string) {
        return string == null || "".equals(string.trim());
    }
}
