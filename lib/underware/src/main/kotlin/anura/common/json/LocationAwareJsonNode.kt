package anura.common.json

import anura.common.ErrorCode
import anura.common.ParseError
import anura.common.cannotParse
import anura.common.fp.Result
import anura.common.fp.Result.Companion.failure
import anura.common.fp.Result.Companion.success
import anura.common.fp.asFailure
import anura.common.fp.asSuccess
import anura.common.fp.failFastMap
import anura.common.fp.failFastMapIndexed
import anura.common.fp.mapFailure
import com.fasterxml.jackson.core.JsonPointer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode
import java.net.MalformedURLException
import java.net.URI
import java.net.URL
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatter.ISO_INSTANT
import java.time.format.DateTimeParseException
import java.util.UUID

class LocationAwareJsonNode internal constructor(val node: JsonNode, private val pointer: JsonPointer) {

    fun exists(path: String): Boolean = ! node.at(path).isMissingNode

    fun <T> parseMandatory(path: String = "/", parser: LocationAwareJsonNode.() -> Result<*, T>): Result<InvalidJson, T> = parseMandatory(path, parser, AnyNodeAccessor)
    fun <T> parseOptional(path: String = "/", parser: LocationAwareJsonNode.() -> Result<*, T>): Result<InvalidJson, T?> = parseOptional(path, parser, AnyNodeAccessor)

    fun getMandatoryText(path: String = "/"): Result<InvalidJson, String> = parseMandatoryText(path) { success(this) }
    fun <T> parseMandatoryText(path: String = "/", parser: String.() -> Result<*, T>): Result<InvalidJson, T> = parseMandatory(path, parser, TextualNodeAccessor)
    fun getOptionalText(path: String = "/"): Result<InvalidJson, String?> = parseOptionalText(path) { success(this) }
    fun <T> parseOptionalText(path: String = "/", parser: String.() -> Result<*, T>): Result<InvalidJson, T?> = parseOptional(path, parser, TextualNodeAccessor)

    fun getMandatoryNumber(path: String = "/"): Result<InvalidJson, Number> = parseMandatoryNumber(path) { success(this) }
    fun <T> parseMandatoryNumber(path: String = "/", parser: Number.() -> Result<*, T>): Result<InvalidJson, T> = parseMandatory(path, parser, NumericNodeAccessor)
    fun getOptionalNumber(path: String = "/"): Result<InvalidJson, Number?> = parseOptionalNumber(path) { success(this) }
    fun <T> parseOptionalNumber(path: String = "/", parser: Number.() -> Result<*, T>): Result<InvalidJson, T?> = parseOptional(path, parser, NumericNodeAccessor)

    fun getMandatoryBoolean(path: String = "/"): Result<InvalidJson, Boolean> = parseMandatoryBoolean(path) { success(this) }
    fun <T> parseMandatoryBoolean(path: String = "/", parser: Boolean.() -> Result<*, T>): Result<InvalidJson, T> = parseMandatory(path, parser, BooleanNodeAccessor)
    fun getOptionalBoolean(path: String = "/"): Result<InvalidJson, Boolean?> = parseOptionalBoolean(path) { success(this) }
    fun <T> parseOptionalBoolean(path: String = "/", parser: Boolean.() -> Result<*, T>): Result<InvalidJson, T?> = parseOptional(path, parser, BooleanNodeAccessor)

    fun getMandatoryObject(path: String = "/"): Result<InvalidJson, JsonNode> = parseMandatory(path, LocationAwareJsonNode::toJsonNode, ObjectNodeAccessor)
    fun <T> parseMandatoryObject(path: String = "/", parser: LocationAwareJsonNode.() -> Result<*, T>): Result<InvalidJson, T> = parseMandatory(path, parser, ObjectNodeAccessor)
    fun getOptionalObject(path: String = "/"): Result<InvalidJson, JsonNode?> = parseOptional(path, LocationAwareJsonNode::toJsonNode, ObjectNodeAccessor)
    fun <T> parseOptionalObject(path: String = "/", parser: LocationAwareJsonNode.() -> Result<*, T>): Result<InvalidJson, T?> = parseOptional(path, parser, ObjectNodeAccessor)
    private fun toJsonNode(): Result<*, JsonNode> = success(node.deepCopy<ObjectNode>())

    fun getMandatoryArray(path: String = "/"): Result<InvalidJson, JsonNode> = parseMandatory(path, LocationAwareJsonNode::toJsonNode, ArrayNodeAccessor)
    fun <T> parseMandatoryArray(path: String = "/", parser: LocationAwareJsonNode.() -> Result<*, T>): Result<InvalidJson, List<T>> = parseMandatory(path, { failFastMap(parser) }, ArrayNodeAsListAccessor)
    fun getOptionalArray(path: String = "/"): Result<InvalidJson, JsonNode?> = parseOptional(path, LocationAwareJsonNode::toJsonNode, ArrayNodeAccessor)
    fun <T> parseOptionalArray(path: String = "/", parser: LocationAwareJsonNode.() -> Result<*, T>): Result<InvalidJson, List<T>?> = parseOptional(path, { failFastMap(parser) }, ArrayNodeAsListAccessor)

    fun <T> parseMandatoryArrayIndexed(path: String = "/", parser: LocationAwareJsonNode.(index: Int) -> Result<*, T>): Result<InvalidJson, List<T>> = parseMandatory(path, { failFastMapIndexed(parser) }, ArrayNodeAsListAccessor)
    fun <T> parseOptionalArrayIndexed(path: String = "/", parser: LocationAwareJsonNode.(index: Int) -> Result<*, T>): Result<InvalidJson, List<T>?> = parseOptional(path, { failFastMapIndexed(parser) }, ArrayNodeAsListAccessor)

    fun getMandatoryTextArray(path: String = "/"): Result<InvalidJson, List<String>> = parseMandatoryArray(path) { getMandatoryText() }

    private fun <In : Any, Out> parseMandatory(path: String, parser: In.() -> Result<*, Out>, accessor: NodeAccessor<In>): Result<InvalidJson, Out> =
        at(path).run {
            when {
                node.isMissingNode || node.isNull -> failure(InvalidJson("Missing field.", pointer))
                else                              -> parse(parser, accessor)
            }
        }

    private fun <In, Out> parseOptional(path: String, parser: In.() -> Result<*, Out>, accessor: NodeAccessor<In>): Result<InvalidJson, Out?> =
        at(path).run {
            when {
                node.isMissingNode || node.isNull -> success(null)
                else                              -> parse(parser, accessor)
            }
        }

    private fun at(path: String): LocationAwareJsonNode =
        with(path.removeSuffix("/")) { LocationAwareJsonNode(node.at(this), pointer.append(JsonPointer.compile(this))) }

    private fun <In, Out> parse(parser: In.() -> Result<*, Out>, accessor: NodeAccessor<In>): Result<InvalidJson, Out> =
        when {
            ! accessor.isCorrectType(node) -> failure(InvalidJson("Expected ${accessor.description} field.", pointer))
            else                           -> parser.invoke(accessor.valueOf(this)).mapFailure { it.toInvalidJson() }
        }

    private fun ErrorCode.toInvalidJson(): InvalidJson =
        when (this) {
            is InvalidJson    -> this
            else              -> InvalidJson(toString(), this@LocationAwareJsonNode.pointer)
        }

    private abstract class NodeAccessor<out T>(val isCorrectType: (JsonNode) -> Boolean, val valueOf: (LocationAwareJsonNode) -> T, val description: String)

    private object AnyNodeAccessor : NodeAccessor<LocationAwareJsonNode>({ true }, { it }, "any")
    private object NumericNodeAccessor : NodeAccessor<Number>(JsonNode::isNumber, { it.node.numberValue() }, "numeric")
    private object TextualNodeAccessor : NodeAccessor<String>(JsonNode::isTextual, { it.node.textValue() }, "textual")
    private object BooleanNodeAccessor : NodeAccessor<Boolean>(JsonNode::isBoolean, { it.node.booleanValue() }, "boolean")
    private object ObjectNodeAccessor : NodeAccessor<LocationAwareJsonNode>(JsonNode::isObject, { it }, "object")
    private object ArrayNodeAccessor : NodeAccessor<LocationAwareJsonNode>(JsonNode::isArray, { it }, "array")
    private object ArrayNodeAsListAccessor : NodeAccessor<List<LocationAwareJsonNode>>(
        JsonNode::isArray,
        { parent ->
            parent.node.mapIndexed { index, child ->
                LocationAwareJsonNode(child, parent.pointer.append(JsonPointer.compile("/$index")))
            }
        },
        "array"
    )

    companion object {
        internal val root = JsonPointer.compile("/")
    }
}

fun JsonNode.locationAware(): LocationAwareJsonNode = LocationAwareJsonNode(this, LocationAwareJsonNode.root)

fun LocationAwareJsonNode.getMandatoryInstant(path: String = "/"): Result<InvalidJson, Instant> = getMandatoryInstant(path, ISO_INSTANT)
fun LocationAwareJsonNode.getMandatoryInstant(path: String = "/", dateTimeFormatter: DateTimeFormatter): Result<InvalidJson, Instant> =
    parseMandatoryText(path) { toInstant(dateTimeFormatter) }

fun LocationAwareJsonNode.getOptionalInstant(path: String = "/", dateTimeFormatter: DateTimeFormatter = ISO_INSTANT): Result<InvalidJson, Instant?> =
    parseOptionalText(path) { toInstant(dateTimeFormatter) }

private fun String.toInstant(dateTimeFormatter: DateTimeFormatter): Result<ParseError, Instant> =
    try {
        success(dateTimeFormatter.parse(this, Instant::from))
    } catch (exception: DateTimeParseException) {
        failure(cannotParse(this, Instant::class))
    }


fun LocationAwareJsonNode.getMandatoryURI(path: String = "/"): Result<InvalidJson, URI> = parseMandatoryText(path) { toURI() }
fun LocationAwareJsonNode.getOptionalURI(path: String = "/"): Result<InvalidJson, URI?> = parseOptionalText(path) { toURI() }

private fun String.toURI(): Result<ParseError, URI> =
    try {
        success(URI.create(this))
    } catch (exception: IllegalArgumentException) {
        failure(cannotParse(this, URI::class))
    }


fun LocationAwareJsonNode.getMandatoryURL(path: String = "/"): Result<InvalidJson, URL> = parseMandatoryText(path) { toURL() }
fun LocationAwareJsonNode.getOptionalURL(path: String = "/"): Result<InvalidJson, URL?> = parseOptionalText(path) { toURL() }

private fun String.toURL(): Result<ParseError, URL> =
    try {
        success(URI.create(this).toURL())
    } catch (exception: IllegalArgumentException) {
        failure(cannotParse(this, URL::class))
    } catch (exception: MalformedURLException) {
        failure(cannotParse(this, URL::class))
    }


fun LocationAwareJsonNode.getMandatoryUUID(path: String = "/"): Result<InvalidJson, UUID> = parseMandatoryText(path) { toUUID() }
fun LocationAwareJsonNode.getOptionalUUID(path: String = "/"): Result<InvalidJson, UUID?> = parseOptionalText(path) { toUUID() }

private fun String.toUUID(): Result<ParseError, UUID> =
    try {
        UUID.fromString(this).asSuccess()
    } catch (exception: IllegalArgumentException) {
        cannotParse(this, UUID::class).asFailure()
    }


inline fun <reified T : Enum<T>> LocationAwareJsonNode.getMandatoryEnum(path: String = "/"): Result<InvalidJson, T> =
    parseMandatoryText(path) {
        try {
            enumValueOf<T>(this).asSuccess()
        } catch (exception: IllegalArgumentException) {
            cannotParse(this, T::class).asFailure()
        }
    }

inline fun <reified T : Enum<T>> LocationAwareJsonNode.getOptionalEnum(path: String = "/"): Result<InvalidJson, T?> =
    parseOptionalText(path) {
        try {
            enumValueOf<T>(this).asSuccess()
        } catch (exception: IllegalArgumentException) {
            cannotParse(this, T::class).asFailure()
        }
    }
