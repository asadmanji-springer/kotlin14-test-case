package anura.common.json

import anura.common.ErrorCode
import com.fasterxml.jackson.core.JsonPointer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.BooleanNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.NumericNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.node.TextNode
import java.util.Arrays.asList

object Json {
    private val json = JsonNodeFactory.instance

    private fun textNode(value: String): TextNode = json.textNode(value)

    private fun booleanNode(value: Boolean): BooleanNode = json.booleanNode(value)

    private fun numberNode(value: Int): NumericNode = json.numberNode(value)

    fun prop(name: String, textValue: String?) =
        prop(name, textValue?.let(Json::textNode))

    fun prop(name: String, intValue: Int?) =
        prop(name, intValue?.let(Json::numberNode))

    fun prop(name: String, boolValue: Boolean?) =
        prop(name, boolValue?.let(Json::booleanNode))

    fun prop(name: String, value: JsonNode?) =
        name to value

    fun obj(props: Iterable<Pair<String, JsonNode?>?>): ObjectNode =
        json.objectNode().apply {
            props.filterNotNull().forEach { set(it.first, it.second) as ObjectNode }
        }

    fun obj(vararg props: Pair<String, JsonNode?>?): ObjectNode =
        obj(asList(*props))
}

infix fun String.of(textValue: String?) = Json.prop(this, textValue)
infix fun String.of(intValue: Int?) = Json.prop(this, intValue)
infix fun String.of(boolValue: Boolean?) = Json.prop(this, boolValue)
infix fun String.of(node: JsonNode?) = Json.prop(this, node)

data class InvalidJson(val message: String, val pointer: JsonPointer? = null) : ErrorCode

operator fun ObjectNode.contains(name: String) =
    !path(name).isMissingNode
