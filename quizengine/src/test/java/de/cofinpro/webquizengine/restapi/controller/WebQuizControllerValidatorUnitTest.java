package de.cofinpro.webquizengine.restapi.controller;

import de.cofinpro.webquizengine.restapi.model.QuizRequestBody;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WebQuizControllerValidatorUnitTest {

    private QuizRequestBody getValidQuizRequest() {
        return new QuizRequestBody("The quiz tile",
                "The text - Which are correct?",
                List.of("option 1", "option 2", "option 3"),
                List.of(0,2) //answer
        );
    }

    private static Stream<Arguments> provideFieldAndValidValue() {
        return Stream.of(
                Arguments.of("title", " some normal !"),
                Arguments.of("title", " "),
                Arguments.of("title", "Title"),
                Arguments.of("title", "long\n 2 lines\n"),
                Arguments.of("text", "  ?"),
                Arguments.of("text", "more\nthan\none\nline\n ?"),
                Arguments.of("options", List.of("1","2")),
                Arguments.of("options", List.of("","")),
                Arguments.of("answer", null),
                Arguments.of("answer", List.of()),
                Arguments.of("answer", List.of(0,1,2))
        );
    }

    @ParameterizedTest
    @MethodSource("provideFieldAndValidValue")
    void createQuizValidRequests(String fieldName, Object validValue) throws Exception {
        QuizRequestBody quizRequest = getValidQuizRequest();

        Field field = QuizRequestBody.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(quizRequest, validValue);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        final Validator validator = factory.getValidator();

        Set<ConstraintViolation<QuizRequestBody>> constraintViolations =
                validator.validate(quizRequest);
        assertEquals(0, constraintViolations.size());
    }

    private static Stream<Arguments> provideFieldAndInvalidValue() {
        return Stream.of(
                Arguments.of("title", ""),
                Arguments.of("title", null),
                Arguments.of("text", null),
                Arguments.of("options", List.of("option 1")),
                Arguments.of("options", List.of()),
                Arguments.of("options", null)
        );
    }

    @ParameterizedTest
    @MethodSource("provideFieldAndInvalidValue")
    void createQuizInvalidRequests(String fieldName, Object validValue) throws Exception {
        QuizRequestBody quizRequest = getValidQuizRequest();

        Field field = QuizRequestBody.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(quizRequest, validValue);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        final Validator validator = factory.getValidator();

        Set<ConstraintViolation<QuizRequestBody>> constraintViolations =
                validator.validate(quizRequest);
        assertEquals(1, constraintViolations.size());
    }


    //@Test
    void patchQuizById() {
    }
}