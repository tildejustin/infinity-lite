package dev.tildejustin.infinity_lite.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.tildejustin.infinity_lite.InfinityLite;
import net.minecraft.client.render.*;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {
    @Shadow
    private ClientWorld world;

    @ModifyConstant(method = "renderSky", constant = @Constant(floatValue = 30.0F))
    private float changeSunSize(float original) {
        return this.world.getRegistryKey() == InfinityLite.pZ4c ? 5.0F : original;
    }

    @ModifyConstant(method = "renderSky", constant = @Constant(floatValue = 20.0F))
    private float changeMoonSize(float original) {
        return this.world.getRegistryKey() == InfinityLite.pZ4c ? 5.0F : original;
    }

    @ModifyExpressionValue(method = "renderSky", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/BufferBuilder;vertex(Lnet/minecraft/util/math/Matrix4f;FFF)Lnet/minecraft/client/render/VertexConsumer;"), slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getRainGradient(F)F"), to = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getMoonPhase()I")))
    private VertexConsumer bufferBuilder(VertexConsumer original) {
        return this.world.getRegistryKey() == InfinityLite.pZ4c ? original.color(0.89094007f, 0.9139131f, 0.2708313f, 1) : original;
    }

    @ModifyExpressionValue(method = "renderSky", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/BufferBuilder;vertex(Lnet/minecraft/util/math/Matrix4f;FFF)Lnet/minecraft/client/render/VertexConsumer;"), slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getMoonPhase()I")))
    private VertexConsumer bufferBuilder2(VertexConsumer original) {
        return this.world.getRegistryKey() == InfinityLite.pZ4c ? original.color(0.47525728f, 0.43430316f, 0.020334244f, 1) : original;
    }
}
