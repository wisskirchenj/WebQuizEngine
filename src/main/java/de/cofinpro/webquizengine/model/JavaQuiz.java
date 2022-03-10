package de.cofinpro.webquizengine.model;

/**
 * the special JavaQuiz that is created and injected to the REST controller on boot.
 * It is a singleton class and bean, declared in the WebQuizConfiguration. The
 * singleton object is created by the QuizGenerator.
 */
public final class JavaQuiz extends Quiz {

    private static final String TITLE = "The Java Logo";
    private static final String TEXT = "What is depicted on the Java logo?";
    private static final String[] OPTIONS = new String[] {
            "Robot", "Tea leaf", "Cup of coffee", "Bug" };

    JavaQuiz(int id) {
        this.setId(id);
        this.setTitle(TITLE);
        this.setText(TEXT);
        this.setOptions(OPTIONS);
        this.setSolution(2);
    }
}
