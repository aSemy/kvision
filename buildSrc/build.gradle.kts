plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10")
    implementation("de.marcphilipp.gradle:nexus-publish-plugin:0.4.0")
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:1.7.0") {
        exclude("org.jetbrains.kotlin","kotlin-stdlib-jdk8")
    }
    implementation(gradleApi())
}
