package com.aura.client;

import com.aura.client.config.AuraConfig;
import com.aura.client.gui.AuraClickGuiScreen;
import com.aura.client.hud.HudRenderer;
import com.aura.client.input.InputTracker;
import com.aura.client.module.ModuleRegistry;
import com.aura.client.module.impl.ArmorDurabilityModule;
import com.aura.client.module.impl.CoordinatesModule;
import com.aura.client.module.impl.CpsModule;
import com.aura.client.module.impl.CrosshairModule;
import com.aura.client.module.impl.DirectionModule;
import com.aura.client.module.impl.FpsModule;
import com.aura.client.module.impl.FullbrightModule;
import com.aura.client.module.impl.KeystrokesModule;
import com.aura.client.module.impl.PingModule;
import com.aura.client.module.impl.PotionTimersModule;
import com.aura.client.module.impl.ZoomModule;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public final class AuraClient implements ClientModInitializer {
    public static final String MOD_ID = "aura";

    private static AuraConfig config;
    private static ModuleRegistry modules;
    private static KeyBinding clickGuiKey;

    @Override
    public void onInitializeClient() {
        config = AuraConfig.load();
        modules = new ModuleRegistry(config);
        modules.register(new FpsModule());
        modules.register(new CoordinatesModule());
        modules.register(new DirectionModule());
        modules.register(new PingModule());
        modules.register(new CpsModule());
        modules.register(new KeystrokesModule());
        modules.register(new PotionTimersModule());
        modules.register(new ArmorDurabilityModule());
        modules.register(new FullbrightModule());
        modules.register(new CrosshairModule());
        modules.register(new ZoomModule());
        modules.load();
        AuraCommands.register();

        clickGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.aura.click_gui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                "category.aura"
        ));

        HudRenderer.register(modules);
        ClientTickEvents.END_CLIENT_TICK.register(AuraClient::onClientTick);
    }

    private static void onClientTick(MinecraftClient client) {
        while (clickGuiKey.wasPressed()) {
            if (client.currentScreen instanceof AuraClickGuiScreen) {
                client.setScreen(null);
            } else {
                client.setScreen(new AuraClickGuiScreen(modules, config));
            }
        }

        InputTracker.tick(client);
        modules.tick(client);
    }

    public static AuraConfig config() {
        return config;
    }

    public static ModuleRegistry modules() {
        return modules;
    }
}
