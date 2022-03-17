package de.cofinpro.webquizengine.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Persistence layer entity object representing a quiz.
 * The solution is not displayed to clients who attempt to solve this quiz,
 * therefore the getter is marked @JsonIgnore.
 */
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Quiz {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    @NotEmpty
    private String title;
    @NotEmpty
    private String text;
    @ElementCollection
    @Size(min=2)
    private List<String> options;
    @ElementCollection
    private List<Integer> answer;

    @JsonIgnore
    public List<Integer> getAnswer() {
        return answer;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public void setAnswer(List<Integer> answer) {
        this.answer = answer;
    }

    public static final String JAVA_QUIZ_TITLE = "The Java Logo";
    public static final String JAVA_QUIZ_TEXT = "What is depicted on the Java logo?";
    public static final List<String> JAVA_QUIZ_OPTIONS = List.of(
            "Robot", "Tea leaf", "Cup of coffee", "Bug");
}
