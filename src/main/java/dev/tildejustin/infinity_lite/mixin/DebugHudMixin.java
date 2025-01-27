package dev.tildejustin.infinity_lite.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.hud.DebugHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DebugHud.class)
public abstract class DebugHudMixin {
    @ModifyExpressionValue(method = "getLeftText", at = @At(value = "INVOKE", target = "Lcom/mojang/bridge/game/GameVersion;getName()Ljava/lang/String;", remap = false))
    private String addInfiniteBranding(String text) {
        return text + "infinite (lite)";
    }
}
