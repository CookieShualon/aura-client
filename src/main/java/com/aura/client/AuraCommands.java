package com.aura.client;

import com.aura.client.gui.AuraClickGuiScreen;
import com.aura.client.gui.HudEditScreen;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public final class AuraCommands {
    private AuraCommands() {
    }

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(literal("aura")
                .then(literal("gui").executes(context -> {
                    context.getSource().getClient().setScreen(new AuraClickGuiScreen(AuraClient.modules(), AuraClient.config()));
                    return 1;
                }))
                .then(literal("hud").executes(context -> {
                    context.getSource().getClient().setScreen(new HudEditScreen(AuraClient.modules()));
                    return 1;
                }))));
    }
}
