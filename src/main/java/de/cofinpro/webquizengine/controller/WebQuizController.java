package de.cofinpro.webquizengine.controller;

import de.cofinpro.webquizengine.model.Quiz;
import de.cofinpro.webquizengine.model.QuizAnswer;
import org.springframework.web.bind.annotation.*;

/**
 * RESTController-class offering the following endpoints:
 * GET api/quiz - JSON object of a Quiz
 * POST api/quiz - post an answer with response parameter answer = id -> e.g. api/quiz?answer=0
 */
@RestController
public class WebQuizController {

    private final Quiz quiz;

    public WebQuizController(Quiz quiz) {
        this.quiz = quiz;
    }

    /**
     * GET-endpoint "api/quiz", that returns a predefined simple web quiz
     */
    @GetMapping("api/quiz")
    public Quiz getQuiz() {
        return quiz;
    }

    /**
     * POST endpoint "api/quiz" - receiving an Integer answer (0-3) to the quiz
     * given by GET as parameter - e.g.: api/quiz?answer=0
     * @param answer the answer choice as integer
     * @return a boolean - string answer object QuizAnswer
     */
    @PostMapping("api/quiz")
    public QuizAnswer answerQuiz(@RequestParam("answer") Integer answer) {
        if (answer == quiz.findCorrectSolution()) {
            return new QuizAnswer(true, "Cooooooorrect, oider!");
        }
        return new QuizAnswer(false, "Nöööööö !");
    }
}
