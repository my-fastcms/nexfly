package com.nexfly.auth.config;

import com.nexfly.auth.handler.JwtAuthenticationFailureHandler;
import com.nexfly.auth.handler.JwtAuthenticationSuccessHandler;
import com.nexfly.auth.handler.NexflyLogoutSuccessHandler;
import com.nexfly.common.auth.provider.NexflyAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class NexflyAuthConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(formLoginConfigurer -> formLoginConfigurer.successHandler(jwtAuthenticationSuccessHandler())
                        .failureHandler(jwtAuthenticationFailureHandler())
                        .usernameParameter("email")
                )
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .logout(logout -> logout.logoutSuccessHandler(new NexflyLogoutSuccessHandler()))
                ;
        return http.build();
    }

    @Bean
    public JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler() {
        return new JwtAuthenticationSuccessHandler();
    }

    @Bean
    public JwtAuthenticationFailureHandler jwtAuthenticationFailureHandler() {
        return new JwtAuthenticationFailureHandler();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public NexflyAuthenticationProvider nexflyAuthenticationProvider(UserDetailsService userDetailsService,
                                        PasswordEncoder passwordEncoder) {
        NexflyAuthenticationProvider nexflyAuthenticationProvider = new NexflyAuthenticationProvider();
        nexflyAuthenticationProvider.setUserDetailsService(userDetailsService);
        nexflyAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return nexflyAuthenticationProvider;
    }

}
