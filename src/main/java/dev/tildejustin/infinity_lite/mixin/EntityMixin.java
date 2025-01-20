package dev.tildejustin.infinity_lite.mixin;

import dev.tildejustin.infinity_lite.NetherPortalBlockEntity;
import net.minecraft.block.entity.BlockEntity;
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

    @Shadow
    public abstract @Nullable MinecraftServer getServer();

    @Unique
    private boolean end;

    @Inject(method = "setInNetherPortal", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/pattern/BlockPattern$Result;getForwards()Lnet/minecraft/util/math/Direction;"))
    private void getPortalDest(BlockPos pos, CallbackInfo ci) {
        this.end = false;
        BlockEntity blockEntity = this.world.getBlockEntity(pos);
        if (blockEntity instanceof NetherPortalBlockEntity) {
            this.end = ((NetherPortalBlockEntity) blockEntity).isEnd();
        }
    }

    @ModifyArg(method = "tickNetherPortal", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;changeDimension(Lnet/minecraft/server/world/ServerWorld;)Lnet/minecraft/entity/Entity;"))
    private ServerWorld switchDestDimension(ServerWorld destination) {
        if (this.end) {
            return this.getServer().getWorld(World.END);
        }
        return destination;
    }
}
