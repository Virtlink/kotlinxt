package dev.pelsmaeker.kotlinxt

/**
 * Helper function for Kotlin that converts an explicitly nullable value `T?` into one that is only null
 * if `T` is a nullable type.
 *
 * For example, if you have a `Map`, its `get` function will return either the value associated
 * with the key, or `null` if not found. Therefore, its return type in Kotlin is `V?`, nullable `V`.
 * However, there is also the option for `get` to return `null` when the key is found but the
 * actual stored value is `null`. Therefore, we do not want to insert an explicit non-null assertion
 * (`!!` in Kotlin) because that would throw an exception if the result is `null`.
 *
 * Instead, this function silences the nullness checker without throwing an exception.
 *
 * @receiver The value of type `T?`
 * @param T The type of the value.
 * @return The value of type `T`, which may be null if `T` is a nullable type.
 */
@Suppress("NOTHING_TO_INLINE")
inline fun <T> T?.cast(): T = Cast.generic(this)
