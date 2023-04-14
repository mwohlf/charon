package net.wohlfart.charon.service

import org.springframework.stereotype.Service


@Service
class RegistrationTokenService {

    fun createToken(useId: Int): String {
        return "huetzelbruetzel"
    }

}
