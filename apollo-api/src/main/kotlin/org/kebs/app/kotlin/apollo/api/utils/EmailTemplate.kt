package org.kebs.app.kotlin.apollo.api.utils

import java.io.File
import java.io.IOException
import java.lang.Exception
import java.nio.file.Files


class EmailTemplate(s: String) {

    private var templateId: String? = null

    private var template: String? = null

    private val replacementParams: Map<String, String>? = null

    fun EmailTemplate(templateId: String) {
        this.templateId = templateId
        try {
            template = loadTemplate(templateId)
        } catch (e: Exception) {
            template = "Empty"
        }
    }

    @Throws(Exception::class)
    private fun loadTemplate(templateId: String): String? {
        val classLoader = javaClass.classLoader
        val file = File(classLoader.getResource(templateId).file)
        var content: String? = "Empty"
        try {
            content = String(Files.readAllBytes(file.toPath()))
        } catch (e: IOException) {
            throw Exception("Could not read template with ID = $templateId")
        }
        return content
    }

    fun getTemplate(replacements: Map<String, String?>): String? {
        var cTemplate = template

        //Replace the String
        for ((key, value) in replacements) {
            cTemplate = cTemplate!!.replace("{{$key}}", value!!)
        }
        return cTemplate
    }
}