package com.aura.client.module.impl;

import com.aura.client.hud.HudModule;
import com.aura.client.input.InputTracker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public final class CpsModule extends HudModule {
    public CpsModule() {
        super("cps", "CPS", "Shows left and right clicks per second.", 8, 56);
    }

    @Override
    public void render(DrawContext context, MinecraftClient client) {
        String text = "CPS " + InputTracker.leftCps() + " | " + InputTracker.rightCps();
        context.drawText(client.textRenderer, text, x(), y(), 0xFFFFFFFF, shadow.value());
    }
}
