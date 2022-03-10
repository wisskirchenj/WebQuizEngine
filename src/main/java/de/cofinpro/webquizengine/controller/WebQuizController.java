package de.cofinpro.webquizengine.controller;

import de.cofinpro.webquizengine.model.JavaQuiz;
import de.cofinpro.webquizengine.model.Quiz;
import de.cofinpro.webquizengine.model.QuizAnswer;
import de.cofinpro.webquizengine.model.QuizRequestBody;
import de.cofinpro.webquizengine.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * RESTController-class offering the following endpoints:
 * GET api/quiz - display the standard Java Quiz
 * GET api/quizzes - display all quizzes
 * GET api/quizzes/{id} - display the quiz to id given
 * POST api/quizzes - create a new quiz
 * POST api/quizzes/{id}/solve - post an answer with response parameter answer = id -> e.g. api/quiz?answer=0 to given quiz id
 */
@RestController
public class WebQuizController {

    private final QuizService quizService;
    private final JavaQuiz javaQuiz;

    @Autowired
    public WebQuizController(QuizService quizService, JavaQuiz javaQuiz) {
        this.quizService = quizService;
        this.javaQuiz = javaQuiz;
    }

    /**
     * GET-endpoint "api/quiz", that returns the one and only predefined Java quiz
     * @return th JavaQuiz which is dependency injected
     */
    @GetMapping("api/quiz")
    public JavaQuiz getQuiz() {
        return this.javaQuiz;
    }

    /**
     * GET endpoint "api/quizzes" - returning out all available web quizzes
     * @return an array of all quiz objects created in this session starting with the Java quiz
     */
    @GetMapping("api/quizzes")
    public Quiz[] getQuizzes() {
        return quizService.getQuizzes();
    }

    /**
     * GET endpoint "api/quizzes/{id}" - with path variable {id}
     * e.g.: api/quiz/1 that returns the queried quiz if available or a 404-HTTP response
     * @param id the id of a quiz as path variable
     * @return the queried quiz if available or a 404-HTTP response
     */
    @GetMapping("api/quizzes/{id}")
    public ResponseEntity<Quiz> getQuizById(@PathVariable("id") int id) {
        return quizService.getQuizById(id);
    }

    /**
     * POST endpoint "api/quizzes" - receives data to create a new quiz in the ResponseBody and
     * displays it with the id information from creation
     * @return the queried quiz if available or a 404-HTTP response
     */
    @PostMapping("api/quizzes")
    public Quiz createQuiz(@RequestBody QuizRequestBody quizRequestBody) {
        return quizService.createQuiz(quizRequestBody);
    }

    /**
     * POST endpoint "api/quizzes/{id}/solve" - receiving an Integer answer to the quiz
     * as request parameter - e.g.: api/quiz?answer=0, that corresponds to the solution option
     * the client chooses (starting with 0).
     * @param id the id of a quiz as path variable
     * @param answer the id of a quiz as path variable
     * @return a boolean - string answer object QuizAnswer
     */
    @PostMapping("api/quizzes/{id}/solve")
    public ResponseEntity<QuizAnswer> answerQuiz(@PathVariable("id") int id,
                                 @RequestParam("answer") int answer) {
        return quizService.returnSolveResponse(id, answer);
    }
}
