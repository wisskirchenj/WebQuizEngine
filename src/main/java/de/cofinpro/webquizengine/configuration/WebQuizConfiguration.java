package de.cofinpro.webquizengine.configuration;

import de.cofinpro.webquizengine.persistence.Quiz;
import de.cofinpro.webquizengine.persistence.QuizRepository;
import de.cofinpro.webquizengine.persistence.RegisteredUser;
import de.cofinpro.webquizengine.persistence.RegisteredUserRepository;
import de.cofinpro.webquizengine.restapi.model.QuizResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration class to declare the Beans used (besides Components and Services)
 */
@Configuration
public class WebQuizConfiguration {

    public static final String ADMIN_COFINPRO = "admin@cofinpro.de";
    public static final String ADMIN_PASSWORD = "$2a$10$xcPv1H0w83PpW1k5u27LweY0gO/sG.4ggkwUzC4V7rTuiWQTZtGMa";
    public static final String USER_COFINPRO = "user@cofinpro.de";
    public static final String USER_PASSWORD = "$2a$10$tDkP5RqxOso/2baWqAEi7uh79OY.MuXiychlMOxA9jJf4r4z27w1e";

    public static final String JAVA_QUIZ_TITLE = "The Java Logo";
    public static final int QUIZ_PAGE_SIZE = 5;
    private static final String JAVA_QUIZ_TEXT = "What is depicted on the Java logo?";
    private static final List<String> JAVA_QUIZ_OPTIONS = List.of(
            "Robot", "Tea leaf", "Cup of coffee", "Bug");

    private final QuizRepository quizRepository;
    private final RegisteredUserRepository userRepository;

    @Autowired
    public WebQuizConfiguration(QuizRepository quizRepository, RegisteredUserRepository userRepository) {
        this.quizRepository = quizRepository;
        this.userRepository = userRepository;
    }

    /**
     * the admin user is created and stored in the repo if it does not already exist.
     * @return the admin's RegisteredUser object
     */
    @Bean
    public RegisteredUser getAdmin() {
        return userRepository.findByUsername(ADMIN_COFINPRO)
                .orElseGet(() -> userRepository.save(new RegisteredUser().setUsername(ADMIN_COFINPRO)
                .setEncryptedPassword(ADMIN_PASSWORD)));
    }

    /**
     * the admin user is created and stored in the repo if it does not already exist.
     * @return the admin's RegisteredUser object
     */
    @Bean
    public RegisteredUser getUser() {
        return userRepository.findByUsername(USER_COFINPRO)
                .orElseGet(() -> userRepository.save(new RegisteredUser().setUsername(USER_COFINPRO)
                        .setEncryptedPassword(ADMIN_PASSWORD)));
    }

    /**
     * the Java Quiz is created, stored in the Repository if it does not exist already
     * It is then available for injection on server boot.
     * Thus, it can be displayed and solved at the start of any session.
     * @return the standard java quiz injected as start quiz
     */
    @Bean
    public QuizResponse getJavaQuiz() {

        return QuizResponse.fromQuiz(quizRepository.findByTitle(JAVA_QUIZ_TITLE)
                .orElseGet(() -> quizRepository.save(new Quiz().setTitle(JAVA_QUIZ_TITLE).setText(JAVA_QUIZ_TEXT)
                        .setOptions(JAVA_QUIZ_OPTIONS).setAnswer(List.of(2)).setUsername(getAdmin().getUsername()))));
    }
}
