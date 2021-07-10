# `Aliucord-plugins-template`

Template for an [Aliucord](https://github.com/Aliucord) plugins repo

This template uses Java 11 which requires gradle alpha which is only supported by Android Studio Beta or later.
To use it, you must change your Android Studio updater branch to beta or canary.

Alternatively, you can change `JavaVersion.VERSION_11` to `JavaVersion.VERSION_1_8` in all build.gradle files and follow
the Android Studio instructions to downgrade to Java 8

## Quick Setup

1. Generate a repo based on this template
2. Create a new folder that will hold everything needed
3. Inside this folder, create a folder called buildtool
4. Save the latest [buildtool binary](https://github.com/Aliucord/buildtool/releases/latest) and [example config](https://github.com/Aliucord/buildtool/blob/main/config.example.json) to the buildtools folder
5. Rename the config to `config.json` and correct the androidSDK path in it
6. Clone Aliucord: `git clone https://github.com/Aliucord/Aliucord repo`
7. Clone your generated repo and change the plugins field in buildtool's config.json to the name of your plugin folder
8. Optional but highly recommended (You will need this sooner or later): Download and decompile the latest Discord apk
   1. Download the correct discord apk from <https://aliucord.tk/download/discord?v=VERSION>, for example <https://aliucord.tk/download/discord?v=80202>
   2. Decompile it using [jadx](https://github.com/Juby210/jadx):
      ```sh
      jadx -e --show-bad-code --no-debug-info --no-inline-anonymous --no-inline-methods --no-generate-kotlin-metadata --no-replace-consts --respect-bytecode-access-modifiers --fs-case-sensitive theapk.apk
      ```
9. Your resulting folder structure should look something like this:
    ```
    Plugins
    ├── buildtool
    │   ├── buildtool
    │   └── config.json
    ├── discord.apk
    ├── discord_decompiled
    │   ├── resources
    │   └── sources
    ├── plugins
    │   ├── build.gradle
    │   ├── build.sh
    │   ├── ExamplePlugins
    │   ├── gradle
    │   ├── gradle.properties
    │   ├── gradlew
    │   ├── gradlew.bat
    │   ├── local.properties
    │   ├── README.md
    │   ├── settings.gradle
    │   └── updater.json
    └── repo
        ├── Aliucord
        ├── build.gradle
        ├── DiscordStubs
        ├── gradle
        ├── gradle.properties
        ├── gradlew
        ├── gradlew.bat
        ├── installer
        ├── LICENSE
        ├── local.properties
        ├── manifest.patch
        ├── README.md
        └── settings.gradle
    ```

## Getting started with writing your first plugin

This template includes 4 example plugins which you can find in the ExamplePlugins folder.

1. Copy the `HelloWorld` example plugin into the root of this folder as MyFirstPlugin and fix all values in the plugin Manifest method
2. Uncomment the first line in settings.gradle to tell gradle to include it. Whenever you add a new plugin you have to add it here
3. Inspect the updater.json file and uncomment the MyFirstPlugin section. Whenever you add a new plugin you have to add it to updater.json similar to the MyFirstPlugin block
4. Run `./build.sh MyFirstPlugin` to build and deploy the plugin to your phone and try it out

## License

Everything in this repo is released into the public domain. You may use it however you want with no conditions whatsoever
