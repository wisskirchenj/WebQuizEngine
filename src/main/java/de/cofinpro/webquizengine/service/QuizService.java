package de.cofinpro.webquizengine.service;

import de.cofinpro.webquizengine.persistence.Quiz;
import de.cofinpro.webquizengine.controller.QuizAnswer;
import de.cofinpro.webquizengine.persistence.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Web service class, that bundles all the endpoint functionality (as of now) for the
 * WebQuizController REST-controller.
 */
@Service
public class QuizService {

    private final QuizRepository quizRepository;

    @Autowired
    public QuizService(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    /**
     * service corresponding to GET endpoints "api/quiz" and "api/quizzes/{id}"
     * @param id the queried quiz id
     * @return the quiz as retrieved from the QuizGenerator component or a 404 NOT FOUND
     */
    public ResponseEntity<Quiz> getQuizById(int id) {
        return ResponseEntity.ok(findQuizByIdOrThrow(id));
    }

    private Quiz findQuizByIdOrThrow(long id) {
        return quizRepository.findById(id).orElseThrow(QuizNotFoundException::new);
    }

    /**
     * service corresponding to GET endpoints "api/quizzes"
     * @return all quizzes from the QuizGenerator component
     */
    public Iterable<Quiz> getQuizzes() {
        return quizRepository.findAll();
    }

    /**
     * service corresponding to POST endpoints "api/quizzes".
     * calls the QuizGenerator with the QuizRequestBody to create this quiz
     * @param quiz the quiz DTO as received by POST
     * @return the created quiz information - also displaying the id-key to client
     *
     */
    public Quiz createQuiz(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    /**
     * service corresponding to POST endpoints "api/quizzes/{id}/solve"
     * @param id the queried quiz id
     * @param answer the answer option, that the user chose
     * @return a feedback message on correctness of answer option or a 404 NOT FOUND
     */
    public ResponseEntity<QuizAnswer> returnSolveResponse(int id, List<Integer> answer) {
        Quiz quiz = findQuizByIdOrThrow(id);
        if (containsSameElements(answer, quiz.getAnswer())) {
            return ResponseEntity.ok(QuizAnswer.correct());
        }
        return ResponseEntity.ok(QuizAnswer.incorrect());
    }

    private boolean containsSameElements(List<Integer> answer, List<Integer> correctOptions) {
        if (answer.size() != correctOptions.size()) {
            return false;
        }
        answer.removeAll(correctOptions);
        return answer.size() == 0;
    }
}