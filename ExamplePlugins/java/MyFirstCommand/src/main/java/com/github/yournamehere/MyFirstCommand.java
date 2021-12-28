package com.github.yournamehere;

import android.content.Context;
import com.aliucord.Utils;
import com.aliucord.annotations.AliucordPlugin;
import com.aliucord.api.CommandsAPI;
import com.aliucord.entities.Plugin;
import com.discord.api.commands.ApplicationCommandType;

import java.util.Arrays;

// Aliucord Plugin annotation. Must be present on the main class of your plugin
@AliucordPlugin(requiresRestart = false /* Whether your plugin requires a restart after being installed/updated */)
// Plugin class. Must extend Plugin and override start and stop
// Learn more: https://github.com/Aliucord/documentation/blob/main/plugin-dev/1_introduction.md#basic-plugin-structure
public class MyFirstCommand extends Plugin {
    @Override
    public void start(Context context) {
        // Register a command with the name hello and description "My first command!" and no arguments.
        // Learn more: https://github.com/Aliucord/documentation/blob/main/plugin-dev/2_commands.md
        commands.registerCommand("hello", "My first command!", ctx -> {
            // Just return a command result with hello world as the content
            return new CommandsAPI.CommandResult(
                    "Hello World!",
                    null, // List of embeds
                    false // Whether to send visible for everyone
            );
        });

        // A bit more advanced command with arguments
        commands.registerCommand(
                "hellowitharguments",
                "Hello World but with arguments!",
                Arrays.asList(
                        Utils.createCommandOption(ApplicationCommandType.STRING, "name", "Person to say hello to"),
                        Utils.createCommandOption(ApplicationCommandType.USER, "user", "User to say hello to")
                ),
                ctx -> {
                    // Check if a user argument was passed
                    if (ctx.containsArg("user")) {
                        var user = ctx.getRequiredUser("user");
                        return new CommandsAPI.CommandResult("Hello " + user.getUsername() + "!");
                    } else {
                        // Returns either the argument value if present, or the defaultValue ("World" in this case)
                        var name = ctx.getStringOrDefault("name", "World");
                        return new CommandsAPI.CommandResult("Hello " + name + "!");
                    }
                }
        );
    }

    @Override
    public void stop(Context context) {
        // Unregister all commands
        commands.unregisterAll();
    }
}
