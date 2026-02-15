package com.aliucord.plugins

import android.graphics.Color
import android.graphics.Typeface
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import com.aliucord.SpannableBuilder
import com.aliucord.annotations.AliucordPlugin
import com.aliucord.entities.Plugin
import com.aliucord.patcher.AfterPatch
import com.aliucord.utils.ReflectUtils

@AliucordPlugin
class MarkdownFix : Plugin() {
    override fun start(context: android.content.Context) {
        // We patch the method that gets the text to display on screen
        patcher.patch(
            "com.discord.widgets.chat.list.adapter.WidgetChatListAdapterItemMessage",
            "onConfigure",
            arrayOf(Any::class.java),
            AfterPatch { param ->
                val message = param.args[0]
                val content = ReflectUtils.getField(message, "content") as String
                
                // We use a SpannableBuilder to add colors and sizes
                val builder = SpannableBuilder()
                val lines = content.split("\n")

                for ((index, line) in lines.withIndex()) {
                    when {
                        // Big Bold Header: # [space]
                        line.startsWith("# ") -> {
                            val text = line.substring(2)
                            builder.append(text, StyleSpan(Typeface.BOLD), RelativeSizeSpan(1.4f))
                        }
                        
                        // Gray Subtext: -# [space]
                        line.startsWith("-# ") -> {
                            val text = line.substring(3)
                            // 0.8f makes it smaller, Color.GRAY makes it gray
                            builder.append(text, RelativeSizeSpan(0.85f), ForegroundColorSpan(Color.GRAY))
                        }
                        
                        else -> builder.append(line)
                    }
                    
                    // Add the newline back if it's not the last line
                    if (index < lines.size - 1) builder.append("\n")
                }

                // Force Discord to show our styled version
                ReflectUtils.setField(message, "displayContent", builder)
            }
        )
    }

    override fun stop(context: android.content.Context) {
        patcher.unpatchAll()
    }
}
