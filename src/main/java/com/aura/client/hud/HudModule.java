package com.aura.client.hud;

import com.aura.client.config.AuraConfig;
import com.aura.client.module.Category;
import com.aura.client.module.Module;
import com.aura.client.module.setting.BooleanSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public abstract class HudModule extends Module {
    protected final BooleanSetting shadow = addSetting(new BooleanSetting("shadow", "Text shadow", true));
    private int x;
    private int y;

    protected HudModule(String id, String name, int defaultX, int defaultY) {
        this(id, name, "", defaultX, defaultY);
    }

    protected HudModule(String id, String name, String description, int defaultX, int defaultY) {
        super(id, name, description, Category.HUD, true);
        this.x = defaultX;
        this.y = defaultY;
    }

    public final int x() {
        return x;
    }

    public final int y() {
        return y;
    }

    public final void setPosition(int x, int y) {
        this.x = Math.max(0, x);
        this.y = Math.max(0, y);
    }

    public abstract void render(DrawContext context, MinecraftClient client);

    public int width(MinecraftClient client) {
        return 96;
    }

    public int height(MinecraftClient client) {
        return 12;
    }

    @Override
    public void load(AuraConfig.ModuleState state) {
        super.load(state);
        x = state.hudX;
        y = state.hudY;
    }

    @Override
    public void save(AuraConfig.ModuleState state) {
        super.save(state);
        state.hudX = x;
        state.hudY = y;
    }
}
