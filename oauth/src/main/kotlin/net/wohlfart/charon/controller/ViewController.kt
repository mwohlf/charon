package net.wohlfart.charon.controller

import jakarta.servlet.http.HttpServletRequest
import mu.KotlinLogging
import net.wohlfart.charon.OAuthProperties
import net.wohlfart.charon.dto.UserDto
import net.wohlfart.charon.service.UserRegistrationService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.Errors
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.context.request.WebRequest

private val logger = KotlinLogging.logger {}

const val REQUEST_PARAM_TOKEN = "token"

const val REQUEST_PATH_ERROR = "error"
const val REQUEST_PATH_LOGIN = "login"
const val REQUEST_PATH_HOME = "home"
const val REQUEST_PATH_REGISTER = "register"
const val REQUEST_PATH_CONFIRM = "confirm"


// base path here is the issuer parameter
@Controller
class ViewController(
    val userRegistrationService: UserRegistrationService,
    val oAuthProperties: OAuthProperties,
) {

    @GetMapping("/$REQUEST_PATH_ERROR")
    fun getError(
        request: WebRequest,
        model: Model,
    ): String {
        return "error" // template name
    }

    @GetMapping("/$REQUEST_PATH_LOGIN")
    fun getLogin(
        request: WebRequest,
        model: Model,
    ): String {
        return "login" // template name
    }

    @GetMapping("/$REQUEST_PATH_HOME")
    fun getHome(
        request: WebRequest,
        model: Model,
    ): String {
        return "redirect:${oAuthProperties.appHomeUrl}"
    }

    @GetMapping("/$REQUEST_PATH_REGISTER")
    fun getRegister(
        request: WebRequest,
        model: Model,
    ): String {
        val userDto = UserDto()
        model.addAttribute("user", userDto)
        return "register" // template name
    }

    @PostMapping("/$REQUEST_PATH_REGISTER")
    fun postRegister(
        @ModelAttribute("user") userDto: UserDto,
        request: HttpServletRequest,
        errors: Errors,
        model: Model,
    ): String {
        logger.info { "post register called: $userDto" }
        userRegistrationService.startRegistration(userDto)
        model.addAttribute("username", userDto.username)
        model.addAttribute("email", userDto.email)
        return "registered" // template name
    }


    @GetMapping("/$REQUEST_PATH_CONFIRM")
    fun getConfirm(
        request: WebRequest,
        @RequestParam(REQUEST_PARAM_TOKEN) tokenValue: String,
    ): String {
        userRegistrationService.finishRegistration(tokenValue)
        return "redirect:${oAuthProperties.appHomeUrl}"
    }

}
