package com.mintpayments.api.customer.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@Configuration
public class BasicSecurityConfiguration extends WebSecurityConfigurerAdapter {

    /**
     * Create mock users for testing purposes.
     */
    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        //No password encryption
        authenticationManagerBuilder.inMemoryAuthentication()
                .withUser("user1").password("{noop}password1").roles("USER");

    }

    /**
     * Provide http basic validation as security for all methods
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers("/**").authenticated()
                .and()
                .csrf().disable()
                .formLogin().disable();
    }
}