plugins {
    `java-library`
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    compileOnly         (libs.jsr305)
    testImplementation  (libs.kotest)
}
