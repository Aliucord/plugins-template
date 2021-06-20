package com.aliucord.plugins;

import android.content.Context;

import androidx.annotation.NonNull;

import com.aliucord.entities.MessageEmbedBuilder;
import com.aliucord.entities.Plugin;
import com.aliucord.patcher.PinePatchFn;
import com.discord.widgets.chat.list.entries.ChatListEntry;
import com.discord.widgets.chat.list.entries.MessageEntry;

// This class is never used so your IDE will likely complain. Let's make it shut up!
@SuppressWarnings("unused")
public class MyFirstPatch extends Plugin {
    @NonNull
    @Override
    // Plugin Manifest - Required
    public Manifest getManifest() {
        var manifest = new Manifest();
        manifest.authors = new Manifest.Author[]{new Manifest.Author("DISCORD USERNAME", 123456789L)};
        manifest.description = "My First Patch";
        manifest.version = "1.0.0";
        manifest.updateUrl = "https://raw.githubusercontent.com/USERNAME/REPONAME/builds/updater.json";
        return manifest;
    }


    @Override
    // Called when your plugin is started. This is the place to register command, add patches, etc
    public void start(Context context) {
        var className = "com.discord.widgets.chat.list.adapter.WidgetChatListAdapterItemMessage";
        var methodName = "onConfigure";
        // These are the arguments the method we patch receives. In this case the methods code is
        // public void onConfigure(int i, ChatListEntry chatListEntry), so our methodArguments
        // look like this
        var methodArguments = new Class<?>[] { int.class, ChatListEntry.class };
        patcher.patch(className, methodName, methodArguments, new PinePatchFn(callFrame -> {
            // Obtain the second argument passed to the method, so the chatEntry and cast it to MessageEntry
            // as seen in the Discord Code
            var entry = (MessageEntry) callFrame.args[1];

            // Obtain the actual message object
            var msg = entry.getMessage();
            if (msg == null) return;
            // Make sure message type isn't -1 (Which means the message is by logged in user and currently sending)
            if (msg.getType() == -1) return;

            // Obtain the embeds ArrayList from the message
            var embeds = msg.getEmbeds();
            // Let's add our own!
            var ourEmbed = new MessageEmbedBuilder().setTitle("Hello World").build();
            embeds.add(ourEmbed);
        }));
    }

    @Override
    // Called when your plugin is stopped
    public void stop(Context context) {
        // Remove all patches
        patcher.unpatchAll();
    }
}
