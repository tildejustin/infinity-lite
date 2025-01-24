package dev.tildejustin.infinity_lite.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.tildejustin.infinity_lite.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
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
    private int dimension;

    @Inject(method = "setInNetherPortal", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/pattern/BlockPattern$Result;getForwards()Lnet/minecraft/util/math/Direction;"))
    private void getPortalDest(BlockPos pos, CallbackInfo ci) {
        this.dimension = 0;
        BlockEntity blockEntity = this.world.getBlockEntity(pos);
        if (blockEntity instanceof NetherPortalBlockEntity) {
            this.dimension = ((NetherPortalBlockEntity) blockEntity).getDimension();
        }
    }

    @ModifyExpressionValue(method = "tickNetherPortal", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getRegistryKey()Lnet/minecraft/util/registry/RegistryKey;"))
    private RegistryKey<World> fixDestDestDimension(RegistryKey<World> original) {
        return original == World.OVERWORLD ? World.OVERWORLD : World.NETHER;
    }

    @ModifyArg(method = "tickNetherPortal", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;changeDimension(Lnet/minecraft/server/world/ServerWorld;)Lnet/minecraft/entity/Entity;"))
    private ServerWorld switchDestDimension(ServerWorld destination) {
        if (this.dimension == 0) return destination;
        RegistryKey<World> world = null;
        if (this.dimension == 2) world = World.END;
        else if (this.dimension == 260948822) world = InfinityLite.pZ4c;
        return world != null ? this.getServer().getWorld(world) : destination;
    }
}
