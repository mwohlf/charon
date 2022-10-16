package net.wohlfart.charon.component

import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer
import org.springframework.stereotype.Component


@Component
class LoginCustomizer : Customizer<FormLoginConfigurer<HttpSecurity>> {

    override fun customize(configurer: FormLoginConfigurer<HttpSecurity>) {
        configurer.loginPage("/login").permitAll()
    }

}
