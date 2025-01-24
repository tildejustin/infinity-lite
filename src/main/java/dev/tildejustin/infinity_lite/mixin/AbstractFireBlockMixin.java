package dev.tildejustin.infinity_lite.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.*;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractFireBlock.class)
public abstract class AbstractFireBlockMixin {
    @ModifyExpressionValue(method = "onBlockAdded", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getRegistryKey()Lnet/minecraft/util/registry/RegistryKey;", ordinal = 0))
    private RegistryKey<World> forceTruePt1(RegistryKey<World> original) {
        return World.OVERWORLD;
    }

    @WrapOperation(method = "onBlockAdded", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/NetherPortalBlock;createPortalAt(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean makePortalsInAnyDimension(WorldAccess world, BlockPos pos, Operation<Boolean> original) {
        return ((World) world).getRegistryKey() != World.END && original.call(world, pos);
    }
}
