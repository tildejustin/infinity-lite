package dev.tildejustin.infinity_lite.mixin;

import dev.tildejustin.infinity_lite.InfinityLite;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow
    public World world;

    @Unique
    private boolean end;

    @Inject(method = "setInNetherPortal", at = @At("TAIL"))
    private void getPortalDest(BlockPos pos, CallbackInfo ci) {
        if (!InfinityLite.config.enabled) return;

        this.end = false;
        Block block = this.world.getBlockState(pos).getBlock();
        if (block == InfinityLite.NEITHER_PORTAL) {
            this.end = true;
        }
    }

    @ModifyVariable(method = "tickNetherPortal", at = @At("STORE"))
    private RegistryKey<World> switchDestDimension(RegistryKey<World> destination) {
        if (!InfinityLite.config.enabled) return destination;

        if (this.end) {
            // int dim = 2;
            // unfortunately dimensions do not have associated numbers in this version, so hardcoding is the best that can be done
            return World.END;
        }
        return destination;
    }
}
