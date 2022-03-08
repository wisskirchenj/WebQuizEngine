package de.cofinpro.webquizengine.configuration;

import de.cofinpro.webquizengine.model.Quiz;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebQuizConfiguration {

    @Bean
    public Quiz quiz() {
        return new Quiz();
    }
}
