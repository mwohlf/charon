package net.wohlfart.charon.mail

import java.util.*


fun createEnglishRegistration(): MailBuilder {
    return MailBuilder(MailModelAndView("registration", Locale.ENGLISH))
}

class MailBuilder(private val mailModelAndView: MailModelAndView) {

    fun build(): MailModelAndView {
        return mailModelAndView
    }

}

