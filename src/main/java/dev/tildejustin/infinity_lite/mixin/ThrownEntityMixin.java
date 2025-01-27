package dev.tildejustin.infinity_lite.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.tildejustin.infinity_lite.InfinityLite;
import net.minecraft.block.BlockState;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ThrownEntity.class)
public abstract class ThrownEntityMixin {
    @ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z", ordinal = 0))
    private boolean alsoCheckNeitherPortal(boolean original, @Local BlockState state) {
        if (!InfinityLite.enabled) return original;

        return original || state.isOf(InfinityLite.NEITHER_PORTAL);
    }
}
