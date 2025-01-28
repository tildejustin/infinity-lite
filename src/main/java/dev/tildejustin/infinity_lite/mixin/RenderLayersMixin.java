package dev.tildejustin.infinity_lite.mixin;

import dev.tildejustin.infinity_lite.InfinityLite;
import net.minecraft.block.Block;
import net.minecraft.client.render.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;

@Mixin(RenderLayers.class)
public abstract class RenderLayersMixin {
    @Inject(method = "method_23685", at = @At("TAIL"))
    private static void setNeitherPortalRenderLayer(HashMap<Block, RenderLayer> map, CallbackInfo ci) {
        map.put(InfinityLite.NEITHER_PORTAL, RenderLayer.getTranslucent());
    }
}
