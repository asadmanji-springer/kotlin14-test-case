package anura.common

import kotlin.reflect.KClass

data class ParseError(val description: String) : ErrorCode

fun <T> cannotParse(value: T, klass: KClass<out Any>): ParseError =
    ParseError("Cannot parse value '$value' as ${klass.simpleName}.")
