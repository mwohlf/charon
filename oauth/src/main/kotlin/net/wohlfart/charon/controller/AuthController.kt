package net.wohlfart.charon.controller

import jakarta.servlet.http.HttpServletRequest
import mu.KotlinLogging
import net.wohlfart.charon.entity.UserDto
import net.wohlfart.charon.service.SendmailService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.Errors
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.context.request.WebRequest

private val logger = KotlinLogging.logger {}

@Controller
class AuthController(
    val sendmailService: SendmailService,
) {

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
    fun postRegister(
        @ModelAttribute("user") userDto: UserDto,
        request: HttpServletRequest,
        errors: Errors,
    ): String {
        logger.info { "post register called: $userDto" }
        sendmailService.sendEmail()
        return "register"
    }

}
