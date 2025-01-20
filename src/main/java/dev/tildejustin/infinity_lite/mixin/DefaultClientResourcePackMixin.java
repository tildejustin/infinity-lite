package dev.tildejustin.infinity_lite.mixin;

import net.fabricmc.loader.api.*;
import net.minecraft.client.resource.DefaultClientResourcePack;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.*;

@Mixin(DefaultClientResourcePack.class)
public abstract class DefaultClientResourcePackMixin {
    ModContainer mod = FabricLoader.getInstance().getModContainer("infinity-lite").get();

    @Inject(method = "findInputStream", at = @At("HEAD"), cancellable = true)
    private void replaceAssets(ResourceType type, Identifier id, CallbackInfoReturnable<InputStream> cir) {
        mod.findPath("assets/" + id.getNamespace() + "/" + id.getPath()).ifPresent(path -> {
            try {
                cir.setReturnValue(path.toUri().toURL().openStream());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
