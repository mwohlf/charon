package net.wohlfart.charon.config

import net.wohlfart.charon.component.HeaderCustomizer
import net.wohlfart.charon.component.LoginCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.SecurityFilterChain


@EnableWebSecurity
class WebSecurityConfig {

    @Bean
    fun defaultSecurityFilterChain(
        http: HttpSecurity,
        userDetailsService: UserDetailsService,
        loginCustomizer: LoginCustomizer,
        headerCustomizer: HeaderCustomizer,
    ): SecurityFilterChain {

        // used for:
        //   http://127.0.0.1:8081/oauth2/token
        //   http://127.0.0.1:8081/oauth2/revoke (?)
        return http
            .cors { }
            .userDetailsService(userDetailsService)
            .formLogin(loginCustomizer)
            .headers(headerCustomizer)
            .build()
    }

}
/*

http://127.0.0.1:8081/oauth2/authorize
?client_id=public-client
&redirect_uri=http%3A%2F%2F127.0.0.1%3A8081
&response_type=code&scope=openid%20profile%20email%20offline_access&nonce=2f1ec08e1ea4c4b37727eabd3ede4c2e9bLb9Bsth&state=984fbad271498ef5d658c9004b5b7e9b3fOVMZcDM&code_challenge=qjHB_jjIvoLsPF-rMRPrtTKJ0_MFQQPpI09-rHJNlbA&code_challenge_method=S256&prompt=none


 */
