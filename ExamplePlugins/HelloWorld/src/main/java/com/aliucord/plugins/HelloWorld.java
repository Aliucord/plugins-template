package com.aliucord.plugins;

// Import several packages such as Aliucord's CommandApi and the Plugin class
import android.content.Context;

import com.aliucord.annotations.AliucordPlugin;
import com.aliucord.api.CommandsAPI;
import com.aliucord.entities.Plugin;

import java.util.Collections;

// This class is never used so your IDE will likely complain. Let's make it shut up!
@SuppressWarnings("unused")
@AliucordPlugin
public class HelloWorld extends Plugin {
    @Override
    // Called when your plugin is started. This is the place to register command, add patches, etc
    public void start(Context context) {
        // Registers a command with the name hello, the description "Say hello to the world" and no options
        commands.registerCommand(
                "hello",
                "Say hello to the world",
                Collections.emptyList(),
                // Return a command result with Hello World! as the content, no embeds and send set to false
                ctx -> new CommandsAPI.CommandResult("Hello World!", null, false)
        );
    }

    @Override
    // Called when your plugin is stopped
    public void stop(Context context) {
        // Unregisters all commands
        commands.unregisterAll();
    }
}
