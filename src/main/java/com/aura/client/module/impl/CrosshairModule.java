package com.aura.client.module.impl;

import com.aura.client.module.Category;
import com.aura.client.module.Module;
import com.aura.client.module.OverlayModule;
import com.aura.client.module.setting.NumberSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public final class CrosshairModule extends Module implements OverlayModule {
    private final NumberSetting size = addSetting(new NumberSetting("size", "Size", 5.0, 2.0, 12.0, 1.0));
    private final NumberSetting gap = addSetting(new NumberSetting("gap", "Gap", 3.0, 0.0, 8.0, 1.0));
    private final NumberSetting thickness = addSetting(new NumberSetting("thickness", "Thickness", 1.0, 1.0, 4.0, 1.0));

    public CrosshairModule() {
        super("crosshair", "Custom Crosshair", "Replaces the vanilla crosshair with Aura's clean overlay.", Category.VISUAL, false);
    }

    @Override
    public void renderOverlay(DrawContext context, MinecraftClient client) {
        if (client.options.hudHidden || client.currentScreen != null) {
            return;
        }

        int centerX = context.getScaledWindowWidth() / 2;
        int centerY = context.getScaledWindowHeight() / 2;
        int line = size.value().intValue();
        int space = gap.value().intValue();
        int thick = thickness.value().intValue();
        int color = 0xFFFFFFFF;

        context.fill(centerX - space - line, centerY - thick / 2, centerX - space, centerY + Math.max(1, thick - thick / 2), color);
        context.fill(centerX + space, centerY - thick / 2, centerX + space + line, centerY + Math.max(1, thick - thick / 2), color);
        context.fill(centerX - thick / 2, centerY - space - line, centerX + Math.max(1, thick - thick / 2), centerY - space, color);
        context.fill(centerX - thick / 2, centerY + space, centerX + Math.max(1, thick - thick / 2), centerY + space + line, color);
    }
}
