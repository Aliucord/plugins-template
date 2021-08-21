# `Aliucord-plugins-template`

Template for an [Aliucord](https://github.com/Aliucord) plugins repo

## Setup

1. Generate a repo based on this template
2. Change Discord version in `build.gradle.kts` to latest version supported by Aliucord

## Getting started with writing your first plugin

This template includes 4 example plugins which you can find in the ExamplePlugins folder.

1. Copy the `HelloWorld` example plugin into the root of this folder as MyFirstPlugin and fix all values in the plugin Manifest method
2. Uncomment the first line in `settings.gradle.kts` to tell gradle to include it. Whenever you add a new plugin you have to add it here
3. Inspect the updater.json file and uncomment the MyFirstPlugin section. Whenever you add a new plugin you have to add it to updater.json similar to the MyFirstPlugin block
4. Run `./gradlew MyFirstPlugin:make` or `./gradlew MyFirstPlugin:deployWithAdb`

## License

Everything in this repo is released into the public domain. You may use it however you want with no conditions whatsoever
