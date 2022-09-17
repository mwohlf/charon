package net.wohlfart.charon.component;

import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer
import org.springframework.stereotype.Component


@Component
class LogoutCustomizer: Customizer<LogoutConfigurer<HttpSecurity>> {

    override fun customize(configurer: LogoutConfigurer<HttpSecurity>?) {

    }

}
