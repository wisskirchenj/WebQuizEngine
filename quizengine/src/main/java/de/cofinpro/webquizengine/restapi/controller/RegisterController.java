package de.cofinpro.webquizengine.restapi.controller;

import de.cofinpro.webquizengine.restapi.model.UserRequestBody;
import de.cofinpro.webquizengine.restapi.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * RESTController-class offering the following endpoint - (public accessible, not authenticated):
 * POST "api/register" - register a new user by email and password
 */
@RestController
public class RegisterController {

    private final RegisterService registerService;

    @Autowired
    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    /**
     * POST endpoint "api/register" - receives and validates data to create a new user in the
     * RequestBody and store it for authentication
     * @param userRequest the register data received (e-mail and password)
     * @return the queried quiz if available or a 404-HTTP response
     */
    @PostMapping("api/register")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody UserRequestBody userRequest) {
        return registerService.registerUser(userRequest);
    }
}
