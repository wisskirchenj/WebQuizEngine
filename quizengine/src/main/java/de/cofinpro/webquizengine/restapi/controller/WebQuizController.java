package de.cofinpro.webquizengine.restapi.controller;

import de.cofinpro.webquizengine.restapi.model.*;
import de.cofinpro.webquizengine.restapi.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * RESTController-class offering the following endpoints:
 * GET api/quiz - display the standard Java Quiz
 * GET api/quizzes - display all quizzes, paginated
 * GET api/quizzes/{id} - display the quiz to id given
 * GET api/quizzes/completed - display all quiz completions of the authenticated user, paginated and sorted
 * POST api/quizzes - create a new quiz
 * POST api/quizzes/{id}/solve - post an answer with response parameter answer = id -> e.g. api/quiz?answer=0 to given quiz id
 * DELETE api/quizzes/{id} - deletes the quiz to id given, if the authenticated user owns this quiz
 * PATCH api/quizzes/{id} - patches the fields given to the quiz {id}, if the authenticated user owns this quiz
 */
@RestController
public class WebQuizController {

    private final QuizService quizService;

    @Autowired
    public WebQuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    /**
     * GET-endpoint "api/quiz", that returns the one and only predefined Java quiz
     * @return the JavaQuiz which is dependency injected
     */
    @GetMapping("api/quiz")
    public ResponseEntity<QuizResponse> getJavaQuiz() {
        return quizService.getJavaQuiz();
    }

    /**
     * GET endpoint "api/quizzes" - returning out all available web quizzes
     * @param page the page number to be displayed
     * @return an array of all quiz objects created in this session starting with the Java quiz
     */
    @GetMapping("api/quizzes")
    public List<QuizResponse> getQuizzes(@RequestParam(defaultValue = "0") Integer page) {
        return quizService.getQuizzes(page);
    }

    /**
     * GET endpoint "api/quizzes" - returning out all available web quizzes
     * @param userDetails the user details of the authenticated user
     * @param page the page number to be displayed
     * @return an array of all quiz objects created in this session starting with the Java quiz
     */
    @GetMapping("api/quizzes/completed")
    public List<QuizCompletionResponse> getQuizCompletions(@AuthenticationPrincipal UserDetails userDetails,
                                                           @RequestParam(defaultValue = "0") Integer page) {
        return quizService.getCompletions(userDetails.getUsername(), page);
    }

    /**
     * GET endpoint "api/quizzes/{id}" - with path variable {id}
     * e.g.: api/quiz/1 that returns the queried quiz if available or a 404-HTTP response
     * @param id the id of a quiz as path variable
     * @return the queried quiz if available or a 404-HTTP response
     */
    @GetMapping("api/quizzes/{id}")
    public ResponseEntity<QuizResponse> getQuizById(@PathVariable("id") long id) {
        return quizService.getQuizById(id);
    }

    /**
     * POST endpoint "api/quizzes" - receives and validates data to create a new quiz in the
     * ResponseBody and displays it with the id information from creation
     * @param userDetails the user details of the authenticated user
     * @param quizRequest the creation data received
     * @return the queried quiz if available or a 404-HTTP response
     */
    @PostMapping("api/quizzes")
    public QuizResponse createQuiz(@AuthenticationPrincipal UserDetails userDetails,
                                   @Valid @RequestBody QuizRequestBody quizRequest) {
        return quizService.createQuiz(quizRequest, userDetails.getUsername());
    }

    /**
     * POST endpoint "api/quizzes/{id}/solve" - receiving an Integer answer to the quiz
     * as request parameter - e.g.: api/quiz?answer=0, that corresponds to the solution option
     * the client chooses (starting with 0).
     * @param userDetails the user details of the authenticated user
     * @param id the id of a quiz as path variable
     * @param answerEntry the request body key:value data, consisting of an int array with valid
     *                    options for the quiz to solve as value
     * @return a boolean - string answer object QuizSolveResponse
     */
    @PostMapping("api/quizzes/{id}/solve")
    public ResponseEntity<QuizSolveResponse> solveQuiz(@AuthenticationPrincipal UserDetails userDetails,
                                                       @PathVariable("id") long id,
                                                       @RequestBody Map.Entry<String, List<Integer>> answerEntry) {
        return quizService.returnSolveResponse(id, userDetails.getUsername(), answerEntry.getValue());
    }

    /**
     * DELETE endpoint "api/quizzes/{id}" - with path variable {id}
     * e.g.: api/quiz/2 that deletes the quiz to id given, if the authenticated user owns this quiz
     * if the quiz is not found - 404 is returned, if the quiz is not owned by the user - 403 is returned
     * if everything fits and the quiz can be deleted 204 is returned
     * @param userDetails the user details of the authenticated user
     * @param id the id of a quiz as path variable
     * @return the appropriate status code - no response body
     */
    @DeleteMapping("api/quizzes/{id}")
    public ResponseEntity<Object> deleteQuizById(@AuthenticationPrincipal UserDetails userDetails,
                                                 @PathVariable("id") long id) {
        return quizService.deleteQuizById(id, userDetails);
    }

    /**
     * PATCH endpoint "api/quizzes/{id}" - with path variable {id}
     * e.g.: api/quiz/2 that patches the quiz to id given, if the authenticated user owns this quiz.
     * All the fields given in the quizPatchRequestBody are optional, and will be applied, if the format is
     * valid. if the quiz is not found - 404 is returned, if the quiz is not owned by the user - 403 is returned
     * if everything fits and the quiz can be patched 200 is returned and the patched quiz is displayed
     * @param userDetails the user details of the authenticated user
     * @param id the id of a quiz as path variable
     * @param quizPatchRequest the user sent patch request data
     * @return the patched quiz if available and owned or an error status as above explained
     */
    @PatchMapping("api/quizzes/{id}")
    public ResponseEntity<QuizResponse> patchQuizById(@AuthenticationPrincipal UserDetails userDetails,
                                                 @PathVariable("id") long id,
                                                 @Valid @RequestBody QuizPatchRequestBody quizPatchRequest) {
        return quizService.patchQuizById(id, quizPatchRequest, userDetails);
    }
}