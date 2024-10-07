// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
}
buildscript {
    repositories {
        google()
        mavenCentral() // Add if missing
    }
    dependencies {
        classpath ("io.realm.kotlin:gradle-plugin:1.10.0") // Adjust the version
    }
}