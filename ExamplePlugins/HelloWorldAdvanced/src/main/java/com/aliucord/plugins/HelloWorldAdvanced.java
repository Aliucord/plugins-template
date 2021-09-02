package com.aliucord.plugins;

import android.content.Context;

import com.aliucord.annotations.AliucordPlugin;
import com.aliucord.api.CommandsAPI;
import com.aliucord.entities.Plugin;
import com.discord.api.commands.ApplicationCommandType;
import com.discord.models.commands.ApplicationCommandOption;

import java.util.Arrays;

// This class is never used so your IDE will likely complain. Let's make it shut up!
@SuppressWarnings("unused")
@AliucordPlugin
public class HelloWorldAdvanced extends Plugin {
    @Override
    // Called when your plugin is started. This is the place to register command, add patches, etc
    public void start(Context context) {
        var options = Arrays.asList(
                new ApplicationCommandOption(ApplicationCommandType.STRING, "world", "The name of the world", null, false, true, null, null),
                new ApplicationCommandOption(ApplicationCommandType.USER, "user", "The user to greet", null, false, false, null, null)
        );

        commands.registerCommand(
                "advancedhello",
                "Say hello to the world or a user",
                options,
                ctx -> {
                    // get argument passed to the world option or fall back to Earth if not specified
                    var world = ctx.getStringOrDefault("world", "Earth");

                    // get the user argument
                    var user = ctx.getUser("user");

                    boolean shouldSend;
                    String result;
                    if (user == null) {
                        result = "Hello " + world;

                        // Send locally as clyde
                        shouldSend = false;
                    } else {
                        var userName = user.getUsername();

                        result = String.format("Hello from %s, %s!", world, userName);

                        // We're greeting a user, so let's make sure they can see it!
                        shouldSend = true;
                    }

                    return new CommandsAPI.CommandResult(result, null, shouldSend);
                }
        );
    }

    @Override
    // Called when your plugin is stopped
    public void stop(Context context) {
        // Unregisters all commands
        commands.unregisterAll();
    }
}
