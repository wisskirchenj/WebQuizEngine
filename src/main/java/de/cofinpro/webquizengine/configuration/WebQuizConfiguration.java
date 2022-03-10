package de.cofinpro.webquizengine.configuration;

import de.cofinpro.webquizengine.model.JavaQuiz;
import de.cofinpro.webquizengine.model.QuizGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class to declare the Beans used (besides Components and Services)
 */
@Configuration
public class WebQuizConfiguration {

    @Autowired
    private QuizGenerator quizGenerator;

    /**
     * the Java Quiz is created and injected on server boot and can be displayed and solved
     * at the start of any session.
     * @return the Singleton JavaQuiz object
     */
    @Bean
    public JavaQuiz javaQuiz() {
        return quizGenerator.createJavaQuiz();
    }
}
