package net.wohlfart.charon.controller

import net.wohlfart.charon.api.AccessTokenApi
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("\${net.wohlfart.charon.api.base-path}")
class AccessTokenController() : AccessTokenApi {


}
