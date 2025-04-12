package net.pelsmaeker.kotlinxt

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract


/**
 * Satisfies the Kotlin type checker by assuming a value
 * is not `null` without explicitly checking it.
 *
 * For example, if you know a value can never be `null` but it
 * has a nullable type, then you can use this method instead of a
 * non-null assertion (`!!`) operator in Kotlin. This method does not
 * explicitly check nullness.
 *
 * @param T The type of the value.
 * @param value The value of type `T?`.
 * @return The value of type `T`; which should never be `null`.
 */
@OptIn(ExperimentalContracts::class)
@Suppress("NOTHING_TO_INLINE")
inline fun <T : Any> assumeNotNull(value: T?): T {
    contract {
        returns() implies (value != null)
    }
    return value.cast()
}
