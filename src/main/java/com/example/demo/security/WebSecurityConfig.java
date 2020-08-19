package com.example.demo.security;

import com.example.demo.details.UserDetailsServiceImpl;
import com.example.demo.jwt.JwtTokenAuthorizationOncePerRequestFilter;
import com.example.demo.jwt.JwtUnAuthorizedResponseAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    UserDetailsServiceImpl userDetailsService;

    JwtUnAuthorizedResponseAuthenticationEntryPoint jwtUnAuthorizedResponseAuthenticationEntryPoint;

    JwtTokenAuthorizationOncePerRequestFilter jwtTokenAuthorizationOncePerRequestFilter;

    @Value("${jwt.get.token.uri}")
    private String authenticationPath;

    public WebSecurityConfig(UserDetailsServiceImpl userDetailsService,
                             JwtTokenAuthorizationOncePerRequestFilter jwtTokenAuthorizationOncePerRequestFilter,
                             JwtUnAuthorizedResponseAuthenticationEntryPoint
                                     jwtUnAuthorizedResponseAuthenticationEntryPoint) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenAuthorizationOncePerRequestFilter = jwtTokenAuthorizationOncePerRequestFilter;
        this.jwtUnAuthorizedResponseAuthenticationEntryPoint = jwtUnAuthorizedResponseAuthenticationEntryPoint;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(jwtUnAuthorizedResponseAuthenticationEntryPoint).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .anyRequest().authenticated();

        httpSecurity
                .addFilterBefore(jwtTokenAuthorizationOncePerRequestFilter, UsernamePasswordAuthenticationFilter.class);

        httpSecurity
                .headers()
                .frameOptions().sameOrigin()
                .cacheControl();

        httpSecurity.cors().disable();
    }

    @Override
    public void configure(WebSecurity webSecurity) throws Exception {
        webSecurity
                .ignoring()
                .antMatchers(
                        HttpMethod.POST,
                        authenticationPath
                )
                .antMatchers(HttpMethod.OPTIONS, "/**")
                .and()
                .ignoring()
                .antMatchers(
                        HttpMethod.GET,
                        "/"
                )
                .and()
                .ignoring()
                .antMatchers("/h2-console/**/**")
                .and()
                .ignoring()
                .antMatchers("/user/register");
    }

}
