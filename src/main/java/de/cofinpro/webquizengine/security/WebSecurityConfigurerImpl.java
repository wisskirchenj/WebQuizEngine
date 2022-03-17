package de.cofinpro.webquizengine.security;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class WebSecurityConfigurerImpl extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("juergen").password(getEncoder().encode("secret")).roles("USER")
                .and().withUser("jonas").password(getEncoder().encode("topsecret")).roles("ADMIN")
                .and().passwordEncoder(getEncoder());
    }

    @Bean
    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .mvcMatchers(HttpMethod.POST,"/actuator/shutdown").permitAll()
                .and().csrf().disable().httpBasic();
        http.authorizeRequests()
                .mvcMatchers("/register").permitAll()
                .mvcMatchers("/register.html").permitAll()
                .mvcMatchers("/admin.html").hasRole("ADMIN")
                .mvcMatchers("/", "/api/**").authenticated()
                .and().csrf().disable().httpBasic();
    }
}
