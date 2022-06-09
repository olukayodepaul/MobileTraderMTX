// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        repositories {
            google()
            mavenCentral()
        }
        dependencies {
            classpath(Build.androidBuildTools)
            classpath(Hilt.hilt_gradle_plugin)
            classpath(Build.kotlinGradlePlugin)
            classpath ("com.google.gms:google-services:4.3.10")

        }
    }
}

plugins {
    id ("com.android.application") version "7.2.1" apply false
    id ("org.jetbrains.kotlin.android") version "1.6.10" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
