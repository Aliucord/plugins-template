@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        maven("https://maven.aliucord.com/releases")
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://maven.aliucord.com/releases")
    }
}

rootProject.name = "aliucord-plugins"
include(":plugins")

// Add each directory under ./plugins as a separate project
rootDir.resolve("plugins")
    .listFiles { file -> file.isDirectory && file.resolve("build.gradle.kts").exists() }!!
    .forEach { include(":plugins:${it.name}") }
