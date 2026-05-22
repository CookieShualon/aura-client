package com.aura.client.module.impl;

import com.aura.client.hud.HudModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.effect.StatusEffectInstance;

public final class PotionTimersModule extends HudModule {
    public PotionTimersModule() {
        super("potion_timers", "Potion Timers", "Lists active potion effects and remaining time.", 8, 108);
    }

    @Override
    public void render(DrawContext context, MinecraftClient client) {
        if (client.player == null || client.player.getStatusEffects().isEmpty()) {
            return;
        }

        int rowY = y();
        for (StatusEffectInstance effect : client.player.getStatusEffects()) {
            String amplifier = effect.getAmplifier() > 0 ? " " + (effect.getAmplifier() + 1) : "";
            String text = effect.getEffectType().value().getName().getString() + amplifier + " " + formatDuration(effect);
            context.drawText(client.textRenderer, text, x(), rowY, 0xFFFFFFFF, shadow.value());
            rowY += 11;
        }
    }

    private static String formatDuration(StatusEffectInstance effect) {
        if (effect.isInfinite()) {
            return "**:**";
        }

        int seconds = Math.max(0, effect.getDuration() / 20);
        return String.format("%d:%02d", seconds / 60, seconds % 60);
    }

    @Override
    public int height(MinecraftClient client) {
        if (client.player == null || client.player.getStatusEffects().isEmpty()) {
            return 12;
        }
        return Math.max(12, client.player.getStatusEffects().size() * 11);
    }
}
