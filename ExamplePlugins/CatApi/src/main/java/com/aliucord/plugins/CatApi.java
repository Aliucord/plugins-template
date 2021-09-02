package com.aliucord.plugins;

import android.content.Context;

import com.aliucord.Http;
import com.aliucord.annotations.AliucordPlugin;
import com.aliucord.api.CommandsAPI;
import com.aliucord.entities.MessageEmbedBuilder;
import com.aliucord.entities.Plugin;
import com.aliucord.plugins.catapi.ApiResponse;

import java.io.IOException;
import java.util.Collections;

// This class is never used so your IDE will likely complain. Let's make it shut up!
@SuppressWarnings("unused")
@AliucordPlugin
public class CatApi extends Plugin {
    @Override
    // Called when your plugin is started. This is the place to register command, add patches, etc
    public void start(Context context) {
        commands.registerCommand(
                "cat",
                "Get a random cat picture",
                Collections.emptyList(),
                ctx -> {
                    try {
                        // Fetch the api and deserialize the resulting Json string into the ApiResponse class
                        ApiResponse res = Http.simpleJsonGet("https://aws.random.cat/meow", ApiResponse.class);

                        // Build a nice embed
                        var eb = new MessageEmbedBuilder()
                                .setRandomColor()
                                .setTitle("Cat!!")
                                // You should specify height and width but we don't have it in this case, so we have to use -1 (the default)
                                // Thus the image will end up square regardless of its dimensions. In a real plugin you would probably want to
                                // load the image and get its dimensions
                                .setImage(res.file)
                                .setFooter("Powered by https://aws.random.cat/meow");
                        // Embeds must be a list, so create a list with  the embed we just built
                        var embeds = Collections.singletonList(eb.build());
                        return new CommandsAPI.CommandResult(null, embeds, false);
                    } catch (IOException ex) {
                        return new CommandsAPI.CommandResult("I'm so sorry... Something went wrong while getting your cat", null, false);
                    }
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
