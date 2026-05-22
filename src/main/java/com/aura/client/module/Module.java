package com.aura.client.module;

import com.aura.client.config.AuraConfig;
import com.aura.client.module.setting.Setting;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Module {
    private final String id;
    private final String name;
    private final String description;
    private final Category category;
    private final List<Setting<?>> settings = new ArrayList<>();
    private boolean enabled;
    private boolean expanded;

    protected Module(String id, String name, Category category, boolean enabledByDefault) {
        this(id, name, "", category, enabledByDefault);
    }

    protected Module(String id, String name, String description, Category category, boolean enabledByDefault) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.enabled = enabledByDefault;
    }

    public final String id() {
        return id;
    }

    public final String name() {
        return name;
    }

    public final String description() {
        return description;
    }

    public final Category category() {
        return category;
    }

    public final boolean enabled() {
        return enabled;
    }

    public final void setEnabled(boolean enabled) {
        if (this.enabled == enabled) {
            return;
        }

        this.enabled = enabled;
        if (enabled) {
            onEnable();
        } else {
            onDisable();
        }
    }

    public final void toggle() {
        setEnabled(!enabled);
    }

    public final boolean expanded() {
        return expanded;
    }

    public final void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public final List<Setting<?>> settings() {
        return Collections.unmodifiableList(settings);
    }

    protected final <T extends Setting<?>> T addSetting(T setting) {
        settings.add(setting);
        return setting;
    }

    public void tick(MinecraftClient client) {
    }

    protected void onEnable() {
    }

    protected void onDisable() {
    }

    public void load(AuraConfig.ModuleState state) {
        setEnabled(state.enabled);
        setExpanded(state.expanded);
        for (Setting<?> setting : settings) {
            setting.load(state.settings.get(setting.id()));
        }
    }

    public void save(AuraConfig.ModuleState state) {
        state.enabled = enabled;
        state.expanded = expanded;
        for (Setting<?> setting : settings) {
            state.settings.put(setting.id(), setting.value());
        }
    }
}
