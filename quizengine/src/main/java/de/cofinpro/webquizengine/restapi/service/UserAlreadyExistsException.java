package de.cofinpro.webquizengine.restapi.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 400-Error Response on register attempt of an already registered user.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException() {
        super("User already exists!");
    }
}
