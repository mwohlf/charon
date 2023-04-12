package net.wohlfart.charon.mail

import freemarker.template.Configuration
import freemarker.template.Template
import org.springframework.stereotype.Component
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils
import java.nio.charset.StandardCharsets

private const val FILE_EXTENSION = "ftl"

@Component
class MailTemplateRenderer(private val freemarkerConfig: Configuration) {

    fun render(mailModelAndView: MailModelAndView): String {
        val result = StringBuilder()
        val fileName = "${mailModelAndView.templateName}_${mailModelAndView.locale.language}.$FILE_EXTENSION"
        val template: Template = freemarkerConfig.getTemplate(fileName, StandardCharsets.UTF_8.name())
        val templateIntoString = FreeMarkerTemplateUtils.processTemplateIntoString(template, mailModelAndView)
        result.append(templateIntoString)
        return result.toString()
    }

}
