plugins {
    `java-library`
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation      (project(":casts"))
    compileOnly         (libs.jsr305)
    testImplementation  (libs.kotest)
}
