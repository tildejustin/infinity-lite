package dev.tildejustin.infinity_lite.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.fabricmc.loader.api.*;
import net.minecraft.client.resource.DefaultClientResourcePack;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;

import java.io.*;

@Mixin(DefaultClientResourcePack.class)
public abstract class DefaultClientResourcePackMixin {
    @Unique
    ModContainer mod = FabricLoader.getInstance().getModContainer("infinity-lite").orElseThrow(RuntimeException::new);

    @ModifyExpressionValue(method = "findInputStream", at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/DefaultResourcePack;findInputStream(Lnet/minecraft/resource/ResourceType;Lnet/minecraft/util/Identifier;)Ljava/io/InputStream;"))
    private InputStream replaceAssets(InputStream original, ResourceType type, Identifier id) {
        if (original != null || !id.getPath().contains("neither")) {
            return original;
        }

        return mod.findPath(type.getDirectory() + "/" + id.getNamespace() + "/" + id.getPath()).map(stream -> {
            try {
                return stream.toUri().toURL().openStream();
            } catch (IOException e) {
                System.err.println("Failed to open stream for " + id);
                return null;
            }
        }).orElse(null);
    }
}
