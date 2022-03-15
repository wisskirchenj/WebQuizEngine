package de.cofinpro.webquizengine.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 404-Error Response on invalid id given as path variable by client
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class QuizNotFoundException extends RuntimeException {

    public QuizNotFoundException() {
        super("Invalid quiz id given!");
    }
}
