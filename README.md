# Aliucord Plugin Repo Template

---

Template for an [Aliucord](https://github.com/Aliucord) plugin repo

⚠️ Make sure you check "Include all branches" when using this template \
⚠️ Consider getting familiar with Java and/or Kotlin and Gradle before starting

## Pre-requisites

- Java JDK 11 or newer. OpenJDK recommended
- [Android Studio](https://developer.android.com/studio)

## Getting started with writing your first plugin

This template includes an example plugin written in Kotlin and Java, demonstrating how to implement
a command and patches.

To set up your development environment:

1. Clone this repository to your local machine.
2. Open the cloned repository in Android Studio.
3. Open the gradle build script at [plugin/build.gradle.kts](plugin/build.gradle.kts), read the
   comments and replace all the placeholders
4. Familiarize yourself with the project structure. Most files are commented

To build and deploy your plugin:

- On Linux & Mac, run `./gradlew MyFirstKotlinPlugin:make` to build the plugin.
  Use `./gradlew MyFirstKotlinPlugin:deployWithAdb` to deploy directly to a connected device.
- On Windows, use `.\gradlew.bat MyFirstKotlinPlugin:make`
  and `.\gradlew.bat MyFirstKotlinPlugin:deployWithAdb` for building and deploying, respectively.

## License

Everything in this repo is released into the public domain. You may use it however you want with no
conditions whatsoever
