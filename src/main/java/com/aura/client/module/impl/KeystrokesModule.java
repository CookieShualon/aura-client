package com.aura.client.module.impl;

import com.aura.client.hud.HudModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.KeyBinding;

public final class KeystrokesModule extends HudModule {
    private static final int KEY_SIZE = 20;
    private static final int GAP = 2;

    public KeystrokesModule() {
        super("keystrokes", "Keystrokes", "Shows movement, mouse, and jump key states.", 8, 140);
    }

    @Override
    public void render(DrawContext context, MinecraftClient client) {
        drawKey(context, client, "W", client.options.forwardKey, x() + KEY_SIZE + GAP, y(), KEY_SIZE);
        drawKey(context, client, "A", client.options.leftKey, x(), y() + KEY_SIZE + GAP, KEY_SIZE);
        drawKey(context, client, "S", client.options.backKey, x() + KEY_SIZE + GAP, y() + KEY_SIZE + GAP, KEY_SIZE);
        drawKey(context, client, "D", client.options.rightKey, x() + (KEY_SIZE + GAP) * 2, y() + KEY_SIZE + GAP, KEY_SIZE);
        drawKey(context, client, "LMB", client.options.attackKey, x(), y() + (KEY_SIZE + GAP) * 2, KEY_SIZE * 2 + GAP);
        drawKey(context, client, "RMB", client.options.useKey, x() + (KEY_SIZE * 2 + GAP * 2), y() + (KEY_SIZE + GAP) * 2, KEY_SIZE * 2 + GAP);
        drawKey(context, client, "Space", client.options.jumpKey, x(), y() + (KEY_SIZE + GAP) * 3, KEY_SIZE * 4 + GAP * 3);
    }

    private void drawKey(DrawContext context, MinecraftClient client, String label, KeyBinding key, int x, int y, int width) {
        boolean pressed = key.isPressed();
        context.fill(x, y, x + width, y + KEY_SIZE, pressed ? 0xCC52D1B8 : 0xAA101318);
        context.drawBorder(x, y, width, KEY_SIZE, pressed ? 0xFFB8FFF2 : 0xFF2A313B);
        int textX = x + width / 2 - client.textRenderer.getWidth(label) / 2;
        context.drawText(client.textRenderer, label, textX, y + 7, pressed ? 0xFF101318 : 0xFFFFFFFF, false);
    }

    @Override
    public int width(MinecraftClient client) {
        return KEY_SIZE * 4 + GAP * 3;
    }

    @Override
    public int height(MinecraftClient client) {
        return KEY_SIZE * 4 + GAP * 3;
    }
}
