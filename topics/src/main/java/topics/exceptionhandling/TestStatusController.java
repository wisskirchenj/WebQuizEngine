package topics.exceptionhandling;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * In this task, you'll practice returning status codes. As you probably know, there are five groups of response status codes:
 *
 * Informational responses (100–199)
 * Successful responses (200–299)
 * Redirects (300–399)
 * Client errors (400–499)
 * Server errors (500–599)
 * Your task is to implement the following test endpoint:
 *
 * GET /test/{status} – responds with a specified status code.
 * The endpoint should support at least these 4 status codes:
 *
 * 200 – if this number is specified responds with 200 OK
 * 300 – if this number is specified responds with 300 Multiple Choices
 * 400 – if this number is specified responds with 400 Bad Request
 * 500 – if this number is specified responds with 500 Internal Server Error
 */
@RestController
public class TestStatusController {

    @GetMapping("/test/{status}")
    public ResponseEntity<?> returnStatus(@PathVariable int status) {
        return new ResponseEntity<>(HttpStatus.valueOf(status));
    }
}
