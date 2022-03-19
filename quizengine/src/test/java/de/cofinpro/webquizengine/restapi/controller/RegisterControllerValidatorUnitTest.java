package de.cofinpro.webquizengine.restapi.controller;

import de.cofinpro.webquizengine.restapi.model.UserRequestBody;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.lang.reflect.Field;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RegisterControllerValidatorUnitTest {

    private UserRequestBody getValidUserRequest() {
        return new UserRequestBody("some_name@company.org", "secret_word");
    }

    private static Stream<Arguments> provideFieldAndValidValue() {
        return Stream.of(
                Arguments.of("email", "admin@cofinpro.de"),
                Arguments.of("email", "a_mueller@doodle_net.org"),
                Arguments.of("email", "a@b.de"),
                Arguments.of("password", "12345"),
                Arguments.of("password", "12_34:56*6-8+"),
                Arguments.of("password", "dvewvgrezmntumurum,uzmuzm,z")
        );
    }

    @ParameterizedTest
    @MethodSource("provideFieldAndValidValue")
    void registerUserValidRequests(String fieldName, Object validValue) throws Exception {
        UserRequestBody userRequest = getValidUserRequest();

        Field field = UserRequestBody.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(userRequest, validValue);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        final Validator validator = factory.getValidator();

        Set<ConstraintViolation<UserRequestBody>> constraintViolations =
                validator.validate(userRequest);
        assertEquals(0, constraintViolations.size());
    }

    private static Stream<Arguments> provideFieldAndInvalidValue() {
        return Stream.of(
                Arguments.of("email", null),
                Arguments.of("email", " "),
                Arguments.of("email", "no_email.com"),
                Arguments.of("email", "@google.com"),
                Arguments.of("email", "a@googlecom"),
                Arguments.of("email", "a@googlecom."),
                Arguments.of("email", "anton@google.com.net"),
                Arguments.of("password", null),
                Arguments.of("password", ""),
                Arguments.of("password", " "),
                Arguments.of("password", "1234")
        );
    }

    @ParameterizedTest
    @MethodSource("provideFieldAndInvalidValue")
    void registerUserInvalidRequests(String fieldName, Object invalidValue) throws Exception {
        UserRequestBody userRequest = getValidUserRequest();

        Field field = UserRequestBody.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(userRequest, invalidValue);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        final Validator validator = factory.getValidator();

        Set<ConstraintViolation<UserRequestBody>> constraintViolations =
                validator.validate(userRequest);
        assertEquals(1, constraintViolations.size());
    }
}
