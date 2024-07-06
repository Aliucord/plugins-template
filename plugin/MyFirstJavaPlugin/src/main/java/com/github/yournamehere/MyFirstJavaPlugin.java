package com.github.yournamehere;

import android.content.Context;

import com.aliucord.Utils;
import com.aliucord.annotations.AliucordPlugin;
import com.aliucord.api.CommandsAPI;
import com.aliucord.entities.MessageEmbedBuilder;
import com.aliucord.entities.Plugin;
import com.aliucord.patcher.Hook;
import com.aliucord.patcher.InsteadHook;
import com.aliucord.patcher.PreHook;
import com.aliucord.wrappers.embeds.MessageEmbedWrapper;
import com.discord.api.commands.ApplicationCommandType;
import com.discord.models.user.CoreUser;
import com.discord.stores.StoreUserTyping;
import com.discord.widgets.chat.list.adapter.WidgetChatListAdapterItemMessage;
import com.discord.widgets.chat.list.entries.ChatListEntry;
import com.discord.widgets.chat.list.entries.MessageEntry;

import java.util.Arrays;
import java.util.Objects;

// Aliucord Plugin annotation. Must be present on the main class of your plugin
// Plugin class. Must extend Plugin and override start and stop
// Learn more: https://github.com/Aliucord/documentation/blob/main/plugin-dev/1_introduction.md#basic-plugin-structure
@AliucordPlugin(
        requiresRestart = false // Whether your plugin requires a restart after being installed/updated
)
class MyFirstJavaPlugin extends Plugin {
    @Override
    public void start(Context context) throws Throwable {
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
                    String username;

                    // Check if a user argument was passed
                    if (ctx.containsArg("user")) {
                        username = ctx.getRequiredUser("user").getUsername();
                    } else {
                        // Returns either the argument value if present, or the defaultValue ("World" in this case)
                        username = ctx.getStringOrDefault("name", "World");
                    }

                    // Return the final result that will be displayed in chat as a response to the command
                    return new CommandsAPI.CommandResult("Hello " + username + "!");
                }
        );

        // Patch that adds an embed with message statistics to each message
        // Patched method is WidgetChatListAdapterItemMessage.onConfigure(int type, ChatListEntry entry)
        patcher.patch(WidgetChatListAdapterItemMessage.class.getDeclaredMethod("onConfigure", int.class, ChatListEntry.class), new Hook(param -> {
            // see https://api.xposed.info/reference/de/robv/android/xposed/XC_MethodHook.MethodHookParam.html
            // Obtain the second argument passed to the method, so the ChatListEntry
            // Because this is a Message item, it will always be a MessageEntry, so cast it to that
            var entry = (MessageEntry) param.args[1];
            var message = entry.getMessage();

            // You need to be careful when messing with messages, because they may be loading
            // (user sent a message, and it is currently sending)
            if (message.isLoading()) return;

            // Now add an embed with the statistics

            // This method may be called multiple times per message, e.g. if it is edited,
            // so first remove existing embeds
            message.getEmbeds().removeIf(it -> Objects.equals(new MessageEmbedWrapper(it).getTitle(), "Message Statistics"));

            // Creating embeds is a pain, so Aliucord provides a convenient builder
            var embed = new MessageEmbedBuilder()
                    .setTitle("Message Statistics")
                    .addField("Length", message.getContent() != null ? Integer.toString(message.getContent().length()) : "0", false)
                    .addField("ID", Long.toString(message.getId()), false).build();

            message.getEmbeds().add(embed);
        }));

        // Patch that renames Juby to JoobJoob
        patcher.patch(
                CoreUser.class.getDeclaredMethod("getUsername"),
                new PreHook(param -> { // see https://api.xposed.info/reference/de/robv/android/xposed/XC_MethodHook.MethodHookParam.html
                    if (((CoreUser) param.thisObject).getId() == 925141667688878090L) {
                        // setResult() in before patches skips original method invocation
                        param.setResult("JoobJoob");
                    }
                })
        );

        // Patch that hides your typing status by replacing the method and simply doing nothing
        // This patches the method StoreUserTyping.setUserTyping(long channelId)
        patcher.patch(StoreUserTyping.class.getDeclaredMethod("setUserTyping", long.class), InsteadHook.DO_NOTHING);
    }

    @Override
    public void stop(Context context) {
        // Remove all patches
        patcher.unpatchAll();
    }
}