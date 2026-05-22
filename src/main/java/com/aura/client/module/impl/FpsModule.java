package com.aura.client.module.impl;

import com.aura.client.hud.HudModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public final class FpsModule extends HudModule {
    public FpsModule() {
        super("fps", "FPS", "Shows your current frames per second.", 8, 8);
    }

    @Override
    public void render(DrawContext context, MinecraftClient client) {
        String text = client.getCurrentFps() + " FPS";
        context.drawText(client.textRenderer, text, x(), y(), 0xFFFFFFFF, shadow.value());
    }

    @Override
    public int width(MinecraftClient client) {
        return client.textRenderer.getWidth(client.getCurrentFps() + " FPS");
    }
}
