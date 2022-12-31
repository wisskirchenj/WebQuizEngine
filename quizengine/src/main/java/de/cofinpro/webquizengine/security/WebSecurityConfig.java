package de.cofinpro.webquizengine.security;

import de.cofinpro.webquizengine.configuration.WebQuizConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http,
                                                   UserDetailsService userDetailsService) throws Exception {
        var authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.inMemoryAuthentication()
                .withUser(WebQuizConfiguration.USER_COFINPRO)
                .password(WebQuizConfiguration.USER_PASSWORD).roles("USER")
                .and().withUser(WebQuizConfiguration.ADMIN_COFINPRO)
                .password(WebQuizConfiguration.ADMIN_PASSWORD).roles("ADMIN")
                .and().passwordEncoder(getEncoder());
        authenticationManagerBuilder.userDetailsService(userDetailsService);
        return authenticationManagerBuilder.build();
    }

    @Bean
    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   AuthenticationManager authenticationManager) throws Exception {
        http.authenticationManager(authenticationManager)
                .authorizeHttpRequests()
                .shouldFilterAllDispatcherTypes(false)
                .requestMatchers(HttpMethod.POST,"/actuator/shutdown").permitAll()
                .requestMatchers("/h2").permitAll()
                .requestMatchers("/api/register").permitAll()
                .requestMatchers("/admin**").hasRole("ADMIN")
                .requestMatchers("/", "/api/**").authenticated()
                .and()
                .csrf().disable().httpBasic().and().formLogin();
        return http.build();
    }
}
