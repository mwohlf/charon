package net.wohlfart.charon.config

import net.wohlfart.charon.OAuthProperties
import net.wohlfart.charon.controller.REQUEST_PATH_CONFIRM
import net.wohlfart.charon.controller.REQUEST_PATH_ERROR
import net.wohlfart.charon.controller.REQUEST_PATH_HOME
import net.wohlfart.charon.controller.REQUEST_PATH_REGISTER
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint
import org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter


@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
class SecurityFilterChains {

    // see: https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter
    //      https://docs.spring.io/spring-authorization-server/docs/current/reference/html/getting-started.html
    @Bean
    @Throws(Exception::class)
    @Order(1)  // http://127.0.0.1:8081/.well-known/openid-configuration needs to be available before the login
    fun authorizationServerSecurityFilterChain(
        http: HttpSecurity,
        oAuth2AuthorizationService: OAuth2AuthorizationService,
        jwtDecoder: JwtDecoder,
    ): SecurityFilterChain {
        // externalized configurer
        val authorizationServerConfigurer = customAuthServerConfig()
        http.apply(authorizationServerConfigurer)
        // all endpoints only authenticated
        http.securityMatcher(authorizationServerConfigurer.endpointsMatcher)
        http.authorizeHttpRequests { authorizeRequests: AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry
            ->
            authorizeRequests.anyRequest().authenticated()
        }
        // we post from everywhere
        http.csrf { csrfConfigurer: CsrfConfigurer<HttpSecurity>
            ->
            csrfConfigurer.ignoringRequestMatchers(*arrayOf(authorizationServerConfigurer.endpointsMatcher))
        }
        // this picks up our default cors config
        http.cors { }
        // redirect to login page for any exception
        http.exceptionHandling { exceptionHandlingConfigurer: ExceptionHandlingConfigurer<HttpSecurity>
            ->
            exceptionHandlingConfigurer.authenticationEntryPoint(
                LoginUrlAuthenticationEntryPoint(
                    DefaultLoginPageGeneratingFilter.DEFAULT_LOGIN_PAGE_URL
                )
            )
        }

        return http.build()
    }

    @Bean
    @Throws(Exception::class)
    @Order(2)
    fun defaultSecurityFilterChain(
        http: HttpSecurity,
        oAuthProperties: OAuthProperties,
    ): SecurityFilterChain {
        // serve web resources, and protect anything else
        http.authorizeHttpRequests { authorize: AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry ->
                authorize
                    // our web resources should be available without authentication
                    .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                    .requestMatchers(REQUEST_PATH_CONFIRM).permitAll()
                    .requestMatchers(REQUEST_PATH_ERROR).permitAll()
                    .requestMatchers(REQUEST_PATH_HOME).permitAll()
                    .requestMatchers(REQUEST_PATH_REGISTER).permitAll()
                    // .requestMatchers("/h2/**").permitAll()
                    // anything authenticated is fine
                    .anyRequest().authenticated()
            }
        // use our global cors config
        http.cors { } // picks up our default cors config for the token endpoint
        // customized form login
        http.formLogin()
            .loginPage(DefaultLoginPageGeneratingFilter.DEFAULT_LOGIN_PAGE_URL)
            .permitAll()
        // send back to application on logout
        http.logout().logoutSuccessUrl(oAuthProperties.appHomeUrl)
        // this is for the H2 console TODO: not for production
        // http.csrf { csrf -> csrf.disable() } // for the h2 console
        // http.headers().frameOptions().sameOrigin() // which uses frames it seems
        return http.build()
    }

    // this is what happens in
    // OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http)
    private fun customAuthServerConfig(): OAuth2AuthorizationServerConfigurer {
        val authorizationServerConfigurer = OAuth2AuthorizationServerConfigurer()
        authorizationServerConfigurer
            // OpenID Connect 1.0 is disabled in the default configuration, we need to enable it
            .oidc(Customizer.withDefaults())
            .tokenRevocationEndpoint(Customizer.withDefaults())
        return authorizationServerConfigurer
    }

}
