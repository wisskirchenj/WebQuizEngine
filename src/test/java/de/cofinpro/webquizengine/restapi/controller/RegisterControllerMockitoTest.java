package de.cofinpro.webquizengine.restapi.controller;

import de.cofinpro.webquizengine.restapi.model.UserRequestBody;
import de.cofinpro.webquizengine.restapi.service.RegisterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Deprecated
@SpringBootTest
@MockitoSettings
class RegisterControllerMockitoTest {

    @MockBean
    RegisterService mockService;

    @InjectMocks
    RegisterController registerController;

    @BeforeEach
    void setUp() {
    }

    @Test
    void registerUserValidRequest() {
        UserRequestBody userRequest = new UserRequestBody("dummy@company.org", "password");
        registerController.registerUser(userRequest);
        // check if validation succeeded - service called
        verify(mockService, times(1)).registerUser(userRequest);
    }

    /**
     * NOT WORKING (validation not done)-> direct controller call obviously prevents correct framework validator handling..
     */
    //@Test
    void registerUserInValidEmail() {
        UserRequestBody userRequest = new UserRequestBody("dummycompany.org", "password");
        registerController.registerUser(userRequest);
        // check if validation prevents service call
        verify(mockService, times(0)).registerUser(any(UserRequestBody.class));
    }
}