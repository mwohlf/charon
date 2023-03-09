package net.wohlfart.charon.controller

import net.wohlfart.charon.entity.UserDto
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.context.request.WebRequest


@Controller
class AuthController {

    @GetMapping("/error")
    fun getError(request: WebRequest, model: Model): String {
        return "error"
    }

    @GetMapping("/login")
    fun getLogin(request: WebRequest, model: Model): String {
        return "login"
    }

    @GetMapping("/register")
    fun getRegister(request: WebRequest, model: Model): String {
        val userDto = UserDto()
        model.addAttribute("user", userDto)
        return "register"
    }

    @PostMapping("/register")
    fun postRegister(request: WebRequest, model: Model): String {
        return "register"
    }

}
