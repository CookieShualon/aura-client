package com.aura.client.module.impl;

import com.aura.client.hud.HudModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public final class CoordinatesModule extends HudModule {
    public CoordinatesModule() {
        super("coordinates", "Coordinates", "Shows your block XYZ position.", 8, 20);
    }

    @Override
    public void render(DrawContext context, MinecraftClient client) {
        if (client.player == null) {
            return;
        }

        String text = "XYZ " + client.player.getBlockX() + " " + client.player.getBlockY() + " " + client.player.getBlockZ();
        context.drawText(client.textRenderer, text, x(), y(), 0xFFFFFFFF, shadow.value());
    }

    @Override
    public int width(MinecraftClient client) {
        if (client.player == null) {
            return 90;
        }
        return client.textRenderer.getWidth("XYZ " + client.player.getBlockX() + " " + client.player.getBlockY() + " " + client.player.getBlockZ());
    }
}
