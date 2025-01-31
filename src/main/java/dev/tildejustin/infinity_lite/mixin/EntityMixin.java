package dev.tildejustin.infinity_lite.mixin;

import dev.tildejustin.infinity_lite.InfinityLite;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow
    public World world;

    @Unique
    private boolean end;

    @Shadow
    public abstract @Nullable MinecraftServer getServer();

    @Inject(method = "setInNetherPortal", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/pattern/BlockPattern$Result;getForwards()Lnet/minecraft/util/math/Direction;"))
    private void getPortalDest(BlockPos pos, CallbackInfo ci) {
        if (!InfinityLite.enabled) return;

        this.end = false;
        Block block = this.world.getBlockState(pos).getBlock();
        if (block == InfinityLite.NEITHER_PORTAL) {
            this.end = true;
        }
    }

    @SuppressWarnings("DataFlowIssue")
    @ModifyArg(method = "tickNetherPortal", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;changeDimension(Lnet/minecraft/server/world/ServerWorld;)Lnet/minecraft/entity/Entity;"))
    private ServerWorld switchDestDimension(ServerWorld destination) {
        if (!InfinityLite.enabled) return destination;

        if (this.end) {
            // int dim = 2;
            // unfortunately dimensions do not have associated numbers in this version, so hardcoding is the best that can be done
            return this.getServer().getWorld(World.END);
        }
        return destination;
    }
}
