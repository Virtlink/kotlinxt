package dev.pelsmaeker.kotlinxt

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class PreconditionsTests: FunSpec({

    test("should do nothing when the value is not null") {
        // Arrange
        val value: String? = "Hello, world!"

        // Act
        val result = assumeNotNull(value)

        // Assert
        result shouldBe "Hello, world!"
    }

    test("should not throw an exception when the value is null") {
        // Arrange
        val value: String? = null

        // Act
        val result = assumeNotNull(value)

        // Assert
        result shouldBe null
    }

})