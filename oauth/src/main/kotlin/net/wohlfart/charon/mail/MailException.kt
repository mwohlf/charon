package net.wohlfart.charon.mail


class MailException(message: String, cause: Exception) : RuntimeException(message, cause)
