package de.cofinpro.webquizengine.restapi.model;

import de.cofinpro.webquizengine.persistence.Quiz;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * DTO object representing a quiz creation request body.
 * Different from Quiz objects, at this stage (before creation) no id has been assigned
 * and also different from GET requests the solution to the quiz has to be provided by client.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class QuizRequestBody {

    @NotEmpty
    private String title;
    @NotEmpty
    private String text;
    @NotNull
    @Size(min=2)
    private List<String> options;
    private List<Integer> answer;

    /**
     * map the DTO to a new persistence layer entity object.
     * @return the entity quiz object
     */
    public Quiz toQuiz() {
        Quiz quiz = new Quiz();
        quiz.setTitle(this.title);
        quiz.setText(this.text);
        quiz.setOptions(this.options);
        quiz.setAnswer(this.answer);
        return quiz;
    }
}
