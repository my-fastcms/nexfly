package com.nexfly.auth.config;

import com.nexfly.auth.handler.JwtAuthenticationFailureHandler;
import com.nexfly.auth.handler.JwtAuthenticationSuccessHandler;
import com.nexfly.auth.handler.NexflyLogoutSuccessHandler;
import com.nexfly.auth.handler.Oauth2AuthenticationSuccessHandler;
import com.nexfly.common.auth.provider.NexflyAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @see org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter
 *
 * @see org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter
 * @see org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationProvider
 * @see org.springframework.security.oauth2.client.authentication.OAuth2AuthorizationCodeAuthenticationProvider
 * @see org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient
 *
 */
@Configuration
@EnableWebSecurity
public class NexflyAuthConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize.anyRequest().authenticated())
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(formLoginConfigurer -> formLoginConfigurer.successHandler(jwtAuthenticationSuccessHandler())
                        .failureHandler(jwtAuthenticationFailureHandler())
                )
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .logout(logout -> logout.logoutSuccessHandler(new NexflyLogoutSuccessHandler()))
                .oauth2Login(
                        (oauth2LoginConfig) ->
                                oauth2LoginConfig.authorizationEndpoint((authorizationEndpointConfig)
                                        -> authorizationEndpointConfig
                                        .authorizationRedirectStrategy(new NexflyRedirectStrategy())
                                        .authorizationRequestRepository(redisOAuth2AuthorizationRequestRepository())
                                )
                                        .successHandler(oauth2AuthenticationSuccessHandler())

                )
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
        DelegatingPasswordEncoder delegatingPasswordEncoder = (DelegatingPasswordEncoder) PasswordEncoderFactories.createDelegatingPasswordEncoder();
        delegatingPasswordEncoder.setDefaultPasswordEncoderForMatches(new BCryptPasswordEncoder());
        return delegatingPasswordEncoder;
    }

    @Bean
    public NexflyAuthenticationProvider nexflyAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        NexflyAuthenticationProvider nexflyAuthenticationProvider = new NexflyAuthenticationProvider();
        nexflyAuthenticationProvider.setUserDetailsService(userDetailsService);
        nexflyAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return nexflyAuthenticationProvider;
    }

//    @Bean
//    @ConditionalOnMissingBean(ClientRegistrationRepository.class)
//    InMemoryClientRegistrationRepository clientRegistrationRepository() {
//        ClientRegistration clientRegistration = ClientRegistration.withRegistrationId("nexfly-client")
//                .clientId("nexfly-client")
//                .clientSecret("secret")
//                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                .scope(OidcScopes.OPENID, OidcScopes.PROFILE)
//                .redirectUri("http://127.0.0.1:6000/nexfly-auth/login/oauth2/code/nexfly-client")
//                .authorizationUri("http://localhost:8080/oauth2/authorize")
//                .tokenUri("http://localhost:8080/oauth2/token")
//                .userInfoUri("http://localhost:8080/userinfo")
//                .jwkSetUri("http://localhost:8080/oauth2/jwks")
//                .userNameAttributeName("sub")
//                .build();
//        return new InMemoryClientRegistrationRepository();
//    }

    @Bean
    RedisOAuth2AuthorizationRequestRepository redisOAuth2AuthorizationRequestRepository() {
        return new RedisOAuth2AuthorizationRequestRepository();
    }

    @Bean
    Oauth2AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler() {
        return new Oauth2AuthenticationSuccessHandler();
    }

}
