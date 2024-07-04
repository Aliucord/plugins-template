package com.github.yournamehere

import android.content.Context
import com.aliucord.annotations.AliucordPlugin
import com.aliucord.entities.MessageEmbedBuilder
import com.aliucord.entities.Plugin
import com.aliucord.patcher.*
import com.aliucord.wrappers.embeds.MessageEmbedWrapper.Companion.title
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
class MyFirstPatch : Plugin() {
    override fun start(context: Context) {
        // Patch that adds an embed with message statistics to each message
        // Patched method is WidgetChatListAdapterItemMessage.onConfigure(int type, ChatListEntry entry)
        patcher.after<WidgetChatListAdapterItemMessage> /* Class whose method to patch */(
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

            // You need to be careful when messing with messages, because they may be loading
            // (user sent a message, and it is currently sending)
            if (entry.message.isLoading) return@after

            // Now add an embed with the statistics

            // This method may be called multiple times per message, e.g. if it is edited,
            // so first remove existing embeds
            entry.message.embeds.removeIf {
                // MessageEmbed.getTitle() is actually obfuscated, but Aliucord provides extensions for commonly used
                // obfuscated Discord classes, so just import the MessageEmbed.title extension and boom goodbye obfuscation!
                it.title == "Message Statistics"
            }

            // Creating embeds is a pain, so Aliucord provides a convenient builder
            MessageEmbedBuilder().run {
                setTitle("Message Statistics")
                addField("Length", (entry.message.content?.length ?: 0).toString(), false)
                addField("ID", entry.message.id.toString(), false)

                entry.message.embeds.add(build())
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