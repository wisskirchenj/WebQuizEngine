package de.cofinpro.webquizengine.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Quiz {
    // all constant for now...
    private static final String TITLE = "The Java Logo";
    private static final String TEXT = "What is depicted on the Java logo?";
    private static final String[] OPTIONS = new String[] {
            "Robot", "Tea leaf", "Cup of coffee", "Bug" };

    private String title;
    private String text;
    private String[] options;

    public int findCorrectSolution() {
        return 2; // options array index, i.e. "Cup of Coffee"
    }

    public Quiz() {
        this.title = TITLE;
        this.text = TEXT;
        this.options = OPTIONS;
    }
}
