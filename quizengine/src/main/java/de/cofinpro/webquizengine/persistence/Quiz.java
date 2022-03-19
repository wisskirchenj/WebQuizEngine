package de.cofinpro.webquizengine.persistence;

import de.cofinpro.webquizengine.restapi.model.QuizPatchRequestBody;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
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
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    private String username;
    private String title;
    private String text;

    @ElementCollection
    private List<String> options;

    @ElementCollection
    private List<Integer> answer;

    public Quiz applyPatchRequest(QuizPatchRequestBody quizPatchRequest) {
        title = isNullOrBlank(quizPatchRequest.getTitle()) ? title : quizPatchRequest.getTitle();
        text = isNullOrBlank(quizPatchRequest.getText()) ? text : quizPatchRequest.getText();
        options = quizPatchRequest.getOptions() == null ? options : quizPatchRequest.getOptions();
        answer  = quizPatchRequest.getAnswer() == null ? answer : quizPatchRequest.getAnswer();
        return this;
    }

    private boolean isNullOrBlank(String string) {
        return string == null || "".equals(string.trim());
    }
}
