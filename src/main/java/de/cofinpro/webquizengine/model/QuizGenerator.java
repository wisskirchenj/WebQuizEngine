package de.cofinpro.webquizengine.model;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Quiz-Factory class, that is the single entry point to create new Quiz objects.
 */
@Component
public class QuizGenerator {
    private int quizCounter = 0;
    private final Map<Integer, Quiz> quizzes = new ConcurrentHashMap<>();

    /**
     * create the JavaQuiz. Method is called on bean injection as declared in the configuration
     * class. Thus, the JavaQuiz gets id=0.
     * @return the JavaQuiz object - singleton class.
     */
    public JavaQuiz createJavaQuiz() {
        JavaQuiz javaQuiz = new JavaQuiz(quizCounter);
        quizzes.put(quizCounter++, javaQuiz);
        return javaQuiz;
    }

    /**
     * model changing method call, which is invoked by a POST-request. The
     * QuizGenerator creates the quiz from the data given in the QuizRequestBody.
     * As id the next generator internal counter value is used. Since
     * counter increment and put to Hashmap are non-atomic, synchronization is needed.
     * @param quizRequestBody creation data provided
     * @return the quiz object created.
     */
    public synchronized Quiz createQuiz(QuizRequestBody quizRequestBody) {
        Quiz quiz = new Quiz(quizCounter, quizRequestBody);
        quizzes.put(quizCounter++, quiz);
        return quiz;
    }

    /**
     * return the queried quiz
     * @param id the query parameter id
     * @return the JavaQuiz object queried by id - or null if not there
     */
    public Quiz findQuizById(int id) {
        return quizzes.get(id);
    }

    /**
     * returns data to all quizzes generated.
     * @return all quizzes as Quiz[]
     */
    public Quiz[] getQuizzes() {
        return quizzes.values().toArray(new Quiz[0]);
    }
}
