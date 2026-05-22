package com.aura.client.module.impl;

import com.aura.client.hud.HudModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;

public final class ArmorDurabilityModule extends HudModule {
    public ArmorDurabilityModule() {
        super("armor_durability", "Armor Durability", "Shows armor pieces and remaining durability.", 8, 80);
    }

    @Override
    public void render(DrawContext context, MinecraftClient client) {
        if (client.player == null) {
            return;
        }

        int slotX = x();
        for (int slot = 3; slot >= 0; slot--) {
            ItemStack stack = client.player.getInventory().getArmorStack(slot);
            if (!stack.isEmpty()) {
                context.drawItem(stack, slotX, y());
                context.drawItemInSlot(client.textRenderer, stack, slotX, y());
                if (stack.isDamageable()) {
                    int remaining = stack.getMaxDamage() - stack.getDamage();
                    String text = String.valueOf(remaining);
                    int textX = slotX + 8 - client.textRenderer.getWidth(text) / 2;
                    context.drawText(client.textRenderer, text, textX, y() + 18, durabilityColor(stack), shadow.value());
                }
            }
            slotX += 24;
        }
    }

    private static int durabilityColor(ItemStack stack) {
        float ratio = (stack.getMaxDamage() - stack.getDamage()) / (float) stack.getMaxDamage();
        if (ratio < 0.25F) {
            return 0xFFFF5555;
        }
        if (ratio < 0.5F) {
            return 0xFFFFFF55;
        }
        return 0xFF55FF55;
    }

    @Override
    public int width(MinecraftClient client) {
        return 96;
    }

    @Override
    public int height(MinecraftClient client) {
        return 30;
    }
}
