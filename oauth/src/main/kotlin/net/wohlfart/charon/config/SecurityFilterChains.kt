package net.wohlfart.charon.config

import net.wohlfart.charon.OAuthProperties
import net.wohlfart.charon.component.CharonRevocationAuthenticationProvider
import net.wohlfart.charon.controller.REQUEST_PATH_CONFIRM
import net.wohlfart.charon.controller.REQUEST_PATH_ERROR
import net.wohlfart.charon.controller.REQUEST_PATH_HOME
import net.wohlfart.charon.controller.REQUEST_PATH_REGISTER
import net.wohlfart.charon.federation.FederatedIdentityAuthenticationSuccessHandler
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.MediaType
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.*
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint
import org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher


@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
class SecurityFilterChains {

    // see: https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter
    //      https://docs.spring.io/spring-authorization-server/docs/current/reference/html/getting-started.html
    //      https://stackoverflow.com/questions/75724218/spring-security-oauth2-authorization-server
    @Bean
    @Throws(Exception::class)
    @Order(Ordered.HIGHEST_PRECEDENCE)  // http://127.0.0.1:8081/.well-known/openid-configuration needs to be available before the login
    fun authorizationServerSecurityFilterChain(
        http: HttpSecurity,
        oAuth2AuthorizationService: OAuth2AuthorizationService,
        jwtDecoder: JwtDecoder,
    ): SecurityFilterChain {
        // use our global cors config
        http.cors { } // picks up our default cors config for the token endpoint
        /* frontend send this to the revoke endpoint:
        client_id: public-client
        token: eyJraWQiOiJmNzc2OGRiYy01Mzk0LTRjNzktOGEzZC1mNzUxMTYwM2FlZjIiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiYXVkIjoicHVibGljLWNsaWVudCIsIm5iZiI6MTY4Mzk4NjU3Niwic2NvcGUiOlsib3BlbmlkIiwicHJvZmlsZSIsIm9mZmxpbmVfYWNjZXNzIiwiZW1haWwiXSwiaXNzIjoiaHR0cDovLzEyNy4wLjAuMTo4MDgxIiwiZXhwIjoxNjgzOTg2NjM2LCJ1c2VyTmFtZSI6Imp1c3QgdGVzdGluZyBhY2Nlc3MgdG9rZW4iLCJpYXQiOjE2ODM5ODY1NzZ9.PUapCVas1zrntfo7S4jWUT3PR409B6Ef8o9MPaj6b6z5vJOW2wplhrmpimKLOquo-MsEAhkCIqDrotqN18S2UVU-np4mtvs1asGxEl5l81citaIQuOq1rCMYGwd2e6VkEebEVjpXQktVzFkKjgtRM3jQGofL34wtmHFIPOX5q43cjgFiuYl77xwUlSz-pr_Q-yPIkXi6YV1NL39wZD2MsRBduoBGMsw7tnv1dC5IP3V0_fGtsTpP1-Pt7OF-p8qG9AJ6Vdrm_POTQIuVdhXEq-4cmJTci4yjDFp4HT5hKcd1VQ4MWWFiuZLM7qt84ZfniIoYwRq-ik0uWyLc9jyxMQ
        token_type_hint: access_token
        */
        val authorizationServerConfigurer = OAuth2AuthorizationServerConfigurer()
        authorizationServerConfigurer
            // OpenID Connect 1.0 is disabled in the default configuration, we need to enable it
            .oidc(Customizer.withDefaults())
            .tokenRevocationEndpoint { tokenRevocationEndpoint
                ->
                tokenRevocationEndpoint
                    .authenticationProvider(CharonRevocationAuthenticationProvider())
                //    .revocationResponseHandler()
                // https://stackoverflow.com/questions/71568725/spring-authorization-server-0-2-2-how-to-disable-a-default-authentication-provi
            }

        http.apply(authorizationServerConfigurer)
        // all endpoints only authenticated
        http.securityMatcher(authorizationServerConfigurer.endpointsMatcher)
        http.authorizeHttpRequests { authorizeRequests: AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry
            ->
            authorizeRequests
                .requestMatchers("/oauth2/revoke").permitAll() // TODO: replace this by a custom auth provider for revocation
                .anyRequest().authenticated()

        }
        // we post from everywhere
        http.csrf { csrfConfigurer: CsrfConfigurer<HttpSecurity>
            ->
            csrfConfigurer.ignoringRequestMatchers(*arrayOf(authorizationServerConfigurer.endpointsMatcher))
        }

        // redirect to login page for any exception
        http.exceptionHandling { exceptionHandlingConfigurer: ExceptionHandlingConfigurer<HttpSecurity>
            ->
            exceptionHandlingConfigurer.defaultAuthenticationEntryPointFor(
                LoginUrlAuthenticationEntryPoint(DefaultLoginPageGeneratingFilter.DEFAULT_LOGIN_PAGE_URL),
                MediaTypeRequestMatcher(MediaType.TEXT_HTML)
            )
        }
/*
        // see: https://github.com/spring-projects/spring-authorization-server/blob/main/samples/demo-authorizationserver/src/main/java/sample/config/AuthorizationServerConfig.java
        http.oauth2ResourceServer { oauth2ResourceServer: OAuth2ResourceServerConfigurer<HttpSecurity>
            ->
            oauth2ResourceServer.jwt(Customizer.withDefaults())
        }
*/

        return http.build()
    }

    @Bean
    @Throws(Exception::class)
    @Order(Ordered.HIGHEST_PRECEDENCE + 1)
    fun defaultSecurityFilterChain(
        http: HttpSecurity,
        oAuthProperties: OAuthProperties,
    ): SecurityFilterChain {
        // use our global cors config
        http.cors { } // picks up our default cors config for the token endpoint
        // serve web resources, and protect anything else
        http.authorizeHttpRequests { authorize: AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry ->
            authorize
                // our web resources should be available without authentication
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .requestMatchers(REQUEST_PATH_CONFIRM).permitAll()
                .requestMatchers(REQUEST_PATH_ERROR).permitAll()
                .requestMatchers(REQUEST_PATH_HOME).permitAll()
                .requestMatchers(REQUEST_PATH_REGISTER).permitAll()
                // anything authenticated is fine
                .anyRequest().authenticated()
        }

        // a customized form login
        http.formLogin { formLogin ->
            formLogin
                .loginPage(DefaultLoginPageGeneratingFilter.DEFAULT_LOGIN_PAGE_URL)
                .permitAll()
        }
/*
        // federation oauth2 client login google etc.
        http.oauth2Login { oauth2Login ->
            oauth2Login
                .loginPage(DefaultLoginPageGeneratingFilter.DEFAULT_LOGIN_PAGE_URL)
                .successHandler(authenticationSuccessHandler())
        }
*/
        // send back to application on logout
        http.logout { logout ->
            logout.logoutSuccessUrl(oAuthProperties.appHomeUrl)
        }
        return http.build()
    }

    private fun authenticationSuccessHandler(): AuthenticationSuccessHandler {
        return FederatedIdentityAuthenticationSuccessHandler()
    }
}
