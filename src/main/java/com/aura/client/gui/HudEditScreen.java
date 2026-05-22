package com.aura.client.gui;

import com.aura.client.hud.HudModule;
import com.aura.client.module.Module;
import com.aura.client.module.ModuleRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public final class HudEditScreen extends Screen {
    private final ModuleRegistry modules;
    private HudModule dragging;
    private int dragOffsetX;
    private int dragOffsetY;

    public HudEditScreen(ModuleRegistry modules) {
        super(Text.literal("Aura HUD Editor"));
        this.modules = modules;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, width, height, 0x66000000);
        context.drawCenteredTextWithShadow(textRenderer, "Aura HUD Editor", width / 2, 12, 0xFFFFFFFF);
        context.drawCenteredTextWithShadow(textRenderer, "Drag HUD modules. Escape to close.", width / 2, 25, 0xFFB8C0CC);

        MinecraftClient client = MinecraftClient.getInstance();
        for (Module module : modules.all()) {
            if (module instanceof HudModule hudModule && hudModule.enabled()) {
                int boxX = hudModule.x() - 3;
                int boxY = hudModule.y() - 3;
                int boxWidth = hudModule.width(client) + 6;
                int boxHeight = hudModule.height(client) + 6;
                boolean hovered = inBounds(mouseX, mouseY, boxX, boxY, boxWidth, boxHeight);

                context.fill(boxX, boxY, boxX + boxWidth, boxY + boxHeight, hovered || dragging == hudModule ? 0x8852D1B8 : 0x5523272F);
                context.drawBorder(boxX, boxY, boxWidth, boxHeight, hovered || dragging == hudModule ? 0xFF52D1B8 : 0xFF5A6573);
                hudModule.render(context, client);
            }
        }
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != GLFW.GLFW_MOUSE_BUTTON_LEFT) {
            return super.mouseClicked(mouseX, mouseY, button);
        }

        MinecraftClient client = MinecraftClient.getInstance();
        for (Module module : modules.all()) {
            if (module instanceof HudModule hudModule && hudModule.enabled()) {
                int boxX = hudModule.x() - 3;
                int boxY = hudModule.y() - 3;
                int boxWidth = hudModule.width(client) + 6;
                int boxHeight = hudModule.height(client) + 6;
                if (inBounds(mouseX, mouseY, boxX, boxY, boxWidth, boxHeight)) {
                    dragging = hudModule;
                    dragOffsetX = (int) mouseX - hudModule.x();
                    dragOffsetY = (int) mouseY - hudModule.y();
                    return true;
                }
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (dragging != null) {
            MinecraftClient client = MinecraftClient.getInstance();
            int maxX = Math.max(0, width - dragging.width(client));
            int maxY = Math.max(0, height - dragging.height(client));
            dragging.setPosition(clamp((int) mouseX - dragOffsetX, 0, maxX), clamp((int) mouseY - dragOffsetY, 0, maxY));
            return true;
        }

        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (dragging != null) {
            dragging = null;
            modules.save();
            return true;
        }

        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void close() {
        modules.save();
        MinecraftClient.getInstance().setScreen(null);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    private static boolean inBounds(double mouseX, double mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
}
