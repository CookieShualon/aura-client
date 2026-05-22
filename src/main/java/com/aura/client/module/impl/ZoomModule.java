package com.aura.client.module.impl;

import com.aura.client.module.Category;
import com.aura.client.module.Module;
import com.aura.client.module.setting.NumberSetting;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public final class ZoomModule extends Module {
    private final NumberSetting fov = addSetting(new NumberSetting("fov", "Zoom FOV", 30.0, 10.0, 70.0, 1.0));
    private final KeyBinding keyBinding;
    private Integer previousFov;

    public ZoomModule() {
        super("zoom", "Zoom", "Hold C to temporarily lower your FOV.", Category.VISUAL, true);
        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.aura.zoom",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_C,
                "category.aura"
        ));
    }

    @Override
    public void tick(MinecraftClient client) {
        if (keyBinding.isPressed()) {
            if (previousFov == null) {
                previousFov = client.options.getFov().getValue();
            }
            client.options.getFov().setValue(fov.value().intValue());
        } else {
            restore(client);
        }
    }

    @Override
    protected void onDisable() {
        restore(MinecraftClient.getInstance());
    }

    private void restore(MinecraftClient client) {
        if (previousFov != null && client.options != null) {
            client.options.getFov().setValue(previousFov);
            previousFov = null;
        }
    }
}
