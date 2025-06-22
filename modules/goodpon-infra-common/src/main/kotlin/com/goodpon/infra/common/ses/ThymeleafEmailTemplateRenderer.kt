package com.goodpon.infra.common.ses

import org.springframework.stereotype.Component
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context

@Component
class ThymeleafEmailTemplateRenderer(
    private val templateEngine: TemplateEngine,
) {
    fun render(templateName: String, variables: Map<String, Any>): String {
        val context = Context().apply { setVariables(variables) }
        return templateEngine.process(templateName, context)
    }
}