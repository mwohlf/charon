package net.wohlfart.charon.mail

import java.util.*


fun createRegistration(): MailBuilder {
    return MailBuilder(MailModelAndView("registration"))
}

class MailBuilder(private val mailModelAndView: MailModelAndView) {

    fun build(): MailModelAndView {
        return mailModelAndView
    }

    fun english(): MailBuilder {
        mailModelAndView.locale = Locale.ENGLISH
        return this
    }

    fun put(key: String, value: String): MailBuilder {
        mailModelAndView.put(key, value)
        return this
    }

}

