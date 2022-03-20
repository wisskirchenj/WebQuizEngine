package de.cofinpro.webquizengine.restapi.controller;

import de.cofinpro.webquizengine.restapi.model.QuizPatchRequestBody;
import de.cofinpro.webquizengine.restapi.model.QuizRequestBody;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WebQuizControllerValidatorUnitTest {

    final JPAUnitTestValidator<QuizRequestBody> quizValidator = new JPAUnitTestValidator<>(this::getValidQuizRequest);
    final JPAUnitTestValidator<QuizPatchRequestBody> patchValidator
            = new JPAUnitTestValidator<>(this::getValidQuizPatchRequest);

    QuizRequestBody getValidQuizRequest() {
        return new QuizRequestBody("The quiz tile",
                "The text - Which are correct?",
                List.of("option 1", "option 2", "option 3"),
                List.of(0,2) //answer
        );
    }

    @ParameterizedTest
    @MethodSource("provideFieldAndValidValueQuizRequest")
    void createQuizValidRequests(String fieldName, Object validValue) throws Exception {
        assertTrue(quizValidator.getConstraintViolationsOnUpdate(fieldName, validValue).isEmpty());
    }

    static Stream<Arguments> provideFieldAndValidValueQuizRequest() {
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
    @MethodSource("provideFieldAndInvalidValueQuizRequest")
    void createQuizInvalidRequests(String fieldName, Object invalidValue) throws Exception {
        assertEquals(1, quizValidator.getConstraintViolationsOnUpdate(fieldName, invalidValue).size());
    }

    static Stream<Arguments> provideFieldAndInvalidValueQuizRequest() {
        return Stream.of(
                Arguments.of("title", ""),
                Arguments.of("title", null),
                Arguments.of("text", null),
                Arguments.of("options", List.of("option 1")),
                Arguments.of("options", List.of()),
                Arguments.of("options", null)
        );
    }

    QuizPatchRequestBody getValidQuizPatchRequest() {
        return new QuizPatchRequestBody("New title",
                "The new text. Choose:",
                List.of("newopt 1", "newopt 2", "newopt 3"),
                List.of(0,2) //answer
        );
    }

    @ParameterizedTest
    @MethodSource("provideFieldAndValidValuePatchRequest")
    void patchQuizValidRequest(String fieldName, Object validValue) throws Exception {
        assertTrue(patchValidator.getConstraintViolationsOnUpdate(fieldName, validValue).isEmpty());
    }


    static Stream<Arguments> provideFieldAndValidValuePatchRequest() {
        return Stream.of(
                Arguments.of("title", ""),
                Arguments.of("title", null),
                Arguments.of("text", null),
                Arguments.of("text", ""),
                Arguments.of("options", List.of("new option 1", "new option 2")),
                Arguments.of("options", null),
                Arguments.of("answer", null),
                Arguments.of("answer", List.of()),
                Arguments.of("answer", List.of(1,2,3,0))
        );
    }


    @ParameterizedTest
    @MethodSource("provideFieldAndInvalidValuePatchRequest")
    void patchQuizInvalidRequest(String fieldName, Object invalidValue) throws Exception {
        assertEquals(1, patchValidator.getConstraintViolationsOnUpdate(fieldName, invalidValue).size());
    }


    static Stream<Arguments> provideFieldAndInvalidValuePatchRequest() {
        return Stream.of(
                Arguments.of("options", List.of("new option 1")),
                Arguments.of("options", List.of())
        );
    }

}