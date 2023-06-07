package org.kebs.app.kotlin.apollo.api.export

import org.kebs.app.kotlin.apollo.api.objects.TemplateExportVariables
import org.kebs.app.kotlin.apollo.config.properties.map.apps.ApplicationMapProperties
import org.springframework.stereotype.Component
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import org.xhtmlrenderer.pdf.ITextRenderer
import java.io.FileOutputStream;
import java.io.OutputStream;


@Component
class ExportFile(
        applicationMapProperties: ApplicationMapProperties
) {
    val uploadDir = applicationMapProperties.localFilePath

    fun parseThymeleafTemplate(template:String, variableName:String, variableValue: Any?): String?
    {
            val templateResolver = ClassLoaderTemplateResolver()
            templateResolver.suffix = ".html"
            templateResolver.templateMode = TemplateMode.HTML
            val templateEngine = TemplateEngine()
            templateEngine.setTemplateResolver(templateResolver)
            val context = Context()
            context.setVariable(variableName, variableValue)
            return templateEngine.process(template, context)
    }

    fun parseThymeleafTemplate(template:String, templateExportVariables:List<TemplateExportVariables>): String?
    {
        val templateResolver = ClassLoaderTemplateResolver()
        templateResolver.suffix = ".html"
        templateResolver.templateMode = TemplateMode.HTML
        val templateEngine = TemplateEngine()
        templateEngine.setTemplateResolver(templateResolver)
        val context = Context()
        for (teVar in templateExportVariables){
            context.setVariable(teVar.name, teVar.value)
        }
        return templateEngine.process(template, context)
    }

    fun generatePdfFromHtml(html: String?) {
        try{
            val outputFolder = uploadDir + "pdf_output.pdf"
            val outputStream: OutputStream = FileOutputStream(outputFolder)
            val renderer = ITextRenderer()
            renderer.setDocumentFromString(html)
            renderer.layout()
            renderer.createPDF(outputStream)
            outputStream.close()
        }  catch (e: Exception){
            e.printStackTrace()
        }
    }

    fun generatePdfFromHtml(html: String, outputFilepath:String) {
        try{
            val outputStream: OutputStream = FileOutputStream(outputFilepath)
            val renderer = ITextRenderer()
            renderer.setDocumentFromString(html)
            renderer.layout()
            renderer.createPDF(outputStream)
            outputStream.close()
        }  catch (e: Exception){
            e.printStackTrace()
        }
    }
}