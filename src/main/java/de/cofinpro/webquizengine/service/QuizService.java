package de.cofinpro.webquizengine.service;

import de.cofinpro.webquizengine.model.Quiz;
import de.cofinpro.webquizengine.model.QuizAnswer;
import de.cofinpro.webquizengine.model.QuizGenerator;
import de.cofinpro.webquizengine.model.QuizRequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Web service class, that bundles all the endpoint functionality (as of now) for the
 * WebQuizController REST-controller.
 */
@Service
public class QuizService {

    private final QuizGenerator quizGenerator;

    public QuizService(@Autowired QuizGenerator quizGenerator) {
        this.quizGenerator = quizGenerator;
    }

    /**
     * service corresponding to GET endpoints "api/quiz" and "api/quizzes/{id}"
     * @param id the queried quiz id
     * @return the quiz as retrieved from the QuizGenerator component or a 404 NOT FOUND
     */
    public ResponseEntity<Quiz> getQuizById(int id) {
        Quiz quiz = quizGenerator.findQuizById(id);
        if (quiz == null) {
            throw new QuizNotFoundException("Invalid quiz id given!");
        }
        return new ResponseEntity<>(quiz, HttpStatus.OK);
    }

    /**
     * service corresponding to GET endpoints "api/quizzes"
     * @return all quizzes from the QuizGenerator component
     */
    public Quiz[] getQuizzes() {
        return quizGenerator.getQuizzes();
    }

    /**
     * service corresponding to POST endpoints "api/quizzes".
     * calls the QuizGenerator with the QuizRequestBody to create this quiz
     * @param quizRequestBody the QuizRequestBody DTO as received by POST
     * @return the created quiz information - also displaying the id-key to client
     *
     */
    public Quiz createQuiz(QuizRequestBody quizRequestBody) {
        return quizGenerator.createQuiz(quizRequestBody);
    }

    /**
     * service corresponding to POST endpoints "api/quizzes/{id}/solve"
     * @param id the queried quiz id
     * @param answer the answer option, that the user chose
     * @return a feedback message on correctness of answer option or a 404 NOT FOUND
     */
    public ResponseEntity<QuizAnswer> returnSolveResponse(int id, int answer) {
        Quiz quiz = quizGenerator.findQuizById(id);
        if (quiz == null) {
            throw new QuizNotFoundException("Invalid quiz id given!");
        }
        if (answer == quiz.getSolution()) {
            return ResponseEntity.ok(new QuizAnswer(true, "Cooooooorrect, oider!"));
        }
        return ResponseEntity.ok(new QuizAnswer(false, "Nöööööö !"));
    }
}