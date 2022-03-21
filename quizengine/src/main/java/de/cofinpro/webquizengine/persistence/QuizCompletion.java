package de.cofinpro.webquizengine.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Persistence layer entity object representing a quiz completion.
 * The solution is not displayed to clients who attempt to solve this quiz,
 * therefore the getter is marked @JsonIgnore.
 */
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Entity
public class QuizCompletion {

    @JsonIgnore //TODO nur für Hyperskill
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "completion_id")
    private Long completionId;

    @JsonIgnore //TODO nur für Hyperskill
    @Column(name = "username")
    private String completedByUsername;

    @ManyToOne
    @JoinColumn(name = "id")
    private Quiz quiz;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    public QuizCompletion(Quiz quiz, String username) {
        this.quiz = quiz;
        this.completedByUsername = username;
        this.completedAt = LocalDateTime.now();
    }
}
