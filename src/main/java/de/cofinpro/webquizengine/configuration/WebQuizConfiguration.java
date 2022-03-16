package de.cofinpro.webquizengine.configuration;

import de.cofinpro.webquizengine.persistence.Quiz;
import de.cofinpro.webquizengine.persistence.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

/**
 * Configuration class to declare the Beans used (besides Components and Services)
 */
@Configuration
public class WebQuizConfiguration {

    private final QuizRepository quizRepository;

    @Autowired
    public WebQuizConfiguration(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    /**
     * the Java Quiz is created and injected on server boot and can be displayed and solved
     * at the start of any session.
     * @return the standard java quiz injected as start quiz
     */
    @Bean
    public Quiz javaQuiz() {
        Quiz javaQuiz = new Quiz();
        javaQuiz.setTitle(Quiz.JAVA_QUIZ_TITLE);
        javaQuiz.setText(Quiz.JAVA_QUIZ_TEXT);
        javaQuiz.setOptions(Quiz.JAVA_QUIZ_OPTIONS);
        javaQuiz.setAnswer(List.of(2));
        return quizRepository.save(javaQuiz);
    }

    @Bean
    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }
}
