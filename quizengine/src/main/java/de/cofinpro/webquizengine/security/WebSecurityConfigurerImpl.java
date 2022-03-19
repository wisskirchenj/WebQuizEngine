package de.cofinpro.webquizengine.security;

import de.cofinpro.webquizengine.configuration.WebQuizConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
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

    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    public WebSecurityConfigurerImpl(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser(WebQuizConfiguration.USER_COFINPRO)
                .password(getEncoder().encode(WebQuizConfiguration.USER_PASSWORD)).roles("USER")
                .and().withUser(WebQuizConfiguration.ADMIN_COFINPRO)
                .password(getEncoder().encode(WebQuizConfiguration.ADMIN_PASSWORD)).roles("ADMIN")
                .and().passwordEncoder(getEncoder());
        auth.userDetailsService(userDetailsService).passwordEncoder(getEncoder());
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
                .mvcMatchers("/h2").permitAll()
                .and().csrf().disable().headers().frameOptions().disable();
        http.authorizeRequests()
                .mvcMatchers("/api/register").permitAll()
                .mvcMatchers("/admin**").hasRole("ADMIN")
                .mvcMatchers("/", "/api/**").authenticated()
                .and().csrf().disable().httpBasic().and().formLogin();
    }
}
