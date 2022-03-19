package de.cofinpro.webquizengine.restapi.service;

import de.cofinpro.webquizengine.persistence.RegisteredUser;
import de.cofinpro.webquizengine.persistence.RegisteredUserRepository;
import de.cofinpro.webquizengine.restapi.model.UserRequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Web service class, that provides the services to the endpoints of the RegisterController
 * REST-controller.
 */
@Service
public class RegisterService {

    private PasswordEncoder encoder;
    private RegisteredUserRepository userRepository;

    @Autowired
    public RegisterService(PasswordEncoder passwordEncoder, RegisteredUserRepository userRepository) {
        this.encoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    /**
     * service corresponding to POST endpoints "register".
     * saves the RegisteredUser entity created from the quizRequest in the Quiz Repo.
     * @param userRequest the user information DTO as received by POST
     * @return the created quiz information - also displaying the id-key to client
     */
    public ResponseEntity<Object> registerUser(UserRequestBody userRequest) {
        if (userRepository.findByUsername(userRequest.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException();
        }
        userRepository.save(new RegisteredUser().setUsername(userRequest.getEmail())
                .setEncryptedPassword(encoder.encode(userRequest.getPassword())));
        return ResponseEntity.ok().build();
    }
}
