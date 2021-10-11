package com.cnu.spg.security.config;

import com.cnu.spg.security.filter.AuthenticationTokenValidFilter;
import com.cnu.spg.security.filter.AuthorizationProcessFilter;
import com.cnu.spg.security.handler.AuthFailureHandler;
import com.cnu.spg.security.provider.AuthProvider;
import com.cnu.spg.security.token.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService loginDetailService;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AuthorizationProcessFilter authorizationProcessFilter() throws Exception {
        AuthorizationProcessFilter authorizationProcessFilter = new AuthorizationProcessFilter(tokenProvider, authenticationManagerBean(), "/v1/login");
        authorizationProcessFilter.setAuthenticationFailureHandler(new AuthFailureHandler());

        return authorizationProcessFilter;
    }

    @Bean
    public AuthenticationTokenValidFilter authenticationTokenValidFilter() {
        return new AuthenticationTokenValidFilter(loginDetailService, tokenProvider);
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new AuthProvider(loginDetailService, passwordEncoder);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers(
                        "/h2-console/**",
                        "/h2-console"
                )
                .antMatchers(
                        "/swagger-ui/**",
                        "/swagger-resources/**",
                        "/v3/api-docs"
                );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/v1/login", "/api/v1/users", "/api/v1/health-check").permitAll()
                .anyRequest().authenticated();

        http
                .csrf().disable()
                .headers().frameOptions().disable();

        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS); // session 사용하지 않음

        http
                .addFilterBefore(authenticationTokenValidFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(authorizationProcessFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
