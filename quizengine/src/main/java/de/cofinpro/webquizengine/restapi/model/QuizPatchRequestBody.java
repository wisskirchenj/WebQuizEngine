package de.cofinpro.webquizengine.restapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * DTO object representing a quiz creation request body.
 * Different from Quiz objects, at this stage (before creation) no id has been assigned
 * and also different from GET requests the solution to the quiz has to be provided by client.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class QuizPatchRequestBody {

    private String title;
    private String text;
    @Size(min = 2)
    private List<String> options;
    private List<Integer> answer;
}
