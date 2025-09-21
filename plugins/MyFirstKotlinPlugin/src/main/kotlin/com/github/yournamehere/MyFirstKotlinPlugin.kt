package com.github.yournamehere

import android.content.Context
import com.aliucord.Utils
import com.aliucord.annotations.AliucordPlugin
import com.aliucord.api.CommandsAPI
import com.aliucord.entities.MessageEmbedBuilder
import com.aliucord.entities.Plugin
import com.aliucord.patcher.*
import com.aliucord.wrappers.embeds.MessageEmbedWrapper.Companion.title
import com.discord.api.commands.ApplicationCommandType
import com.discord.models.user.CoreUser
import com.discord.stores.StoreUserTyping
import com.discord.widgets.chat.list.adapter.WidgetChatListAdapterItemMessage
import com.discord.widgets.chat.list.entries.ChatListEntry
import com.discord.widgets.chat.list.entries.MessageEntry

// Aliucord Plugin annotation. Must be present on the main class of your plugin
// Plugin class. Must extend Plugin and override start and stop
// Learn more: https://github.com/Aliucord/documentation/blob/main/plugin-dev/1_introduction.md#basic-plugin-structure
@AliucordPlugin(
    requiresRestart = false // Whether your plugin requires a restart after being installed/updated
)
@Suppress("unused")
class MyFirstKotlinPlugin : Plugin() {
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
        commands.registerCommand(
            "hellowitharguments",
            "Hello World but with arguments!",
            listOf(
                Utils.createCommandOption(
                    ApplicationCommandType.STRING,
                    "name",
                    "Person to say hello to"
                ),
                Utils.createCommandOption(
                    ApplicationCommandType.USER,
                    "user",
                    "User to say hello to"
                )
            )
        ) { ctx ->
            // Check if a user argument was passed
            val username = if (ctx.containsArg("user")) {
                ctx.getRequiredUser("user").username
            } else {
                // Returns either the argument value if present, or the defaultValue ("World" in this case)
                ctx.getStringOrDefault("name", "World")
            }

            // Return the final result that will be displayed in chat as a response to the command
            CommandsAPI.CommandResult("Hello $username!")
        }

        // Patch that adds an embed with message statistics to each message
        // Patched method is WidgetChatListAdapterItemMessage.onConfigure(int type, ChatListEntry entry)
        patcher.after<WidgetChatListAdapterItemMessage>(
            "onConfigure", // Method name
            // Refer to https://kotlinlang.org/docs/reflection.html#class-references
            // and https://docs.oracle.com/javase/tutorial/reflect/class/classNew.html
            Int::class.java, // int type
            ChatListEntry::class.java // ChatListEntry entry
        ) { param ->
            // see https://api.xposed.info/reference/de/robv/android/xposed/XC_MethodHook.MethodHookParam.html
            // Obtain the second argument passed to the method, so the ChatListEntry
            // Because this is a Message item, it will always be a MessageEntry, so cast it to that
            val entry = param.args[1] as MessageEntry
            val message = entry.message

            // You need to be careful when messing with messages, because they may be loading
            // (user sent a message, and it is currently sending)
            if (message.isLoading) return@after

            // Now add an embed with the statistics

            // This method may be called multiple times per message, e.g. if it is edited,
            // so first remove existing embeds
            message.embeds.removeAll {
                // MessageEmbed.getTitle() is actually obfuscated, but Aliucord provides extensions for commonly used
                // obfuscated Discord classes, so just import the MessageEmbed.title extension and boom goodbye obfuscation!
                it.title == "Message Statistics"
            }

            // Creating embeds is a pain, so Aliucord provides a convenient builder
            MessageEmbedBuilder().run {
                setTitle("Message Statistics")
                addField("Length", "${message.content?.length ?: 0}", false)
                addField("ID", message.id.toString(), false)

                message.embeds.add(build())
            }
        }

        // Patch that renames Juby to JoobJoob
        patcher.before<CoreUser>("getUsername") { param ->
            // see https://api.xposed.info/reference/de/robv/android/xposed/XC_MethodHook.MethodHookParam.html
            // in before, after and instead patches, `this` refers to the instance of the class
            // the patched method is on, so the CoreUser instance here
            if (id == 925141667688878090) {
                // setResult() in before patches skips original method invocation
                param.result = "JoobJoob"
            }
        }

        // Patch that hides your typing status by replacing the method and simply doing nothing
        patcher.instead<StoreUserTyping>(
            "setUserTyping",
            Long::class.java // java.lang.Long channelId
        ) { null }
    }

    override fun stop(context: Context) {
        // Remove all patches
        patcher.unpatchAll()
    }
}
