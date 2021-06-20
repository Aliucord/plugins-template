package com.aliucord.plugins;

import android.content.Context;

import androidx.annotation.NonNull;

import com.aliucord.api.CommandsAPI;
import com.aliucord.entities.Plugin;
import com.discord.api.commands.ApplicationCommandType;
import com.discord.models.commands.ApplicationCommandOption;
import com.discord.stores.StoreStream;

import java.util.Arrays;

// This class is never used so your IDE will likely complain. Let's make it shut up!
@SuppressWarnings("unused")
public class HelloWorldAdvanced extends Plugin {
    @NonNull
    @Override
    // Plugin Manifest - Required
    public Manifest getManifest() {
        var manifest = new Manifest();
        manifest.authors = new Manifest.Author[]{new Manifest.Author("DISCORD USERNAME", 123456789L)};
        manifest.description = "Advanced Hello World";
        manifest.version = "1.0.0";
        manifest.updateUrl = "https://raw.githubusercontent.com/USERNAME/REPONAME/builds/updater.json";
        return manifest;
    }


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
                args -> {
                    // get argument passed to the world option
                    var world = (String) args.get("world");
                    // This argument was specified as optional, so it may be null, in that case assign a default
                    if (world == null) world = "Earth";

                    // get the user argument
                    var userId = (String) args.get("user");

                    boolean shouldSend;
                    String result;
                    if (userId == null) {
                        result = "Hello " + world;

                        // Send locally as clyde
                        shouldSend = false;
                    } else {
                        // The UserID we get is a String, but Discord uses longs, so parse it to a long
                        long parsedUserId = Long.parseLong(userId);
                        // Get the UserStore. This is Discords internal Manager that has all cached Users
                        var userStore = StoreStream.getUsers();
                        // Try to get the mentioned user
                        var user = userStore.getUsers().get(parsedUserId);
                        var userName = user != null ? user.getUsername() : "Stranger" ;

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
