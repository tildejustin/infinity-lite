package dev.tildejustin.infinity_lite.mixin;

import com.google.common.collect.Sets;
import com.llamalad7.mixinextras.injector.wrapoperation.*;
import dev.tildejustin.infinity_lite.InfinityLite;
import net.minecraft.block.*;
import net.minecraft.world.poi.PointOfInterestType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

import java.util.Set;

@Mixin(PointOfInterestType.class)
public abstract class PointOfInterestTypeMixin {
    @WrapOperation(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/poi/PointOfInterestType;getAllStatesOf(Lnet/minecraft/block/Block;)Ljava/util/Set;", ordinal = 0), slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/block/Blocks;NETHER_PORTAL:Lnet/minecraft/block/Block;")))
    private static Set<BlockState> addNeitherPortalToPortalPOIType(Block block, Operation<Set<BlockState>> original) {
        return Sets.union(original.call(block), original.call(InfinityLite.NEITHER_PORTAL)).immutableCopy();
    }
}
