package com.aura.client.mixin;

import com.aura.client.AuraClient;
import com.aura.client.module.impl.FullbrightModule;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LightmapTextureManager.class)
public abstract class LightmapTextureManagerMixin {
    @Shadow
    private NativeImage image;

    @Shadow
    private NativeImageBackedTexture texture;

    @Inject(method = "update", at = @At("TAIL"))
    private void aura$applyFullbright(float delta, CallbackInfo ci) {
        if (!aura$isFullbrightEnabled()) {
            return;
        }

        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
                image.setColor(x, y, -1);
            }
        }
        texture.upload();
    }

    @Unique
    private static boolean aura$isFullbrightEnabled() {
        return AuraClient.modules() != null
                && AuraClient.modules()
                .find("fullbright")
                .filter(module -> module instanceof FullbrightModule)
                .filter(module -> module.enabled())
                .isPresent();
    }
}
