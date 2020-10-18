package anura.common.fp

import anura.common.ErrorCode
import com.natpryce.hamkrest.Matcher
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.isA

fun <E : ErrorCode, T> Result<E, T>.expectSuccess(): T =
    when (this) {
        is Result.Success -> value
        is Result.Failure -> fail("expected success, but was failure: ${error}")
    }


inline fun <reified E : ErrorCode, T> Result<ErrorCode, T>.expectFailure(matching: Matcher<E>): E =
    when (this) {
        is Result.Success -> fail("expected failure of type ${E::class.simpleName}, but was success: $value")
        is Result.Failure -> {
            assertThat(error, isA(matching))
            error as E
        }
    }

fun fail(message: String): Nothing = throw AssertionError(message)