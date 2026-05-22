package com.aura.client.module.impl;

import com.aura.client.hud.HudModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.PlayerListEntry;

public final class PingModule extends HudModule {
    public PingModule() {
        super("ping", "Ping", "Shows your server latency.", 8, 44);
    }

    @Override
    public void render(DrawContext context, MinecraftClient client) {
        if (client.player == null || client.getNetworkHandler() == null) {
            return;
        }

        PlayerListEntry entry = client.getNetworkHandler().getPlayerListEntry(client.player.getUuid());
        String text = entry == null ? "Ping -- ms" : "Ping " + entry.getLatency() + " ms";
        context.drawText(client.textRenderer, text, x(), y(), 0xFFFFFFFF, shadow.value());
    }
}
