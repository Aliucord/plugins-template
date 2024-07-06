version = "1.0.0" // Plugin version. Increment this to trigger the updater
description = "My first Java plugin!" // Plugin description that will be shown to user

aliucord {
    // Changelog of your plugin
    changelog.set(
        """
        Some changelog
        """.trimIndent()
    )
    // Image or Gif that will be shown at the top of your changelog page
    // changelogMedia.set("https://cool.png")

    // Add additional authors to this plugin
    // author("Name", 0)
    // author("Name", 0)

    // Excludes this plugin from the updater, meaning it won't show up for users.
    // Set this if the plugin is unfinished
    excludeFromUpdaterJson.set(true)
}