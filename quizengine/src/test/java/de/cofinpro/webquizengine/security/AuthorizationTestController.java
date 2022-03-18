package de.cofinpro.webquizengine.security;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthorizationTestController {

    @GetMapping("/register")
    public String testRegisterIsPublic() {
        return "/register is accessed";
    }

    @PostMapping("/actuator/shutdown")
    public ResponseEntity<String> testShutdownIsPublic() {
        return ResponseEntity.ok("POST /actuator/shutdown is accessed");
    }

}
