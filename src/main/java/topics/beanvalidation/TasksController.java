package topics.beanvalidation;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The program has one POST endpoint that receives a task as a JSON object with two fields: name (string) and
 * description (string).
 *
 * Your task is to analyze the code and make some changes so that the program would only accept the tasks that
 * meet the following requirements:
 *
 * The length of the name must be from 1 to 50 inclusive.
 * The length of the description must be from 1 to 200 inclusive.
 * Both fields must not be blank.
 * If these requirements are met, the program should respond with 200 OK status code, otherwise 400 Bad Request.
 */
@RestController
public class TasksController {
    final List<Task> tempDb = new CopyOnWriteArrayList<>();

    @PostMapping("/tasks")
    public void addTask(@Valid @RequestBody Task task) {
        tempDb.add(task);
    }
}

