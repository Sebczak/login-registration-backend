package com.comapny.security.config;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

public interface SecurityConfiguration {

    void configure(HttpSecurity http) throws Exception;

    void configure(AuthenticationManagerBuilder auth) throws Exception;
}
