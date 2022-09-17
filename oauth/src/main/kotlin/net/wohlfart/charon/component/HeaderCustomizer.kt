package net.wohlfart.charon.component

import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer
import org.springframework.security.web.header.writers.ContentSecurityPolicyHeaderWriter
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter
import org.springframework.stereotype.Component


@Component
class HeaderCustomizer: Customizer<HeadersConfigurer<HttpSecurity>> {

    override fun customize(configurer: HeadersConfigurer<HttpSecurity>) {
        configurer.addHeaderWriter(ContentSecurityPolicyHeaderWriter())
    }

}
