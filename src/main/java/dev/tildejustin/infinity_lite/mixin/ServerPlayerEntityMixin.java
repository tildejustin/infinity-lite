package dev.tildejustin.infinity_lite.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.*;
import com.mojang.authlib.GameProfile;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {
    public ServerPlayerEntityMixin(World world, BlockPos blockPos, GameProfile gameProfile) {
        super(world, blockPos, gameProfile);
    }

    @Shadow
    public abstract ServerWorld getServerWorld();

    @ModifyExpressionValue(method = "changeDimension", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;getRegistryKey()Lnet/minecraft/util/registry/RegistryKey;", ordinal = 0), slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;push(Ljava/lang/String;)V")))
    private RegistryKey<World> changeDimension(RegistryKey<World> dest) {
        // anything other than end if not dest == end & curr == overworld
        return dest == World.END && this.getServerWorld().getRegistryKey() == World.OVERWORLD ? World.END : World.OVERWORLD;
    }

    @WrapOperation(method = "changeDimension", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;createEndSpawnPlatform(Lnet/minecraft/server/world/ServerWorld;)V"))
    private void makeSpawnPlatform(ServerWorld dest, Operation<Void> original) {
        if (this.getServerWorld().getRegistryKey() == World.OVERWORLD && dest.getRegistryKey() == World.END) {
            original.call(dest);
        } else {
            // ServerWorld#createEndSpawnPlatform
            BlockPos blockPos = this.getBlockPos();
            int i = blockPos.getX();
            int j = blockPos.getY() - 2;
            int k = blockPos.getZ();
            BlockPos.iterate(i - 2, j + 1, k - 2, i + 2, j + 3, k + 2).forEach(pos -> dest.setBlockState(pos, Blocks.AIR.getDefaultState()));
            BlockPos.iterate(i - 2, j, k - 2, i + 2, j, k + 2).forEach(pos -> dest.setBlockState(pos, Blocks.OBSIDIAN.getDefaultState()));
        }
    }
}
