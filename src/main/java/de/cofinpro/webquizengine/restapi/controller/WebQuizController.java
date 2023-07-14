package de.cofinpro.webquizengine.restapi.controller;

import de.cofinpro.webquizengine.restapi.model.QuizCompletionResponse;
import de.cofinpro.webquizengine.restapi.model.QuizPatchRequestBody;
import de.cofinpro.webquizengine.restapi.model.QuizRequestBody;
import de.cofinpro.webquizengine.restapi.model.QuizResponse;
import de.cofinpro.webquizengine.restapi.model.QuizSolveResponse;
import de.cofinpro.webquizengine.restapi.service.QuizService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class WebQuizController {

    private final QuizService quizService;

    @Autowired
    public WebQuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    /**
     * GET endpoint - returning out all available web quizzes
     * @param page the page number to be displayed
     * @return a list of all quiz objects created in this session starting with the Java quiz
     */
    @GetMapping({"api/quiz/all", "api/quiz"})
    public List<QuizResponse> getQuizzes(@RequestParam(defaultValue = "0") Integer page) {
        return quizService.getQuizzes(page);
    }

    /**
     * GET endpoint "api/quizzes" - returning out all available web quizzes
     * @param jwt the jwt of the authenticated user
     * @param page the page number to be displayed
     * @return a list of all quiz objects created in this session starting with the Java quiz
     */
    @GetMapping("api/quiz/completed")
    public List<QuizCompletionResponse> getQuizCompletions(@AuthenticationPrincipal Jwt jwt,
                                                           @RequestParam(defaultValue = "0") Integer page) {
        return quizService.getCompletions(jwt.getSubject(), page);
    }

    /**
     * GET endpoint - with path variable {id}
     * e.g.: api/quiz/1 that returns the queried quiz if available or a 404-HTTP response
     * @param id the id of a quiz as path variable
     * @return the queried quiz if available or a 404-HTTP response
     */
    @GetMapping("api/quiz/{id}")
    public ResponseEntity<QuizResponse> getQuizById(@PathVariable("id") long id) {
        return quizService.getQuizById(id);
    }

    /**
     * POST endpoint - receives and validates data to create a new quiz in the
     * ResponseBody and displays it with the id information from creation
     * @param jwt the authenticated user
     * @param quizRequest the creation data received
     * @return the queried quiz if available or a 404-HTTP response
     */
    @PostMapping("api/quiz")
    public QuizResponse createQuiz(@AuthenticationPrincipal Jwt jwt,
                                   @Valid @RequestBody QuizRequestBody quizRequest) {
        return quizService.createQuiz(quizRequest, jwt.getSubject());
    }

    /**
     * POST endpoint - receiving a list of Integer answer to the quiz as request
     * @param jwt the authenticated user
     * @param id the id of a quiz as path variable
     * @param answerEntry the request body key:value data, consisting of an int array with valid
     *                    options for the quiz to solve as value
     * @return a boolean - string answer object QuizSolveResponse
     */
    @PostMapping("api/quiz/{id}/solve")
    public ResponseEntity<QuizSolveResponse> solveQuiz(@AuthenticationPrincipal Jwt jwt,
                                                       @PathVariable("id") long id,
                                                       @RequestBody Map.Entry<String, List<Integer>> answerEntry) {
        return quizService.returnSolveResponse(id, jwt.getSubject(), answerEntry.getValue());
    }

    /**
     * DELETE endpoint  - with path variable {id}
     * e.g.: api/quiz/2 that deletes the quiz to id given, if the authenticated user owns this quiz
     * if the quiz is not found - 404 is returned, if the quiz is not owned by the user - 403 is returned
     * if everything fits and the quiz can be deleted 204 is returned
     * @param jwt the user details of the authenticated user
     * @param id the id of a quiz as path variable
     * @return the appropriate status code - no response body
     */
    @DeleteMapping("api/quiz/{id}")
    public ResponseEntity<Object> deleteQuizById(@AuthenticationPrincipal Jwt jwt,
                                                 @PathVariable("id") long id) {
        return quizService.deleteQuizById(id, jwt.getSubject());
    }

    /**
     * PATCH endpoint "api/quizzes/{id}" - with path variable {id}
     * e.g.: api/quiz/2 that patches the quiz to id given, if the authenticated user owns this quiz.
     * All the fields given in the quizPatchRequestBody are optional, and will be applied, if the format is
     * valid. if the quiz is not found - 404 is returned, if the quiz is not owned by the user - 403 is returned
     * if everything fits and the quiz can be patched 200 is returned and the patched quiz is displayed
     * @param jwt the authenticated user
     * @param id the id of a quiz as path variable
     * @param quizPatchRequest the user sent patch request data
     * @return the patched quiz if available and owned or an error status as above explained
     */
    @PatchMapping("api/quiz/{id}")
    public ResponseEntity<QuizResponse> patchQuizById(@AuthenticationPrincipal Jwt jwt,
                                                 @PathVariable("id") long id,
                                                 @Valid @RequestBody QuizPatchRequestBody quizPatchRequest) {
        return quizService.patchQuizById(id, quizPatchRequest, jwt.getSubject());
    }
}