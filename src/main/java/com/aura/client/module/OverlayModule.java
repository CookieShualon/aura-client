package com.aura.client.module;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public interface OverlayModule {
    void renderOverlay(DrawContext context, MinecraftClient client);
}
