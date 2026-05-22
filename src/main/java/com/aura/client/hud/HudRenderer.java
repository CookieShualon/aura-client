package com.aura.client.hud;

import com.aura.client.module.Module;
import com.aura.client.module.ModuleRegistry;
import com.aura.client.module.OverlayModule;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;

public final class HudRenderer {
    private HudRenderer() {
    }

    public static void register(ModuleRegistry modules) {
        HudRenderCallback.EVENT.register((context, tickCounter) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.options.hudHidden) {
                return;
            }

            for (Module module : modules.all()) {
                if (module instanceof HudModule hudModule && hudModule.enabled()) {
                    hudModule.render(context, client);
                } else if (module instanceof OverlayModule overlayModule && module.enabled()) {
                    overlayModule.renderOverlay(context, client);
                }
            }
        });
    }
}
