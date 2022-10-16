package net.wohlfart.charon.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping


@Controller
class AuthController {

    @GetMapping("/login")
    fun login(): String {
        return "login"
    }

    @GetMapping("/logout")
    fun logout(): String {
        return "logout"
    }

    @GetMapping("/error")
    fun error(): String {
        return "error"
    }

}
