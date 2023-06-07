/*
 *
 *
 * $$$$$$$\   $$$$$$\  $$\   $$\        $$$$$$\  $$\       $$$$$$\  $$$$$$$\   $$$$$$\  $$\
 * $$  __$$\ $$  __$$\ $$ | $$  |      $$  __$$\ $$ |     $$  __$$\ $$  __$$\ $$  __$$\ $$ |
 * $$ |  $$ |$$ /  \__|$$ |$$  /       $$ /  \__|$$ |     $$ /  $$ |$$ |  $$ |$$ /  $$ |$$ |
 * $$$$$$$\ |\$$$\    $$$$$  /        $$ |$$$$\ $$ |     $$ |  $$ |$$$$$$$\ |$$$$$$$$ |$$ |
 * $$  __$$\  \____$$\ $$  $$<         $$ |\_$$ |$$ |     $$ |  $$ |$$  __$$\ $$  __$$ |$$ |
 * $$ |  $$ |$$\   $$ |$$ |\$ \        $$ |  $$ |$$ |     $$ |  $$ |$$ |  $$ |$$ |  $$ |$$ |
 * $$$$$$$  |  \$$$  |$$ | \$ \       \$$$    |$$$$$$$$\ $$$$$$  |$$$$$$$  |$$ |  $$ |$$$$$$$$\
 * \_______/  \______/ \__|  \__|       \______/ \________|\______/ \_______/ \__|  \__|\________|
 * $$$$$$$$\ $$$$$$$$\  $$$$$$\  $$\   $$\ $$\    $$\  $$$$$$\  $$\       $$$$$$\   $$$$$$\  $$$$$$\ $$$$$$$$\  $$$$$$\
 * \__$$  __|$$  _____|$$  __$$\ $$ |  $$ |$$$\   $$ |$$  __$$\ $$ |     $$  __$$\ $$  __$$\ \_$$  _|$$  _____|$$  __$$\
 *     $$ |   $$ |      $$ /  \__|$$ |  $$ |$$$$\ $$ |$$ /  $$ |$$ |     $$ /  $$ |$$ /  \__|  $$ |  $$ |      $$ /  \__|
 *     $$ |   $$$$$\    $$ |      $$$$$$$$ |$$ $$\ $ |$$ |  $$ |$$ |     $$ |  $$ |$$ |$$$$\   $$ |  $$$$$\    \$$$\
 *     $$ |   $$  __|   $$ |      $$  __$$ |$$ \$   |$$ |  $$ |$$ |     $$ |  $$ |$$ |\_$$ |  $$ |  $$  __|    \____$$\
 *     $$ |   $$ |      $$ |  $$\ $$ |  $$ |$$ |\$   |$$ |  $$ |$$ |     $$ |  $$ |$$ |  $$ |  $$ |  $$ |      $$\   $$ |
 *     $$ |   $$$$$$$$\ \$$$  |$$   |  $$ |$$ | \$  | $$$$$$  |$$$$$$$$\ $$$$$$  |\$$$  |$$$$$$\ $$$$$$$$\    \$$$   |
 *     \__|   \________| \______/ \__|  \__|\__|  \__| \______/ \________|\______/  \______/ \______|\________| \______/
 *
 *
 *
 *
 *
 * Copyright (c) 2020.  BSK
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 */

package org.kebs.app.kotlin.apollo.api.utils

import com.fasterxml.jackson.core.JsonPointer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.StringWriter
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.dom.DOMSource


sealed class AbstractDocument {
    abstract val document: Any
    abstract fun requiredAt(path: String): AbstractDocument

    /**
     * Convenience method that climbs down a JSON or XML tree
     * Note that if the same expression is used often, it is preferable to construct
     * {@link JsonPointer} instance once and reuse it: this method will not perform
     * any caching of compiled expressions.
     *
     * @param path Expression to access the node
     *
     * @return Document that matches given path: if no match exists,
     *   will return a document for which {@link AbstractDocument#isMissingNode()} returns true.
     *
     */
    abstract fun at(path: String): AbstractDocument?
    abstract fun textValue(): String
    abstract fun emptyCopy(name: String): AbstractDocument
    abstract fun toSerializable(): Any

    fun setNested(path: String, source: AbstractDocument): AbstractDocument {
        val destination = this
        when (source) {
            is JsonDocument -> {
                when (destination) {
                    is JsonDocument -> (destination.document as ObjectNode).setNested(
                        path,
                        source.document,
                            JsonDocument.mapper
                    )
                    is XMLDocument -> {
                        val node = destination.document.ownerDocument.createTextNode(source.textValue())
                        destination.document.setNested(
                            path,
                            node,
                        )
                    }
                }
            }


            is XMLDocument -> {
                when (destination) {
                    is JsonDocument -> {
                        throw Exception("Unexpectedly mapping an XML document to JSON")
                    }
                    is XMLDocument -> {
                        destination.document.setNested(
                            path,
                            source.document,
                        )
                    }
                }
            }
        }
        return destination
    }
}

class JsonDocument(override val document: JsonNode) : AbstractDocument() {
    override fun toString(): String {
        return mapper.writeValueAsString(document)
    }

    override fun requiredAt(path: String): AbstractDocument {
        val jsonPointer = path
            .replace(Delimiters.pointerSeparator, JsonPointer.SEPARATOR)
        return JsonDocument(document.requiredAt(jsonPointer))
    }

    override fun at(path: String): AbstractDocument? {
        return try {
            requiredAt(path)
        } catch (_: Exception) {
            null
        }
    }

    override fun textValue(): String {
        return document.asText()
    }

    override fun emptyCopy(name: String): AbstractDocument {
        return JsonDocument(mapper.readTree("{}") as ObjectNode)
    }

    override fun toSerializable(): Any {
        return document
    }

    companion object {
        val mapper = ObjectMapper()
    }
}

fun ObjectNode.setNested(path: String, value: JsonNode, objectMapper: ObjectMapper) {
    val splitPath = path
        .dropWhile { x -> x == Delimiters.pointerSeparator }
        .split(Delimiters.pointerSeparator)

    val lastName = splitPath.last()
    val notLastNames = splitPath.subList(0, splitPath.size - 1)

    notLastNames.fold(this, { acc, part ->
        if (acc.has(part)) {
            acc.get(part) as ObjectNode
        } else {
            val newNode = objectMapper.createObjectNode()
            acc.set<JsonNode>(part, newNode)
            newNode
        }
    }).set<JsonNode>(lastName, value)
}


class XMLDocument(override val document: Node) : AbstractDocument() {
    override fun toString(): String {
        val stringWriter = StringWriter()
        transformer.transform(DOMSource(document), StreamResult(stringWriter))
        return stringWriter.toString()
    }

    override fun requiredAt(path: String): AbstractDocument {
        return XMLDocument(document.getAt(path))
    }

    override fun at(path: String): AbstractDocument? {
        try {
            return XMLDocument(document.getAt(path))
        } catch (e: Exception) {
            return null
        }
    }

    override fun textValue(): String {
        return document.textContent
    }

    override fun emptyCopy(name: String): AbstractDocument {
        return XMLDocument(documentBuilder.newDocument().createElement(name))
    }

    override fun toSerializable(): Any {
        val stringWriter = StringWriter()
        transformer.transform(DOMSource(document), StreamResult(stringWriter))
        return stringWriter.toString()
    }

    companion object {
        val documentBuilder: DocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        val transformer: Transformer = TransformerFactory.newInstance().newTransformer()
    }
}

typealias XMLNode = Node
typealias XMLElement = Element

// Set nested in XML
fun XMLNode.setNested(path: String, value: XMLNode) {
    val splitPath = pathPartsFrom(path, includeRoot = false)

    val lastTag = splitPath.last()
    val intermediateTags = splitPath.subList(0, splitPath.size - 1)

    // Climb down the tree adding intermediate nodes if necessary
    val parent = intermediateTags.fold(this, { acc, part ->

        val nameAndAttr = part.split(Delimiters.nameAttrSeparator)
        val elementName = nameAndAttr[0]

        // if a matching node exists climb into the node
        // return an existing node or create one

        return@fold acc.childNamed(elementName)
            ?: acc.createChildWithAttributes(nameAndAttr)
    })

    // We currently dont support arrays so drop similarly named XMLNodes
    parent.dropDuplicateNodes(lastTag)

    // Create and append a node with the tag and value
    val newNode: Element = parent.createNodeWithValue(lastTag, value)
    parent.appendChild(newNode)
}

private fun XMLNode.childNamed(
    elementName: String,
): XMLNode? {

    val allChildren: NodeList = this.childNodes
    var matchingChild: XMLNode? = null
    for (i in 0 until allChildren.length) {
        val node = allChildren.item(i)
        if (node.nodeName == elementName) {
            matchingChild = node
            break
        }
    }
    return matchingChild
}

private fun XMLNode.createNodeWithValue(lastName: String, value: XMLNode): XMLElement {
    val newNode: XMLElement = this.ownerDocument.createElement(lastName)
    val textNode = this.ownerDocument.importNode(value, true)
    newNode.appendChild(textNode)
    return newNode
}

private fun XMLNode.dropDuplicateNodes(tagName: String) {
    var shadowingElements = (this as Element).getElementsByTagName(tagName)
    if (shadowingElements.length >= 0) {
        for (i in 0 until shadowingElements.length) {
            this.removeChild(shadowingElements.item(i))
        }
    }
}

private fun XMLNode.createChildWithAttributes(nameAndAttr: List<String>): XMLElement {
    val newNode = (this as Element).ownerDocument.createElement(nameAndAttr[0])

    if (nameAndAttr.count() == 2) {
        nameAndAttr[1].split(Delimiters.attrSeparator).map {
            val attrKeyPair = it.split(Delimiters.attrKeyValueSeparator)
            newNode.setAttribute(attrKeyPair[0], attrKeyPair[1])
        }
    }

    this.appendChild(newNode)
    return newNode
}

fun XMLNode.getAt(path: String): Node {

    val splitPath = pathPartsFrom(path, includeRoot = false)

    splitPath.fold(Pair(this, listOf<String>()), { (currentElement, consumedParts), part ->
        val allChildren: NodeList = (currentElement as Element).childNodes
        val elementName = part.takeWhile { it != Delimiters.nameAttrSeparator }
        for (i in 0 until allChildren.length - 1) {
            var node: Node = allChildren.item(i)

            if (node.nodeName == elementName) {
                if (consumedParts.count() == splitPath.count() - 1 && node.firstChild.nodeType == Node.TEXT_NODE) {
                    return node.firstChild
                } else if (node.nodeType == Node.ELEMENT_NODE) {
                    val elem = node as Element
                    return@fold Pair(elem, consumedParts + listOf(elementName))
                }
            }
        }
        throw Exception("Missing element: There is no $elementName in $consumedParts for \n${this.toString()}")
    })
    throw Exception("Missing element: There is no $path for \n${this.toString()}")
}


/**
Converts "█abc█def█ghi"
into ["def", "ghi"]
 */
fun pathPartsFrom(path: String, includeRoot: Boolean = true): List<String> {
    return path                             // "█abc█def█ghi"
        .split(Delimiters.pointerSeparator) // ["", "abc", "def", "ghi"]
        .drop((if (includeRoot) 1 else 2))  // ["abc","def", "ghi"] or ["def", "ghi"]
}

class Delimiters {
    // Reference the Utils.String module in the web-app
    // These characters are chosen as they are unlikely to appear in the JSON keys or XML keys and attributes
    companion object {
        const val pointerSeparator = '█'

        const val attrKeyValueSeparator = '╪'

        const val attrSeparator = '┌'

        const val nameAttrSeparator = 'τ'
    }
}
