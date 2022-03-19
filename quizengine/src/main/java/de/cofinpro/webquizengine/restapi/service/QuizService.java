package de.cofinpro.webquizengine.restapi.service;

import de.cofinpro.webquizengine.configuration.WebQuizConfiguration;
import de.cofinpro.webquizengine.persistence.Quiz;
import de.cofinpro.webquizengine.persistence.QuizRepository;
import de.cofinpro.webquizengine.restapi.model.QuizAnswer;
import de.cofinpro.webquizengine.restapi.model.QuizPatchRequestBody;
import de.cofinpro.webquizengine.restapi.model.QuizRequestBody;
import de.cofinpro.webquizengine.restapi.model.QuizResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Web service class, that bundles all the endpoint functionality (as of now) for the
 * WebQuizController REST-controller.
 */
@Service
public class QuizService {

    private QuizRepository quizRepository;

    @Autowired
    public QuizService(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    /**
     * service corresponding to GET endpoints "api/quiz"
     * @return the injected java quiz as retrieved from the QuizRepository
     */
    public ResponseEntity<QuizResponse> getJavaQuiz() {
        return ResponseEntity.ok(QuizResponse.fromQuiz(
                quizRepository.findByTitle(WebQuizConfiguration.JAVA_QUIZ_TITLE).orElseThrow()
        ));

    }

    /**
     * service corresponding to GET endpoint "api/quizzes/{id}"
     * @param id the queried quiz id
     * @return the quiz as retrieved from the QuizRepository or a 404 NOT FOUND
     */
    public ResponseEntity<QuizResponse> getQuizById(long id) {
        return ResponseEntity.ok(QuizResponse.fromQuiz(findQuizByIdOrThrow(id)));
    }

    private Quiz findQuizByIdOrThrow(long id) {
        return quizRepository.findById(id).orElseThrow(QuizNotFoundException::new);
    }

    /**
     * service corresponding to GET endpoints "api/quizzes"
     * @return all quizzes from the QuizGenerator component
     */
    public List<QuizResponse> getQuizzes() {
        List<QuizResponse> quizzes = new ArrayList<>();
        for (Quiz quiz : quizRepository.findAll()) {
            quizzes.add(QuizResponse.fromQuiz(quiz));
        }
        return quizzes;
    }

    /**
     * service corresponding to POST endpoints "api/quizzes".
     * saves the quiz entity created from the quizRequest in the Quiz Repo.
     * @param quizRequest the quiz DTO as received by POST
     * @return the created quiz information - also displaying the id-key to client
     */
    public QuizResponse createQuiz(QuizRequestBody quizRequest, String username) {
        return QuizResponse.fromQuiz(quizRepository.save(
                quizRequest.toQuiz().setUsername(username)));
    }

    /**
     * service corresponding to POST endpoints "api/quizzes/{id}/solve"
     * @param id the queried quiz id
     * @param answer the answer option, that the user chose
     * @return a feedback message on correctness of answer option or a 404 NOT FOUND
     */
    public ResponseEntity<QuizAnswer> returnSolveResponse(long id, List<Integer> answer) {
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
        return answer.isEmpty();
    }

    /**
     * service method to delete a quiz if owned by the user, with details given by the controller.
     * if the quiz is not found - 404 is returned, if the quiz is not owned by the user - 403 is returned
     * if everything fits and the quiz can be deleted 204 is returned
     * @param id the id of a quiz as path variable
     * @return an empty ResponseEntity with given HTTP-Status
     */
    public ResponseEntity<Object> deleteQuizById(long id, UserDetails userDetails) {
        Quiz quiz = findQuizByIdOrThrow(id);
        if (quiz.getUsername().equals(userDetails.getUsername())) {
            quizRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    /**
     * service method to patch a quiz if it is owned by the user, with details given by the controller.
     * if the quiz is not found - 404 is returned, if the quiz is not owned by the user - 403 is returned
     * if everything fits and the quiz can be patched 200 is returned and the patched quiz is displayed
     * @param id the id of a quiz as path variable
     * @return the patched quiz if available or a 404- or 403-HTTP response
     */
    public ResponseEntity<QuizResponse> patchQuizById(long id, QuizPatchRequestBody quizPatchRequest,
                                                      UserDetails userDetails) {
        Quiz quiz = findQuizByIdOrThrow(id);
        if (quiz.getUsername().equals(userDetails.getUsername())) {

            quizRepository.save(quiz.applyPatchRequest(quizPatchRequest));
            return ResponseEntity.ok(QuizResponse.fromQuiz(quiz));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}