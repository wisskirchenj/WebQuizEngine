package topics.beanvalidation;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * The program contains the following endpoint:
 *
 * GET: /test/{id} — receives an integer id and returns it. Accepts negative and positive integers.
 *
 * You need to change the behavior of the program by adding two annotations. The program must accept only
 * the integers from 1 (inclusive) to 100 (exclusive).
 */
@RestController
@Validated
public class ValidatedPathVariableController {

    @GetMapping("/test/{id}")
    public int test(@PathVariable @Min(1)
                    @Max(value=99, message="must be < 100") int id) {
        return id;
    }
}
