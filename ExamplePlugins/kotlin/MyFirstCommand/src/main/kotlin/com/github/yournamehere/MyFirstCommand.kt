package com.github.yournamehere

import android.content.Context
import com.aliucord.Utils
import com.aliucord.annotations.AliucordPlugin
import com.aliucord.api.CommandsAPI
import com.aliucord.entities.Plugin
import com.discord.api.commands.ApplicationCommandType

// Aliucord Plugin annotation. Must be present on the main class of your plugin
@AliucordPlugin(requiresRestart = false /* Whether your plugin requires a restart after being installed/updated */)
// Plugin class. Must extend Plugin and override start and stop
// Learn more: https://github.com/Aliucord/documentation/blob/main/plugin-dev/1_introduction.md#basic-plugin-structure
class MyFirstCommand : Plugin() {
    override fun start(context: Context) {
        // Register a command with the name hello and description "My first command!" and no arguments.
        // Learn more: https://github.com/Aliucord/documentation/blob/main/plugin-dev/2_commands.md
        commands.registerCommand("hello", "My first command!") {
            // Just return a command result with hello world as the content
            CommandsAPI.CommandResult(
                "Hello World!",
                null, // List of embeds
                false // Whether to send visible for everyone
            )
        }

        // A bit more advanced command with arguments
        commands.registerCommand("hellowitharguments", "Hello World but with arguments!", listOf(
            Utils.createCommandOption(ApplicationCommandType.STRING, "name", "Person to say hello to"),
            Utils.createCommandOption(ApplicationCommandType.USER, "user", "User to say hello to")
        )) { ctx ->
            // Check if a user argument was passed
            if (ctx.containsArg("user")) {
                val user = ctx.getRequiredUser("user")
                CommandsAPI.CommandResult("Hello ${user.username}!")
            } else {
                // Returns either the argument value if present, or the defaultValue ("World" in this case)
                val name = ctx.getStringOrDefault("name", "World")
                CommandsAPI.CommandResult("Hello $name!")
            }
        }
    }

    override fun stop(context: Context) {
        // Unregister our commands
        commands.unregisterAll()
    }
}
