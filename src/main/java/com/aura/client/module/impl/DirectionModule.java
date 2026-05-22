package com.aura.client.module.impl;

import com.aura.client.hud.HudModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public final class DirectionModule extends HudModule {
    public DirectionModule() {
        super("direction", "Direction", "Shows your facing direction and yaw.", 8, 32);
    }

    @Override
    public void render(DrawContext context, MinecraftClient client) {
        if (client.player == null) {
            return;
        }

        float yaw = client.player.getYaw();
        context.drawText(client.textRenderer, directionFor(yaw) + " " + Math.round(wrapDegrees(yaw)) + " deg", x(), y(), 0xFFFFFFFF, shadow.value());
    }

    private static String directionFor(float yaw) {
        int index = Math.floorMod(Math.round(yaw / 45.0F), 8);
        return switch (index) {
            case 0 -> "S";
            case 1 -> "SW";
            case 2 -> "W";
            case 3 -> "NW";
            case 4 -> "N";
            case 5 -> "NE";
            case 6 -> "E";
            default -> "SE";
        };
    }

    private static float wrapDegrees(float degrees) {
        float wrapped = degrees % 360.0F;
        if (wrapped >= 180.0F) {
            wrapped -= 360.0F;
        }
        if (wrapped < -180.0F) {
            wrapped += 360.0F;
        }
        return wrapped;
    }
}
