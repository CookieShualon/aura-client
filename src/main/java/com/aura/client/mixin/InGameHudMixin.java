package com.aura.client.mixin;

import com.aura.client.AuraClient;
import com.aura.client.module.impl.CrosshairModule;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
    private void aura$hideVanillaCrosshair(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (AuraClient.modules() == null) {
            return;
        }

        AuraClient.modules()
                .find("crosshair")
                .filter(module -> module instanceof CrosshairModule)
                .filter(module -> module.enabled())
                .ifPresent(module -> ci.cancel());
    }
}
