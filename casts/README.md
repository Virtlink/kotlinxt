# Kotlin XT — Cast

Kotlin has safe (`as? T`) and unsafe (`as T`) casts, but these are not enough when you want to cast to a generic type that might be nullable. This library provides a function `Cast.asGeneric<T>()` that performs this cast.

## Usage
Replace a cast `as T` by the `Cast.asGeneric<T>()` function from this library.  To add this library as a dependency in Gradle:

```kotlin
dependencies {
    implementation("dev.pelsmaeker.kotlinxt:cast:1.0.0")
}
```


## Example use case
Here is an example usage. Let's say we have a Kotlin `Box<T>` class that either holds a value of type `T` or nothing:

```kotlin
class Box<out T> private constructor(
    val value: T?,
    val hasValue: Boolean,
) {
    companion object {
        fun <T> of(value: T): Box<T> = Box<T>(value, true)
        fun <T> empty(): Box<T> = Box<T>(null, false)
    }

    @Suppress("UNCHECKED_CAST")
    fun get(): T = if (hasValue) (value as T) else error("Empty box.")
}
```

With this we can create instances of `Box` where `T` is any type, including nullable types. For example, `Box<String?>` is entirely valid: it is a box that either contains no value, or `null` or a `String`.

Let's say we have a shelf that stores boxes, and a getter that allows you to get a specific box as long as you know its type `T`:

```kotlin
class Shelf(
    val boxes: List<Box<*>>
) {
    @Suppress("UNCHECKED_CAST")
    fun <T> get(i: Int): Box<T> = boxes[i] as Box<T>
}
```

We'd use it like this:

```kotlin
val shelf = Shelf(listOf(
    Box.of<String>("Hello, world!"),    // Non-empty box with non-nullable String
    Box.of<Int>(42),                    // Non-empty box with non-nullable Int
    Box.of<String?>(null),              // Non-empty box with null
    Box.empty<Int>(),                   // Empty box
    Box.empty<String?>(),               // Empty box
    Box.of<String?>("Goodbye, world!"), // Non-empty box with nullable String
))
val box2 = shelf.get<String?>(2)
```

However, this will not work. This code will throw a `NullPointerException` because the cast `as T` in the `Shelf.get` function will check that the value is not null even though `T` is nullable type `String?`. **That's where this library comes in.** Instead, define the `Shelf` class like this to solve this issue:

```kotlin
class Shelf(
    val boxes: List<Box<*>>
) {
    @Suppress("UNCHECKED_CAST")
    fun <T> get(i: Int): Box<T> = boxes[i] as Box<T>
}
```

