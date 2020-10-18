package anura.common.fp

import anura.common.ErrorCode
import anura.common.fp.Result.Companion.success
import anura.common.fp.Result.Failure
import anura.common.fp.Result.Success

@Suppress("unused")
sealed class Result<out ERR : ErrorCode, out T> {

    data class Failure<out ERR : ErrorCode>(val error: ERR) : Result<ERR, Nothing>()
    data class Success<out T>(val value: T) : Result<Nothing, T>() {
        companion object {
            operator fun invoke(): Result<Nothing, Unit> = Success(Unit)
        }
    }


    companion object {
        fun <ERR : ErrorCode> failure(err: ERR): Result<ERR, Nothing> = Failure(err)

        fun <T> success(value: T): Result<Nothing, T> = Success(value)

        fun <ERR : ErrorCode, T> ERR?.onSuccess(value: T): Result<ERR, T> =
            this?.let { Failure<ERR>(this) } ?: success(value)

        fun <ERR : ErrorCode, T> fromNullable(opt: T?, orElse: () -> ERR): Result<ERR, T> =
            opt?.let { success(it) } ?: Failure(orElse())

        inline fun <ERR : ErrorCode, A, R> apply(fn: (A) -> R, a: Result<ERR, A>): Result<ERR, R> =
            a.flatMap({ aValue: A -> success(fn(aValue)) })

        inline fun <ERR : ErrorCode, A, B, R> apply(fn: (A, B) -> R, a: Result<ERR, A>, b: Result<ERR, B>): Result<ERR, R> =
            a.flatMap({ aValue: A -> b.flatMap({ bValue: B -> success(fn(aValue, bValue)) }) })

        inline fun <ERR : ErrorCode, A, B, C, R> apply(fn: (A, B, C) -> R, a: Result<ERR, A>, b: Result<ERR, B>, c: Result<ERR, C>): Result<ERR, R> =
            a.flatMap { aValue: A ->
                b.flatMap { bValue: B ->
                    c.flatMap { cValue ->
                        success(fn(aValue, bValue, cValue))
                    }
                }
            }

        inline fun <ERR : ErrorCode, A, B, C, D, R> apply(fn: (A, B, C, D) -> R, a: Result<ERR, A>, b: Result<ERR, B>, c: Result<ERR, C>, d: Result<ERR, D>): Result<ERR, R> =
            a.flatMap { aValue: A ->
                b.flatMap { bValue: B ->
                    c.flatMap { cValue: C ->
                        d.flatMap { dValue: D ->
                            success(fn(aValue, bValue, cValue, dValue))
                        }
                    }
                }
            }

        inline fun <ERR : ErrorCode, A, B, C, D, E, R> apply(fn: (A, B, C, D, E) -> R, a: Result<ERR, A>, b: Result<ERR, B>, c: Result<ERR, C>, d: Result<ERR, D>, e: Result<ERR, E>): Result<ERR, R> =
            a.flatMap { aValue: A ->
                b.flatMap { bValue: B ->
                    c.flatMap { cValue: C ->
                        d.flatMap { dValue: D ->
                            e.flatMap { eValue: E ->
                                success(fn(aValue, bValue, cValue, dValue, eValue))
                            }
                        }
                    }
                }
            }

        inline fun <ERR : ErrorCode, A, B, C, D, E, F, R> apply(fn: (A, B, C, D, E, F) -> R, a: Result<ERR, A>, b: Result<ERR, B>, c: Result<ERR, C>, d: Result<ERR, D>, e: Result<ERR, E>, f: Result<ERR, F>): Result<ERR, R> =
            a.flatMap { aValue: A ->
                b.flatMap { bValue: B ->
                    c.flatMap { cValue: C ->
                        d.flatMap { dValue: D ->
                            e.flatMap { eValue: E ->
                                f.flatMap { fValue: F ->
                                    success(fn(aValue, bValue, cValue, dValue, eValue, fValue))
                                }
                            }
                        }
                    }
                }
            }

        inline fun <ERR : ErrorCode, A, B, C, D, E, F, G, R> apply(fn: (A, B, C, D, E, F, G) -> R, a: Result<ERR, A>, b: Result<ERR, B>, c: Result<ERR, C>, d: Result<ERR, D>, e: Result<ERR, E>, f: Result<ERR, F>, g: Result<ERR, G>): Result<ERR, R> =
            a.flatMap { aValue: A ->
                b.flatMap { bValue: B ->
                    c.flatMap { cValue: C ->
                        d.flatMap { dValue: D ->
                            e.flatMap { eValue: E ->
                                f.flatMap { fValue: F ->
                                    g.flatMap { gValue: G ->
                                        success(fn(aValue, bValue, cValue, dValue, eValue, fValue, gValue))
                                    }
                                }
                            }
                        }
                    }
                }
            }

        inline fun <ERR : ErrorCode, A, B, C, D, E, F, G, H, R> apply(fn: (A, B, C, D, E, F, G, H) -> R, a: Result<ERR, A>, b: Result<ERR, B>, c: Result<ERR, C>, d: Result<ERR, D>, e: Result<ERR, E>, f: Result<ERR, F>, g: Result<ERR, G>, h: Result<ERR, H>): Result<ERR, R> =
            a.flatMap { aValue: A ->
                b.flatMap { bValue: B ->
                    c.flatMap { cValue: C ->
                        d.flatMap { dValue: D ->
                            e.flatMap { eValue: E ->
                                f.flatMap { fValue: F ->
                                    g.flatMap { gValue: G ->
                                        h.flatMap { hValue: H ->
                                            success(fn(aValue, bValue, cValue, dValue, eValue, fValue, gValue, hValue))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        inline fun <ERR : ErrorCode, A, B, C, D, E, F, G, H, I, R> apply(fn: (A, B, C, D, E, F, G, H, I) -> R, a: Result<ERR, A>, b: Result<ERR, B>, c: Result<ERR, C>, d: Result<ERR, D>, e: Result<ERR, E>, f: Result<ERR, F>, g: Result<ERR, G>, h: Result<ERR, H>, i: Result<ERR, I>): Result<ERR, R> =
            a.flatMap { aValue: A ->
                b.flatMap { bValue: B ->
                    c.flatMap { cValue: C ->
                        d.flatMap { dValue: D ->
                            e.flatMap { eValue: E ->
                                f.flatMap { fValue: F ->
                                    g.flatMap { gValue: G ->
                                        h.flatMap { hValue: H ->
                                            i.flatMap { iValue: I ->
                                                success(fn(aValue, bValue, cValue, dValue, eValue, fValue, gValue, hValue, iValue))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }


        inline fun <ERR : ErrorCode, A, B, C, D, E, F, G, H, I, J, R> apply(fn: (A, B, C, D, E, F, G, H, I, J) -> R, a: Result<ERR, A>, b: Result<ERR, B>, c: Result<ERR, C>, d: Result<ERR, D>, e: Result<ERR, E>, f: Result<ERR, F>, g: Result<ERR, G>, h: Result<ERR, H>, i: Result<ERR, I>, j: Result<ERR, J>): Result<ERR, R> =
            a.flatMap { aValue: A ->
                b.flatMap { bValue: B ->
                    c.flatMap { cValue: C ->
                        d.flatMap { dValue: D ->
                            e.flatMap { eValue: E ->
                                f.flatMap { fValue: F ->
                                    g.flatMap { gValue: G ->
                                        h.flatMap { hValue: H ->
                                            i.flatMap { iValue: I ->
                                                j.flatMap { jValue: J ->
                                                    success(fn(aValue, bValue, cValue, dValue, eValue, fValue, gValue, hValue, iValue, jValue))
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
    }
}


inline fun <ERR : ErrorCode, T, U> Result<ERR, T>.flatMap(f: (T) -> Result<ERR, U>): Result<ERR, U> =
    when (this) {
        is Failure -> this
        is Success<T> -> f(this.value)
    }

inline fun <ERR : ErrorCode, T, U> Result<ERR, T>.map(f: (T) -> U): Result<ERR, U> =
    when (this) {
        is Failure -> this
        is Success<T> -> Success(f(value))
    }

inline fun <ERR : ErrorCode, T> Result<ERR, T>.flatMapFailure(f: (ERR) -> Result<ERR, T>): Result<ERR, T> =
    when (this) {
        is Failure -> f(error)
        is Success -> this
    }

inline fun <ERRA : ErrorCode, ERRB : ErrorCode, T> Result<ERRA, T>.mapFailure(f: (ERRA) -> ERRB): Result<ERRB, T> =
    when (this) {
        is Failure -> Failure(f(error))
        is Success -> this
    }


inline fun <ERR : ErrorCode, T> Result<ERR, T>.forEach(f: (T) -> Unit) {
    if (this is Success<T>) f(value)
}

inline fun <ERR : ErrorCode, T> Result<ERR, T>.onEach(f: (T) -> Unit): Result<ERR, T> {
    forEach(f)

    return this
}

inline fun <ERR : ErrorCode, T> Result<ERR, T>.forEachFailure(f: (ERR) -> Unit) {
    if (this is Failure<ERR>) f(error)
}

inline fun <ERR : ErrorCode, T> Result<ERR, T>.onEachFailure(f: (ERR) -> Unit): Result<ERR, T> {
    forEachFailure(f)

    return this
}

inline fun <ERR : ErrorCode, T> Result<ERR, T>.orElse(f: (ERR) -> T): T =
    when (this) {
        is Failure -> f(error)
        is Success<T> -> value
    }

fun <T> T.asSuccess(): Result<Nothing, T> = Success(this)

fun <ERR : ErrorCode> ERR.asFailure(): Result<ERR, Nothing> = Failure(this)

operator fun <ERR : ErrorCode, T> Result<ERR, List<T>>.plus(that: Result<ERR, List<T>>) =
    this.flatMap { e1 -> that.flatMap { e2 -> success(e1 + e2) } }


inline fun <ERR : ErrorCode, T> Result<ERR, T>.onFailure(handler: (Failure<ERR>) -> Nothing): T =
    when (this) {
        is Failure -> handler(this)
        is Success -> value
    }

fun <ERR : ErrorCode, T, U> Iterable<T>.failFastMap(f: (T) -> Result<ERR, U>): Result<ERR, List<U>> =
    success(map { e -> f(e).onFailure { return it } })

fun <ERR : ErrorCode, T, U> Iterable<T>.failFastMapIndexed(f: (T, Int) -> Result<ERR, U>): Result<ERR, List<U>> =
    success(mapIndexed { index, element -> f(element, index).onFailure { return it } })

