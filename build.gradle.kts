plugins {
    `java-library`
    `maven-publish`
    signing
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.gitVersion)          // Set gitVersion() from last Git repository tag
    alias(libs.plugins.nexuspublish)        // Publish on Maven Central
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")
    apply(plugin = "signing")
    apply(plugin = "kotlin")
    apply(plugin = "com.palantir.git-version")

    val gitVersion: groovy.lang.Closure<String> by extra

    group = "net.pelsmaeker.kotlinxt"
    version = gitVersion()
    description = "Cast to a generic type that might be nullable."

    extra["isSnapshotVersion"] = version.toString().endsWith("-SNAPSHOT")
    extra["isDirtyVersion"] = version.toString().endsWith(".dirty")
    extra["isCI"] = !System.getenv("CI").isNullOrEmpty()


    tasks.test {
        useJUnitPlatform()
    }

    kotlin {
        jvmToolchain(11)
    }

    configure<JavaPluginExtension> {
        withSourcesJar()
        withJavadocJar()
    }

    publishing {
        publications {
            create<MavenPublication>("mavenJava") {
                from(components["java"])

                pom {
                    name.set("Kotlin Generic Cast")
                    description.set(project.description)
                    url.set("https://github.com/Virtlink/kotlinxt")
                    inceptionYear.set("2023")
                    licenses {
                        // From: https://spdx.org/licenses/
                        license {
                            name.set("Apache-2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                            distribution.set("repo")
                        }
                    }
                    developers {
                        developer {
                            id.set("virtlink")
                            name.set("Daniel A. A. Pelsmaeker")
                            email.set("d.a.a.pelsmaeker@tudelft.nl")
                        }
                    }
                    scm {
                        connection.set("scm:git@github.com:Virtlink/kotlinxt.git")
                        developerConnection.set("scm:git@github.com:Virtlink/kotlinxt.git")
                        url.set("scm:git@github.com:Virtlink/kotlinxt.git")
                    }
                }
            }
        }
        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/Virtlink/skope")
                credentials {
                    username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
                    password = project.findProperty("gpr.publishKey") as String? ?: System.getenv("GITHUB_TOKEN")
                }
            }
        }
    }

    signing {
        sign(publishing.publications["mavenJava"])
        if (!project.hasProperty("signing.secretKeyRingFile")) {
            // If no secretKeyRingFile was set, we assume an in-memory key in the SIGNING_KEY environment variable (used in CI)
            useInMemoryPgpKeys(
                project.findProperty("signing.keyId") as String? ?: System.getenv("SIGNING_KEY_ID"),
                System.getenv("SIGNING_KEY"),
                project.findProperty("signing.password") as String? ?: System.getenv("SIGNING_KEY_PASSWORD"),
            )
        }
    }

    val checkNotDirty by tasks.registering {
        doLast {
            if (project.extra["isDirtyVersion"] as Boolean) {
                throw GradleException("Cannot publish a dirty version: ${project.version}")
            }
        }
    }

    tasks.publish { dependsOn(checkNotDirty) }
}

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://oss.sonatype.org/content/repositories/snapshots/"))
            username.set(project.findProperty("ossrh.user") as String? ?: System.getenv("OSSRH_USERNAME"))
            password.set(project.findProperty("ossrh.token") as String? ?: System.getenv("OSSRH_TOKEN"))
        }
    }
}