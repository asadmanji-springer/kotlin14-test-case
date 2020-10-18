package anura.common.json

import anura.common.ErrorCode
import anura.common.fp.Result
import com.fasterxml.jackson.databind.JsonNode

val mandatoryText = LocationAwareJsonNode::getMandatoryText

// a function that takes a location aware json node and a query path, and returns a parsed value
// of type T or a parsing error of type ERR

typealias JsonAttributeParser<ERR, T> = (LocationAwareJsonNode, String) -> Result<ERR, T>

// overloaded functions for up to 8 parameters

fun <ERR : ErrorCode, A> JsonNode.extractAttributes(
    a: Pair<String, JsonAttributeParser<ERR, A>>
): Result<ERR, A> =
    this.locationAware().run { a.second(this, a.first) }

fun <ERR : ErrorCode, A, B> JsonNode.extractAttributes(
    a: Pair<String, JsonAttributeParser<ERR, A>>,
    b: Pair<String, JsonAttributeParser<ERR, B>>
): Result<ERR, Pair<A, B>> =
    this.locationAware().run { Result.apply(::Pair, a.second(this@run, a.first), b.second(this@run, b.first)) }

fun <ERR : ErrorCode, A, B, C> JsonNode.extractAttributes(
    a: Pair<String, JsonAttributeParser<ERR, A>>,
    b: Pair<String, JsonAttributeParser<ERR, B>>,
    c: Pair<String, JsonAttributeParser<ERR, C>>
): Result<ERR, Triple<A, B, C>> =
    this.locationAware().run { Result.apply(::Triple, a.second(this@run, a.first), b.second(this@run, b.first), c.second(this@run, c.first)) }

fun <ERR : ErrorCode, A, B, C, D> JsonNode.extractAttributes(
    a: Pair<String, JsonAttributeParser<ERR, A>>,
    b: Pair<String, JsonAttributeParser<ERR, B>>,
    c: Pair<String, JsonAttributeParser<ERR, C>>,
    d: Pair<String, JsonAttributeParser<ERR, D>>
): Result<ERR, Tuple4<A, B, C, D>> =
    this.locationAware().run { Result.apply(::Tuple4, extractAttributes(a,b,c), extractAttributes(d)) }

fun <ERR : ErrorCode, A, B, C, D, E> JsonNode.extractAttributes(
    a: Pair<String, JsonAttributeParser<ERR, A>>,
    b: Pair<String, JsonAttributeParser<ERR, B>>,
    c: Pair<String, JsonAttributeParser<ERR, C>>,
    d: Pair<String, JsonAttributeParser<ERR, D>>,
    e: Pair<String, JsonAttributeParser<ERR, E>>
): Result<ERR, Tuple5<A, B, C, D, E>> =
    this.locationAware().run {
        Result.apply(::Tuple5, extractAttributes(a,b,c,d), extractAttributes(e))
    }

fun <ERR : ErrorCode, A, B, C, D, E, F> JsonNode.extractAttributes(
    a: Pair<String, JsonAttributeParser<ERR, A>>,
    b: Pair<String, JsonAttributeParser<ERR, B>>,
    c: Pair<String, JsonAttributeParser<ERR, C>>,
    d: Pair<String, JsonAttributeParser<ERR, D>>,
    e: Pair<String, JsonAttributeParser<ERR, E>>,
    f: Pair<String, JsonAttributeParser<ERR, F>>
): Result<ERR, Tuple6<A, B, C, D, E, F>> =
    this.locationAware().run {
        Result.apply(::Tuple6, extractAttributes(a,b,c,d,e), extractAttributes(f))
    }

fun <ERR : ErrorCode, A, B, C, D, E, F, G> JsonNode.extractAttributes(
    a: Pair<String, JsonAttributeParser<ERR, A>>,
    b: Pair<String, JsonAttributeParser<ERR, B>>,
    c: Pair<String, JsonAttributeParser<ERR, C>>,
    d: Pair<String, JsonAttributeParser<ERR, D>>,
    e: Pair<String, JsonAttributeParser<ERR, E>>,
    f: Pair<String, JsonAttributeParser<ERR, F>>,
    g: Pair<String, JsonAttributeParser<ERR, G>>
): Result<ERR, Tuple7<A, B, C, D, E, F, G>> =
    this.locationAware().run {
        Result.apply(::Tuple7, extractAttributes(a,b,c,d,e,f), extractAttributes(g))
    }

fun <ERR : ErrorCode, A, B, C, D, E, F, G, H> JsonNode.extractAttributes(
    a: Pair<String, JsonAttributeParser<ERR, A>>,
    b: Pair<String, JsonAttributeParser<ERR, B>>,
    c: Pair<String, JsonAttributeParser<ERR, C>>,
    d: Pair<String, JsonAttributeParser<ERR, D>>,
    e: Pair<String, JsonAttributeParser<ERR, E>>,
    f: Pair<String, JsonAttributeParser<ERR, F>>,
    g: Pair<String, JsonAttributeParser<ERR, G>>,
    h: Pair<String, JsonAttributeParser<ERR, H>>
): Result<ERR, Tuple8<A, B, C, D, E, F, G, H>> =
    this.locationAware().run {
        Result.apply(::Tuple8, extractAttributes(a,b,c,d,e,f,g), extractAttributes(h))
    }

fun <ERR : ErrorCode, A, B, C, D, E, F, G, H, I> JsonNode.extractAttributes(
    a: Pair<String, JsonAttributeParser<ERR, A>>,
    b: Pair<String, JsonAttributeParser<ERR, B>>,
    c: Pair<String, JsonAttributeParser<ERR, C>>,
    d: Pair<String, JsonAttributeParser<ERR, D>>,
    e: Pair<String, JsonAttributeParser<ERR, E>>,
    f: Pair<String, JsonAttributeParser<ERR, F>>,
    g: Pair<String, JsonAttributeParser<ERR, G>>,
    h: Pair<String, JsonAttributeParser<ERR, H>>,
    i: Pair<String, JsonAttributeParser<ERR, I>>
): Result<ERR, Tuple9<A, B, C, D, E, F, G, H, I>> =
    this.locationAware().run {
        Result.apply(::Tuple9, extractAttributes(a,b,c,d,e,f,g,h), extractAttributes(i))
    }

fun <ERR : ErrorCode, A, B, C, D, E, F, G, H, I, J> JsonNode.extractAttributes(
    a: Pair<String, JsonAttributeParser<ERR, A>>,
    b: Pair<String, JsonAttributeParser<ERR, B>>,
    c: Pair<String, JsonAttributeParser<ERR, C>>,
    d: Pair<String, JsonAttributeParser<ERR, D>>,
    e: Pair<String, JsonAttributeParser<ERR, E>>,
    f: Pair<String, JsonAttributeParser<ERR, F>>,
    g: Pair<String, JsonAttributeParser<ERR, G>>,
    h: Pair<String, JsonAttributeParser<ERR, H>>,
    i: Pair<String, JsonAttributeParser<ERR, I>>,
    j: Pair<String, JsonAttributeParser<ERR, J>>
): Result<ERR, Tuple10<A, B, C, D, E, F, G, H, I, J>> =
    this.locationAware().run {
        Result.apply(::Tuple10, extractAttributes(a,b,c,d,e,f,g,h,i), extractAttributes(j))
    }

fun <ERR : ErrorCode, A, B, C, D, E, F, G, H, I, J, K> JsonNode.extractAttributes(
    a: Pair<String, JsonAttributeParser<ERR, A>>,
    b: Pair<String, JsonAttributeParser<ERR, B>>,
    c: Pair<String, JsonAttributeParser<ERR, C>>,
    d: Pair<String, JsonAttributeParser<ERR, D>>,
    e: Pair<String, JsonAttributeParser<ERR, E>>,
    f: Pair<String, JsonAttributeParser<ERR, F>>,
    g: Pair<String, JsonAttributeParser<ERR, G>>,
    h: Pair<String, JsonAttributeParser<ERR, H>>,
    i: Pair<String, JsonAttributeParser<ERR, I>>,
    j: Pair<String, JsonAttributeParser<ERR, J>>,
    k: Pair<String, JsonAttributeParser<ERR, K>>
): Result<ERR, Tuple11<A, B, C, D, E, F, G, H, I, J, K>> =
    this.locationAware().run {
        Result.apply(::Tuple11, extractAttributes(a,b,c,d,e,f,g,h,i,j), extractAttributes(k))
    }

// extended tuple types not provided by kotlin

data class Tuple4<T1, T2, T3, T4>(val val1: T1, val val2: T2, val val3: T3, val val4: T4) {
    constructor(tuple: Triple<T1,T2,T3>, p1: T4): this(tuple.first, tuple.second, tuple.third, p1)
}

data class Tuple5<T1, T2, T3, T4, T5>(val val1: T1, val val2: T2, val val3: T3, val val4: T4, val val5: T5) {
    constructor(tuple: Tuple4<T1,T2,T3,T4>, p1: T5): this(tuple.val1, tuple.val2, tuple.val3, tuple.val4, p1)
}

data class Tuple6<T1, T2, T3, T4, T5, T6>(val val1: T1, val val2: T2, val val3: T3, val val4: T4, val val5: T5, val val6: T6) {
    constructor(tuple: Tuple5<T1,T2,T3,T4,T5>, p1: T6): this(tuple.val1, tuple.val2, tuple.val3, tuple.val4, tuple.val5, p1)
}

data class Tuple7<T1, T2, T3, T4, T5, T6, T7>(val val1: T1, val val2: T2, val val3: T3, val val4: T4, val val5: T5, val val6: T6, val val7: T7) {
    constructor(tuple: Tuple6<T1,T2,T3,T4,T5,T6>, p1: T7): this(tuple.val1, tuple.val2, tuple.val3, tuple.val4, tuple.val5, tuple.val6, p1)
}

data class Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>(val val1: T1, val val2: T2, val val3: T3, val val4: T4, val val5: T5, val val6: T6, val val7: T7, val val8: T8) {
    constructor(tuple: Tuple7<T1,T2,T3,T4,T5,T6,T7>, p1: T8): this(tuple.val1, tuple.val2, tuple.val3, tuple.val4, tuple.val5, tuple.val6, tuple.val7, p1)
}

data class Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9>(val val1: T1, val val2: T2, val val3: T3, val val4: T4, val val5: T5, val val6: T6, val val7: T7, val val8: T8, val val9: T9) {
    constructor(tuple: Tuple8<T1,T2,T3,T4,T5,T6,T7,T8>, p1: T9): this(tuple.val1, tuple.val2, tuple.val3, tuple.val4, tuple.val5, tuple.val6, tuple.val7, tuple.val8, p1)
}

data class Tuple10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>(val val1: T1, val val2: T2, val val3: T3, val val4: T4, val val5: T5, val val6: T6, val val7: T7, val val8: T8, val val9: T9, val val10: T10) {
    constructor(tuple: Tuple9<T1,T2,T3,T4,T5,T6,T7,T8, T9>, p1: T10): this(tuple.val1, tuple.val2, tuple.val3, tuple.val4, tuple.val5, tuple.val6, tuple.val7, tuple.val8, tuple.val9, p1)
}

data class Tuple11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>(val val1: T1, val val2: T2, val val3: T3, val val4: T4, val val5: T5, val val6: T6, val val7: T7, val val8: T8, val val9: T9, val val10: T10, val val11: T11) {
    constructor(tuple: Tuple10<T1,T2,T3,T4,T5,T6,T7,T8, T9, T10>, p1: T11): this(tuple.val1, tuple.val2, tuple.val3, tuple.val4, tuple.val5, tuple.val6, tuple.val7, tuple.val8, tuple.val9, tuple.val10, p1)
}
